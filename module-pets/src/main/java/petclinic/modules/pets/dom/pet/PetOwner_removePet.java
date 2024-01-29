package petclinic.modules.pets.dom.pet;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;

import lombok.RequiredArgsConstructor;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.dom.pet.PetRepository;
import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.types.PetName;

@Action(
        semantics = SemanticsOf.IDEMPOTENT,
        commandPublishing = Publishing.ENABLED,
        executionPublishing = Publishing.ENABLED
)
@ActionLayout(associateWith = "pets", sequence = "2")
@RequiredArgsConstructor
public class PetOwner_removePet {

    private final PetOwner petOwner;

    public PetOwner act(@PetName final String name) {
        petRepository.findByPetOwnerAndName(petOwner, name)
                .ifPresent(pet -> repositoryService.remove(pet));
        return petOwner;
    }
    public String disableAct() {
        return petRepository.findByPetOwner(petOwner).isEmpty() ? "No pets" : null;
    }
    public List<String> choices0Act() {
        return petRepository.findByPetOwner(petOwner)
                .stream()
                .map(Pet::getName)
                .collect(Collectors.toList());
    }
    public String default0Act() {
        List<String> names = choices0Act();
        return names.size() == 1 ? names.get(0) : null;
    }

    @Inject PetRepository petRepository;
    @Inject RepositoryService repositoryService;
}
