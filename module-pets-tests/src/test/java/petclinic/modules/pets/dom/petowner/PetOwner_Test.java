package petclinic.modules.pets.dom.petowner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;

@ExtendWith(MockitoExtension.class)
class PetOwner_Test {

    @Mock TitleService mockTitleService;
    @Mock MessageService mockMessageService;
    @Mock RepositoryService mockRepositoryService;

    PetOwner object;

    @BeforeEach
    public void setUp() throws Exception {
        object = PetOwner.withName("Foo");
        object.titleService = mockTitleService;
        object.messageService = mockMessageService;
        object.repositoryService = mockRepositoryService;
    }

    @Nested
    public class updateName {

        @Test
        void happy_case() {
            // given
            assertThat(object.getLastName()).isEqualTo("Foo");

            // when
            object.updateName("Bar", null);

            // then
            assertThat(object.getLastName()).isEqualTo("Bar");
        }

    }
}
