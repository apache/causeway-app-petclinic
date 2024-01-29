package petclinic.modules.pets.fixture.pet;

import org.apache.causeway.applib.services.registry.ServiceRegistry;
import org.apache.causeway.testing.fixtures.applib.personas.Persona;
import org.apache.causeway.testing.fixtures.applib.personas.PersonaWithBuilderScript;
import org.apache.causeway.testing.fixtures.applib.personas.PersonaWithFinder;
import org.apache.causeway.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import lombok.AllArgsConstructor;
import lombok.Getter;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.dom.pet.PetRepository;
import petclinic.modules.pets.dom.pet.PetSpecies;
import petclinic.modules.pets.fixture.petowner.PetOwner_persona;

@AllArgsConstructor
public enum Pet_persona
implements Persona<Pet, PetBuilder> {

    TIDDLES_JONES("Tiddles", PetSpecies.Cat, PetOwner_persona.JONES),
    ROVER_JONES("Rover", PetSpecies.Dog, PetOwner_persona.JONES),
    HARRY_JONES("Harry", PetSpecies.Hamster, PetOwner_persona.JONES),
    BURT_JONES("Burt", PetSpecies.Budgerigar, PetOwner_persona.JONES),
    TIDDLES_FARRELL("Tiddles", PetSpecies.Cat, PetOwner_persona.FARRELL),
    SPIKE_FORD("Spike", PetSpecies.Dog, PetOwner_persona.FORD),
    BARRY_ITOJE("Barry", PetSpecies.Budgerigar, PetOwner_persona.ITOJE);

    @Getter private final String name;
    @Getter private final PetSpecies petSpecies;
    @Getter private final PetOwner_persona petOwner_persona;

    @Override
    public PetBuilder builder() {
        return new PetBuilder()
                        .setName(name)
                        .setPetSpecies(petSpecies)
                        .setPetOwner_persona(petOwner_persona);
    }

    @Override
    public Pet findUsing(final ServiceRegistry serviceRegistry) {
        PetOwner petOwner = petOwner_persona.findUsing(serviceRegistry);
        PetRepository petRepository = serviceRegistry.lookupService(PetRepository.class).orElseThrow();
        return petRepository.findByPetOwnerAndName(petOwner, name).orElse(null);
    }

    public static class PersistAll
    extends PersonaEnumPersistAll<Pet, Pet_persona, PetBuilder> {
        public PersistAll() {
            super(Pet_persona.class);
        }
    }
}
