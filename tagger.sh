#!/bin/bash

usage() {
	echo "$(basename $0) -p previous_version -v new_version -x" >&2
	echo "" >&2
	echo "where:" >&2
	echo "     -p previous version" >&2
	echo "     -v new version" >&2
	echo "     -x execute (otherwise, is a dry run)" >&2
	echo "" >&2
	echo "eg:" >&2
	echo "    $(basename $0) -p 3.4.0 -v 3.5.0 -x" >&2
	echo "" >&2
	exit 1
}

PREV_VERSION=""
NEW_VERSION=""
EXECUTE=""

while getopts ":p:v:x" opt; do
  case ${opt} in
    p)
      PREV_VERSION=$OPTARG
      ;;
    v)
      NEW_VERSION=$OPTARG
      ;;
    x)
      EXECUTE="true"
      ;;
    \? ) usage "Invalid option"
      exit 1
      ;;
    : )
      echo "Option -$OPTARG requires an argument." 1>&2; exit 1;;
  esac
done
shift $((OPTIND -1))


if [ -z "$PREV_VERSION" ] || [ -z "$NEW_VERSION" ]
then
	usage
fi


echo "-p PREV_VERSION : $PREV_VERSION"
echo "-v NEW_VERSION  : $NEW_VERSION"
echo "-x EXECUTE      : $EXECUTE"


update_pom_version() {
  local file="$1"
  local prev="$2"
  local new="$3"

  if [[ "$OSTYPE" == "darwin"* ]]; then
    sed -i '' "s|<version>$prev</version>|<version>$new</version>|g" "$file"
  else
    sed -i "s|<version>$prev</version>|<version>$new</version>|g" "$file"
  fi
}

update_poms_and_commit_if_necessary() {
  local prev="$1"
  local new="$2"
  local exec="$3"

  echo "update all poms to $new and commit if changed..."
  if [ "$exec" = "true" ]
  then
    for POM_XML in $(find . -name "pom.xml")
    do
      update_pom_version "$POM_XML" "$prev" "$new"
    done
    if [ -n "$(git status --porcelain)" ]
    then
      git add .
      git commit --amend --no-edit
    fi
  fi
}

PREV_TAG=""
for TAG in $(git tag -l | grep "tags/$PREV_VERSION/")
do
	NEW_TAG="tags/$NEW_VERSION/$(echo $TAG | cut -c12-)"

	if [ -z "$PREV_TAG" ]
	then
    echo "git reset --hard $PREV_VERSION"
	  if [ "$EXECUTE" = "true" ]
	  then
  	  git reset --hard $PREV_VERSION
    fi
    update_poms_and_commit_if_necessary "$PREV_VERSION" "$NEW_VERSION" "$EXECUTE"
  else
	  for COMMIT in $(git log $PREV_TAG..$TAG --pretty=format:"%H" --reverse)
	  do

      echo "git cherry-pick $COMMIT"
      if [ "$EXECUTE" = "true" ]
      then
        git cherry-pick $COMMIT

        if [ $? -ne 0 ]
        then
            echo "Cherry-pick failed; aborting."
            exit 1
        fi
      fi

      update_poms_and_commit_if_necessary "$PREV_VERSION" "$NEW_VERSION" "$EXECUTE"
    done
	fi

  echo "git tag -f $NEW_TAG"
  if [ "$EXECUTE" = "true" ]
  then
	  git tag -d $NEW_TAG >/dev/null 2>&1
	  git tag -f $NEW_TAG
  fi

	PREV_TAG=$TAG
done
