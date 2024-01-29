package petclinic.modules.pets.dom.pet;

import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;

import lombok.RequiredArgsConstructor;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.dom.pet.PetRepository;
import petclinic.modules.pets.dom.petowner.PetOwner;

@Collection
@CollectionLayout(defaultView = "table")
@RequiredArgsConstructor
public class PetOwner_pets {

    private final PetOwner petOwner;

    public List<Pet> coll() {
        return petRepository.findByPetOwner(petOwner);
    }

    @Inject PetRepository petRepository;
}
