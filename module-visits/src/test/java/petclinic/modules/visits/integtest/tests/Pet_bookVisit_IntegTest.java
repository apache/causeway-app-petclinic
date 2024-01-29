package petclinic.modules.visits.integtest.tests;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.apache.causeway.applib.services.clock.ClockService;
import org.apache.causeway.applib.services.wrapper.DisabledException;
import org.apache.causeway.applib.services.wrapper.InvalidException;
import org.apache.causeway.testing.fakedata.applib.services.FakeDataService;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.fixture.pet.Pet_persona;
import petclinic.modules.visits.contributions.pet.Pet_bookVisit;
import petclinic.modules.visits.dom.visit.Visit;
import petclinic.modules.visits.dom.visit.VisitRepository;
import petclinic.modules.visits.integtest.VisitsModuleIntegTestAbstract;

public class Pet_bookVisit_IntegTest extends VisitsModuleIntegTestAbstract {

    @BeforeEach
    void setup() {
        fixtureScripts.run(new Pet_persona.PersistAll());
    }

    @Test
    public void happy_case() {

        // given
        Pet somePet = fakeDataService.enums().anyOf(Pet_persona.class)
                        .findUsing(serviceRegistry);
        List<Visit> before = visitRepository.findByPetOrderByVisitAtDesc(somePet);

        // when
        LocalDateTime visitAt = clockService.getClock().nowAsLocalDateTime()
                                    .plusDays(fakeDataService.ints().between(1, 3));
        String reason = fakeDataService.strings().upper(40);

        wrapMixin(Pet_bookVisit.class, somePet).act(visitAt, reason);

        // then
        List<Visit> after = visitRepository.findByPetOrderByVisitAtDesc(somePet);
        after.removeAll(before);
        assertThat(after).hasSize(1);
        Visit visit = after.get(0);

        assertThat(visit.getPet()).isSameAs(somePet);
        assertThat(visit.getVisitAt()).isEqualTo(visitAt);
        assertThat(visit.getReason()).isEqualTo(reason);
    }

    @Test
    public void reason_is_required() {

        // given
        Pet somePet = fakeDataService.enums().anyOf(Pet_persona.class)
                        .findUsing(serviceRegistry);
        List<Visit> before = visitRepository.findByPetOrderByVisitAtDesc(somePet);

        // when, then
        LocalDateTime visitAt = clockService.getClock().nowAsLocalDateTime()
                                    .plusDays(fakeDataService.ints().between(1, 3));

        assertThatThrownBy(() ->
            wrapMixin(Pet_bookVisit.class, somePet).act(visitAt, null)
        )
        .isInstanceOf(InvalidException.class)
        .hasMessage("'Reason' is mandatory");
    }

    @Test
    public void cannot_book_in_the_past() {

        // given
        Pet somePet = fakeDataService.enums().anyOf(Pet_persona.class)
                .findUsing(serviceRegistry);
        List<Visit> before = visitRepository.findByPetOrderByVisitAtDesc(somePet);

        // when, then
        LocalDateTime visitAt = clockService.getClock().nowAsLocalDateTime();
        String reason = fakeDataService.strings().upper(40);

        assertThatThrownBy(() ->
                wrapMixin(Pet_bookVisit.class, somePet).act(visitAt, reason)
        )
                .isInstanceOf(InvalidException.class)
                .hasMessage("Must be in the future");
    }

    @Inject FakeDataService fakeDataService;
    @Inject VisitRepository visitRepository;
    @Inject ClockService clockService;

}
