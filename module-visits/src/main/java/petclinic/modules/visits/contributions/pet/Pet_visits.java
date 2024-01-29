package petclinic.modules.visits.contributions.pet;

import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;

import lombok.RequiredArgsConstructor;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.dom.pet.PetRepository;
import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.visits.dom.visit.Visit;
import petclinic.modules.visits.dom.visit.VisitRepository;

@Collection
@CollectionLayout(defaultView = "table")
@RequiredArgsConstructor
public class Pet_visits {

    private final Pet pet;

    public List<Visit> coll() {
        return visitRepository.findByPetOrderByVisitAtDesc(pet);
    }

    @Inject VisitRepository visitRepository;
}
