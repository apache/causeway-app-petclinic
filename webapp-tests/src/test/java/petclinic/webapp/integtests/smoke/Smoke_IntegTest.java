package petclinic.webapp.integtests.smoke;

import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.services.wrapper.InvalidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import org.apache.causeway.applib.services.xactn.TransactionService;

import static org.assertj.core.api.Assertions.assertThat;

import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.dom.petowner.PetOwner_delete;
import petclinic.webapp.integtests.WebAppIntegTestAbstract;
import petclinic.modules.pets.dom.petowner.PetOwners;

@Transactional
class Smoke_IntegTest extends WebAppIntegTestAbstract {

    @Inject PetOwners menu;
    @Inject TransactionService transactionService;

    @Test
    void happy_case() {

        // when
        List<PetOwner> all = wrap(menu).listAll();

        // then
        assertThat(all).isEmpty();


        // when
        final PetOwner fred = wrap(menu).create("Fred", null);
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(1);
        assertThat(all).contains(fred);


        // when
        final PetOwner bill = wrap(menu).create("Bill", null);
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(2);
        assertThat(all).contains(fred, bill);


        // when
        wrap(fred).updateName("Freddy", null);
        transactionService.flushTransaction();

        // then
        assertThat(wrap(fred).getName()).isEqualTo("Freddy");


        // when
        wrap(fred).setNotes("These are some notes");
        transactionService.flushTransaction();

        // then
        assertThat(wrap(fred).getNotes()).isEqualTo("These are some notes");


        // when
        Assertions.assertThrows(InvalidException.class, () -> {
            wrap(fred).updateName("New name !!!", null);
            transactionService.flushTransaction();
        }, "Exclamation mark is not allowed");

        // then
        assertThat(wrap(fred).getNotes()).isEqualTo("These are some notes");


        // when
        wrapMixin(PetOwner_delete.class, fred).act();
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(1);
    }

}

