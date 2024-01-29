package petclinic.webapp.custom.restapi;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.apache.causeway.applib.services.iactnlayer.InteractionContext;
import org.apache.causeway.applib.services.iactnlayer.InteractionService;
import org.apache.causeway.applib.services.user.UserMemento;
import org.apache.causeway.applib.services.xactn.TransactionalProcessor;

import lombok.RequiredArgsConstructor;

import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.dom.petowner.PetOwners;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Inject})
class CustomController {

    private final InteractionService interactionService;
    private final TransactionalProcessor transactionalProcessor;
    private final PetOwners petOwners;

    @GetMapping("/custom/simpleObjects")
    List<PetOwner> all() {
        return call("sven", petOwners::listAll)
                .orElse(Collections.<PetOwner>emptyList());
    }

    private <T> Optional<T> call(
            final String username,
            final Callable<T> callable) {

        return interactionService.call(
                    InteractionContext.ofUserWithSystemDefaults(UserMemento.ofName(username)),
                    () -> transactionalProcessor.callWithinCurrentTransactionElseCreateNew(callable)
                )
                .ifFailureFail()
                .getValue();
    }

}
