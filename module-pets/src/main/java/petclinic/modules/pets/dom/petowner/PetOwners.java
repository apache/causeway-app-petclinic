package petclinic.modules.pets.dom.petowner;

import java.util.List;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.BookmarkPolicy;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.query.Query;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import lombok.RequiredArgsConstructor;

import petclinic.modules.pets.types.FirstName;
import petclinic.modules.pets.types.LastName;

@Named("app.pets.PetOwners")
@DomainService(nature = NatureOfService.VIEW)
@Priority(PriorityPrecedence.EARLY)
@RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class PetOwners {

    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final PetOwnerRepository petOwnerRepository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public PetOwner create(
            @LastName final String lastName,
            @FirstName final String firstName) {
        return repositoryService.persist(PetOwner.withName(lastName, firstName));
    }


    @Action(semantics = SemanticsOf.SAFE)
    public List<PetOwner> findByLastNameLike(
            @LastName final String lastName) {
        return repositoryService.allMatches(
                Query.named(PetOwner.class, PetOwner.NAMED_QUERY__FIND_BY_LAST_NAME_LIKE)
                     .withParameter("lastName", "%" + lastName + "%"));
    }


    @Action(semantics = SemanticsOf.SAFE)
    public List<PetOwner> findByLastName(
            @LastName final String lastName
            ) {
        return petOwnerRepository.findByLastNameContaining(lastName);
    }


    @Programmatic
    public PetOwner findByLastNameExact(final String lastName) {
        return petOwnerRepository.findByLastName(lastName);
    }



    @Action(semantics = SemanticsOf.SAFE)
    public List<PetOwner> listAll() {
        return petOwnerRepository.findAll();
    }




    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(PetOwner.class)
            .mapSuccessWhenPresent(entityManager -> {
                final TypedQuery<PetOwner> q = entityManager.createQuery(
                        "SELECT p FROM PetOwner p ORDER BY p.lastName",
                        PetOwner.class)
                    .setMaxResults(1);
                return q.getResultList();
            }).valueAsNonNullElseFail();
    }


}
