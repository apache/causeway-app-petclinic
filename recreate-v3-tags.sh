#!/usr/bin/env bash
usage() {
  echo "$(basename $0): [-x] -P" >&2
  echo "  -x : execute; otherwise acts as a dry-run" >&2
  echo "  -P : skip push; otherwise will push all changes to origin" >&2
  echo "" >&2
}

EXECUTE="no"
PUSH="yes"

while getopts ":hPx" arg; do
  case $arg in
    h)
      usage
      exit 0
      ;;
    x)
      EXECUTE="yes"
      ;;
    P)
      PUSH="no"
      ;;
    *)
      usage
      exit 1
  esac
done

shift $((OPTIND-1))

echo "-x : EXECUTE : ${EXECUTE}"
echo "-P : PUSH    : ${PUSH}"

for TAGV3 in $(git tag | grep v3)
do

  echo git tag -d $TAGV3
  if [ "$EXECUTE" = "yes" ]
  then
    git tag -d $TAGV3
  fi

  if [ "$PUSH" = "yes" ]
  then
    echo git push origin $TAGV3 --delete
    if [ "$EXECUTE" = "yes" ]
    then
      git push origin $TAGV3 --delete
    fi
  fi

done


PREV=
for TAGV2 in $(git tag | grep v2)
do
  TAGV3=$(echo $TAGV2 | sed s/v2/v3/)

  if [ -n "$PREV" ]
  then
    echo git cherry-pick $PREV..$TAGV2
    if [ "$EXECUTE" = "yes" ]
    then
      git cherry-pick $PREV..$TAGV2
    fi
  fi

  echo git merge $TAGV2 --no-edit
  if [ "$EXECUTE" = "yes" ]
  then
    git merge $TAGV2 --no-edit
  fi

  echo git tag $TAGV3
  if [ "$EXECUTE" = "yes" ]
  then
    git tag $TAGV3
  fi

  PREV=$TAGV2

  read -p "continue?"
done

