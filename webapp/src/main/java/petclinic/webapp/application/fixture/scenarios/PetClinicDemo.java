package petclinic.webapp.application.fixture.scenarios;

import javax.inject.Inject;

import org.apache.causeway.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.causeway.testing.fixtures.applib.modules.ModuleWithFixturesService;

import petclinic.modules.pets.fixture.petowner.PetOwner_persona;
import petclinic.modules.pets.fixture.pet.Pet_persona;

public class PetClinicDemo extends FixtureScript {

    @Override
    protected void execute(final ExecutionContext ec) {
        ec.executeChildren(this, moduleWithFixturesService.getTeardownFixture());
        ec.executeChild(this, new Pet_persona.PersistAll());
        ec.executeChild(this, new PetOwner_persona.PersistAll());
    }

    @Inject ModuleWithFixturesService moduleWithFixturesService;

}
