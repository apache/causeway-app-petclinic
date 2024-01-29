package petclinic.modules.pets.integtests.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.causeway.applib.services.wrapper.HiddenException;
import org.apache.causeway.applib.services.wrapper.InvalidException;

import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.fixture.petowner.PetOwner_persona;
import petclinic.modules.pets.integtests.PetsModuleIntegTestAbstract;

@Transactional
public class PetOwner_IntegTest extends PetsModuleIntegTestAbstract {

    PetOwner petOwner;

    @BeforeEach
    public void setUp() {
        // given
        petOwner = fixtureScripts.runPersona(PetOwner_persona.JONES);
    }


    @Nested
    public static class name extends PetOwner_IntegTest {

        @Test
        public void accessible() {
            // when
            final String name = wrap(petOwner).getName();

            // then
            assertThat(name).isEqualTo(petOwner.getLastName());
        }
    }

    @Nested
    public static class lastName extends PetOwner_IntegTest {

        @Test
        public void not_accessible() {
            // expect
            assertThrows(HiddenException.class, ()->{

                // when
                wrap(petOwner).getLastName();
            });
        }
    }

    @Nested
    public static class firstName extends PetOwner_IntegTest {

        @Test
        public void not_accessible() {
            // expect
            assertThrows(HiddenException.class, ()->{

                // when
                wrap(petOwner).getFirstName();
            });
        }
    }

    @Nested
    public static class updateName extends PetOwner_IntegTest {


        @Test
        public void can_be_updated_directly() {

            // when
            wrap(petOwner).updateName("McAdam", "Adam");
            transactionService.flushTransaction();

            // then
            assertThat(petOwner.getLastName()).isEqualTo("McAdam");
            assertThat(petOwner.getFirstName()).isEqualTo("Adam");
        }

        @Test
        public void fails_validation() {

            // expect
            InvalidException cause = assertThrows(InvalidException.class, ()->{

                // when
                wrap(petOwner).updateName("new name!", null);
            });

            // then
            assertThat(cause.getMessage()).contains("Character '!' is not allowed");
        }
    }

}
