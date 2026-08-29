package domainapp.webapp.integtests.smoke;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import org.apache.causeway.applib.services.xactn.TransactionService;

import domainapp.modules.petowner.PetOwnerModule;
import domainapp.modules.petowner.dom.petowner.PetOwner;
import domainapp.modules.petowner.dom.petowner.PetOwners;
import domainapp.webapp.integtests.WebAppIntegTestAbstract;

@DirtiesContext
@Transactional
class Smoke_IntegTest extends WebAppIntegTestAbstract {

    @Inject @Named(PetOwnerModule.NAMESPACE + ".PetOwners") PetOwners menu;
    @Inject TransactionService transactionService;

    @Test
    void happy_case() {

        // when
        List<PetOwner> all = wrap(menu).listAll();

        // then
        assertThat(all).isEmpty();


        // when
        final PetOwner fred = wrap(menu).create("Fred", null, "01234 567890", null);
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(1);
        assertThat(all).contains(fred);


        // when
        final PetOwner bill = wrap(menu).create("Bill", null, "01234 567891", null);
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(2);
        assertThat(all).contains(fred, bill);
    }

}
