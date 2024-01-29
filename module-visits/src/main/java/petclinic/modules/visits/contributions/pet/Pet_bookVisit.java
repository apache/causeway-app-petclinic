package petclinic.modules.visits.contributions.pet;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.clock.ClockService;
import org.apache.causeway.applib.services.repository.RepositoryService;

import lombok.RequiredArgsConstructor;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.dom.pet.PetRepository;
import petclinic.modules.pets.dom.pet.PetSpecies;
import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.types.PetName;
import petclinic.modules.visits.dom.visit.Visit;
import petclinic.modules.visits.types.Reason;

@Action(
        semantics = SemanticsOf.IDEMPOTENT,
        commandPublishing = Publishing.ENABLED,
        executionPublishing = Publishing.ENABLED
)
@ActionLayout(associateWith = "visits", sequence = "1")
@RequiredArgsConstructor
public class Pet_bookVisit {

    private final Pet pet;

    public Visit act(
            LocalDateTime visitAt,
            @Reason final String reason
            ) {
        return repositoryService.persist(new Visit(pet, visitAt, reason));
    }
    public String validate0Act(LocalDateTime visitAt) {
        return clockService.getClock().nowAsLocalDateTime().isBefore(visitAt)
                ? null
                : "Must be in the future";
    }
    public LocalDateTime default0Act() {
        return clockService.getClock().nowAsLocalDateTime()
                .toLocalDate()
                .plusDays(1)
                .atTime(LocalTime.of(9, 0));
    }

    @Inject ClockService clockService;
    @Inject RepositoryService repositoryService;
}
