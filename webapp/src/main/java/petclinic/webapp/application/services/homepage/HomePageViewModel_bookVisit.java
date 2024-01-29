package petclinic.webapp.application.services.homepage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.services.factory.FactoryService;
import org.apache.causeway.applib.services.wrapper.WrapperFactory;

import lombok.RequiredArgsConstructor;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.dom.pet.PetRepository;
import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.dom.petowner.PetOwnerRepository;
import petclinic.modules.visits.contributions.pet.Pet_bookVisit;
import petclinic.modules.visits.dom.visit.Visit;

@Action
@RequiredArgsConstructor
public class HomePageViewModel_bookVisit {

    final HomePageViewModel homePageViewModel;

    public Object act(PetOwner petOwner, Pet pet, LocalDateTime visitAt, String reason, boolean showVisit) {
        Visit visit = wrapperFactory.wrapMixin(Pet_bookVisit.class, pet).act(visitAt, reason);
        return showVisit ? visit : homePageViewModel;
    }
    public List<PetOwner> autoComplete0Act(final String lastName) {
        return petOwnerRepository.findByLastNameContaining(lastName);
    }
    public List<Pet> choices1Act(PetOwner petOwner) {
        if(petOwner == null) return Collections.emptyList();
        return petRepository.findByPetOwner(petOwner);
    }
    public LocalDateTime default2Act(PetOwner petOwner, Pet pet) {
        if(pet == null) return null;
        return factoryService.mixin(Pet_bookVisit.class, pet).default0Act();
    }
    public String validate2Act(PetOwner petOwner, Pet pet, LocalDateTime visitAt){
         return factoryService.mixin(Pet_bookVisit.class, pet).validate0Act(visitAt);
    }

    @Inject PetRepository petRepository;
    @Inject PetOwnerRepository petOwnerRepository;
    @Inject WrapperFactory wrapperFactory;
    @Inject FactoryService factoryService;
}
