package petclinic.modules.pets;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.apache.causeway.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.causeway.testing.fixtures.applib.modules.ModuleWithFixtures;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.dom.petowner.PetOwner;

@Configuration
@ComponentScan
@EnableJpaRepositories
@EntityScan(basePackageClasses = {PetsModule.class})
public class PetsModule implements ModuleWithFixtures {

    @Override
    public FixtureScript getTeardownFixture() {
        return new FixtureScript() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                repositoryService.removeAll(Pet.class);
                repositoryService.removeAll(PetOwner.class);
            }
        };
    }
}
