package petclinic.modules.visits.contributions.pet;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.apache.causeway.applib.clock.VirtualClock;
import org.apache.causeway.applib.services.clock.ClockService;

@ExtendWith(MockitoExtension.class)
class Pet_bookVisit_Test {

    @Mock ClockService mockClockService;
    @Mock VirtualClock mockVirtualClock;

    @BeforeEach
    void setup() {
        Mockito.when(mockClockService.getClock()).thenReturn(mockVirtualClock);
    }

    @Nested
    class default0 {

        @Test
        void defaults_to_9am_tomorrow_morning() {

            // given
            Pet_bookVisit mixin = new Pet_bookVisit(null);
            mixin.clockService = mockClockService;

            LocalDateTime now = LocalDateTime.of(2021, 10, 21, 16, 37, 45);

            // expecting
            Mockito.when(mockVirtualClock.nowAsLocalDateTime()).thenReturn(now);

            // when
            LocalDateTime localDateTime = mixin.default0Act();

            // then
            Assertions.assertThat(localDateTime)
                    .isEqualTo(LocalDateTime.of(2021,10,22,9,0,0));
        }
    }
}
