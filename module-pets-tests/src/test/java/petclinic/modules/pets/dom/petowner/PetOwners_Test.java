package petclinic.modules.pets.dom.petowner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

@ExtendWith(MockitoExtension.class)
class PetOwners_Test {

    @Mock RepositoryService mockRepositoryService;
    @Mock JpaSupportService mockJpaSupportService;
    @Mock PetOwnerRepository mockPetOwnerRepository;

    PetOwners objects;

    @BeforeEach
    public void setUp() {
        objects = new PetOwners(mockRepositoryService, mockJpaSupportService, mockPetOwnerRepository);
    }

    @Nested
    class create {

        @Test
        void happyCase() {

            // given
            final String someName = "Foobar";

            // expect
            when(mockRepositoryService.persist(
                    argThat((ArgumentMatcher<PetOwner>) simpleObject -> Objects.equals(simpleObject.getLastName(), someName)))
            ).then((Answer<PetOwner>) invocation -> invocation.getArgument(0));

            // when
            final PetOwner obj = objects.create(someName, null);

            // then
            assertThat(obj).isNotNull();
            assertThat(obj.getLastName()).isEqualTo(someName);
        }
    }

    @Nested
    class ListAll {

        @Test
        void happyCase() {

            // given
            final List<PetOwner> all = new ArrayList<>();

            // expecting
            when(mockPetOwnerRepository.findAll())
                .thenReturn(all);

            // when
            final List<PetOwner> list = objects.listAll();

            // then
            assertThat(list).isEqualTo(all);
        }
    }
}
