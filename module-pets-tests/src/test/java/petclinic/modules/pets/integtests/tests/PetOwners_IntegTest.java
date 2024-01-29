package petclinic.modules.pets.integtests.tests;

import java.util.List;

import javax.inject.Inject;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.causeway.testing.unittestsupport.applib.matchers.ThrowableMatchers;

import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.dom.petowner.PetOwners;
import petclinic.modules.pets.fixture.petowner.PetOwner_persona;
import petclinic.modules.pets.integtests.PetsModuleIntegTestAbstract;

@Transactional
public class PetOwners_IntegTest extends PetsModuleIntegTestAbstract {

    @Inject
    PetOwners menu;

    @Nested
    public static class listAll extends PetOwners_IntegTest {

        @Test
        public void happyCase() {

            // given
            fixtureScripts.run(new PetOwner_persona.PersistAll());
            transactionService.flushTransaction();

            // when
            final List<PetOwner> all = wrap(menu).listAll();

            // then
            assertThat(all).hasSize(PetOwner_persona.values().length);
        }

        @Test
        public void whenNone() {

            // when
            final List<PetOwner> all = wrap(menu).listAll();

            // then
            assertThat(all).hasSize(0);
        }
    }

    @Nested
    public static class create extends PetOwners_IntegTest {

        @Test
        public void happyCase() {

            wrap(menu).create(PetOwner_persona.FARRELL.name(), null);

            // then
            final List<PetOwner> all = wrap(menu).listAll();
            assertThat(all).hasSize(1);
        }

        @Test
        public void whenAlreadyExists() {

            // given
            fixtureScripts.runPersona(PetOwner_persona.MAY);
            transactionService.flushTransaction();

            // expect
            Throwable cause = assertThrows(Throwable.class, ()->{

                // when
                wrap(menu).create(PetOwner_persona.MAY.getName(), null);
                transactionService.flushTransaction();

            });

            // also expect
            MatcherAssert.assertThat(cause,
                    ThrowableMatchers.causedBy(DuplicateKeyException.class));

        }

    }


}
