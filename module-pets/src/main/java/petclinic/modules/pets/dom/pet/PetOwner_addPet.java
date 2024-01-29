package petclinic.modules.pets.dom.pet;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;

import lombok.RequiredArgsConstructor;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.dom.pet.PetRepository;
import petclinic.modules.pets.dom.pet.PetSpecies;
import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.types.PetName;

@Action(
        semantics = SemanticsOf.IDEMPOTENT,
        commandPublishing = Publishing.ENABLED,
        executionPublishing = Publishing.ENABLED
)
@ActionLayout(associateWith = "pets", sequence = "1")
@RequiredArgsConstructor
public class PetOwner_addPet {

    private final PetOwner petOwner;

    public PetOwner act(
            @PetName final String name,
            final PetSpecies petSpecies
            ) {
        repositoryService.persist(new Pet(petOwner, name, petSpecies));
        return petOwner;
    }
    public String validate0Act(final String name) {
        return petRepository.findByPetOwnerAndName(petOwner, name).isPresent()
                ? String.format("Pet with name '%s' already defined for this owner", name)
                : null;
    }
    public PetSpecies default1Act() {
        return PetSpecies.Dog;
    }

    @Inject PetRepository petRepository;
    @Inject RepositoryService repositoryService;
}
