package petclinic.webapp.application.services.homepage;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.HomePage;
import org.apache.causeway.applib.annotation.Nature;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.dom.pet.PetRepository;
import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.dom.petowner.PetOwnerRepository;
import petclinic.modules.visits.dom.visit.VisitRepository;

@Named("app.petclinic.HomePageViewModel")
@DomainObject(nature = Nature.VIEW_MODEL)
@HomePage
@DomainObjectLayout()
public class HomePageViewModel {

    public String title() {
        return getPetOwners().size() + " owners";
    }

    public List<PetOwner> getPetOwners() {
        return petOwnerRepository.findAll();
    }
    public List<Pet> getPets() {
        return petRepository.findAll();
    }
    public List<VisitPlusPetOwner> getVisits() {
        return visitRepository.findAll()
                .stream()
                .map(VisitPlusPetOwner::new)
                .collect(Collectors.toList());
    }

    @Inject PetOwnerRepository petOwnerRepository;
    @Inject PetRepository petRepository;
    @Inject VisitRepository visitRepository;
}
