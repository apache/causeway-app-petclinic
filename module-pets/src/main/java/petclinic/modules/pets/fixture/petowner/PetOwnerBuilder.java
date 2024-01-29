package petclinic.modules.pets.fixture.petowner;

import javax.inject.Inject;

import org.apache.causeway.testing.fixtures.applib.personas.BuilderScriptWithResult;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.dom.petowner.PetOwners;

@Accessors(chain = true)
public class PetOwnerBuilder extends BuilderScriptWithResult<PetOwner> {

    @Getter @Setter
    private String name;

    @Override
    protected PetOwner buildResult(final ExecutionContext ec) {

        checkParam("name", ec, String.class);

        PetOwner petOwner = petOwners.findByLastNameExact(name);
        if(petOwner == null) {
            petOwner = wrap(petOwners).create(name, null);
        }
        return this.object = petOwner;
    }

    @Inject PetOwners petOwners;
}
