package petclinic.modules.visits;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.apache.causeway.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.causeway.testing.fixtures.applib.modules.ModuleWithFixtures;

import petclinic.modules.pets.PetsModule;
import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.visits.dom.visit.Visit;

@Configuration
@ComponentScan
@Import(PetsModule.class)
@EnableJpaRepositories
@EntityScan(basePackageClasses = {VisitsModule.class})
public class VisitsModule implements ModuleWithFixtures {

    @Override
    public FixtureScript getTeardownFixture() {
        return new FixtureScript() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                repositoryService.removeAll(Visit.class);
            }
        };
    }
}
