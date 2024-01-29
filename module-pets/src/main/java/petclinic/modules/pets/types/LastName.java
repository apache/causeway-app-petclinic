package petclinic.modules.pets.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.causeway.applib.annotation.Parameter;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.spec.AbstractSpecification;

@Property(maxLength = LastName.MAX_LEN, mustSatisfy = LastName.Spec.class)
@Parameter(maxLength = LastName.MAX_LEN, mustSatisfy = LastName.Spec.class)
@ParameterLayout(named = "Last Name")
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface LastName {

    int MAX_LEN = 40;

    class Spec extends AbstractSpecification<String> {
        @Override public String satisfiesSafely(String candidate) {
            for (char prohibitedCharacter : "&%$!".toCharArray()) {
                if( candidate.contains(""+prohibitedCharacter)) {
                    return "Character '" + prohibitedCharacter + "' is not allowed.";
                }
            }
            return null;
        }
    }
}
