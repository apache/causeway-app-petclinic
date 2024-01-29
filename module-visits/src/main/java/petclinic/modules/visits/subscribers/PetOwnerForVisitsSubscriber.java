package petclinic.modules.visits.subscribers;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.dom.pet.PetRepository;
import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.dom.petowner.PetOwner_delete;
import petclinic.modules.visits.dom.visit.Visit;
import petclinic.modules.visits.dom.visit.VisitRepository;

@Service
public class PetOwnerForVisitsSubscriber {

    @EventListener(PetOwner_delete.ActionEvent.class)
    public void on(PetOwner_delete.ActionEvent ev) {
        switch(ev.getEventPhase()) {
            case DISABLE:
                PetOwner petOwner = ev.getSubject();
                List<Pet> pets = petRepository.findByPetOwner(petOwner);
                for (Pet pet : pets) {
                    List<Visit> visits = visitRepository.findByPetOrderByVisitAtDesc(pet);
                    int numVisits = visits.size();
                    if(numVisits > 0) {
                        ev.disable(String.format("%s has %d visit%s",
                                titleService.titleOf(pet),
                                numVisits,
                                numVisits != 1 ? "s" : ""));
                    }
                }
                break;
        }
    }

    @Inject TitleService titleService;
    @Inject VisitRepository visitRepository;
    @Inject PetRepository petRepository;
}
