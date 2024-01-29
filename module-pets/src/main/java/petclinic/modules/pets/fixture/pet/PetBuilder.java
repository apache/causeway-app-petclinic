package petclinic.modules.pets.fixture.pet;

import javax.inject.Inject;

import org.apache.causeway.testing.fixtures.applib.personas.BuilderScriptWithResult;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.dom.pet.PetOwner_addPet;
import petclinic.modules.pets.dom.pet.PetRepository;
import petclinic.modules.pets.dom.pet.PetSpecies;
import petclinic.modules.pets.fixture.petowner.PetOwner_persona;

@Accessors(chain = true)
public class PetBuilder extends BuilderScriptWithResult<Pet> {

    @Getter @Setter String name;
    @Getter @Setter PetSpecies petSpecies;
    @Getter @Setter PetOwner_persona petOwner_persona;

    @Override
    protected Pet buildResult(final ExecutionContext ec) {

        checkParam("name", ec, String.class);
        checkParam("petSpecies", ec, PetSpecies.class);
        checkParam("petOwner_persona", ec, PetOwner_persona.class);

        PetOwner petOwner = ec.executeChildT(this, petOwner_persona.builder()).getObject();

        Pet pet = petRepository.findByPetOwnerAndName(petOwner, name).orElse(null);
        if(pet == null) {
            wrapMixin(PetOwner_addPet.class, petOwner).act(name, petSpecies);
            pet = petRepository.findByPetOwnerAndName(petOwner, name).orElseThrow();
        }

        return this.object = pet;
    }

    @Inject PetRepository petRepository;
}
