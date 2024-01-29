package petclinic.modules.pets.dom.pet;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import org.apache.causeway.applib.services.repository.RepositoryService;

import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.dom.petowner.PetOwner_delete;

@Service
public class PetOwnerForPetsSubscriber {

    @EventListener(PetOwner_delete.ActionEvent.class)
    public void on(PetOwner_delete.ActionEvent ev) {
        switch(ev.getEventPhase()) {
            case EXECUTING:
                PetOwner petOwner = ev.getSubject();
                List<Pet> pets = petRepository.findByPetOwner(petOwner);
                pets.forEach(repositoryService::remove);
                break;
        }
    }

    @Inject PetRepository petRepository;
    @Inject RepositoryService repositoryService;
}
