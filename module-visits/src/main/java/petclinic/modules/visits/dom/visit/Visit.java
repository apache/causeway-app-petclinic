package petclinic.modules.visits.dom.visit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.title.TitleService;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.dom.pet.PetSpecies;
import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.types.FirstName;
import petclinic.modules.pets.types.Notes;
import petclinic.modules.pets.types.PetName;
import petclinic.modules.visits.types.Reason;


@Entity
@Table(
    schema="visits",
    name = "Visit",
    uniqueConstraints = {
        @UniqueConstraint(name = "Visit__pet_visitAt__UNQ", columnNames = {"owner_id", "name"})
    }
)
@EntityListeners(CausewayEntityListener.class)
@Named("visits.Visit")
@DomainObject(entityChangePublishing = Publishing.ENABLED)
@DomainObjectLayout()
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Visit implements Comparable<Visit> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Getter @Setter
    @PropertyLayout(fieldSetId = "metadata", sequence = "1")
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;


    public Visit(Pet pet, LocalDateTime visitAt, String reason) {
        this.pet = pet;
        this.visitAt = visitAt;
        this.reason = reason;
    }


    public String title() {
        return titleService.titleOf(getPet()) + " @ " + getVisitAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id")
    @PropertyLayout(fieldSetId = "name", sequence = "1")
    @Getter @Setter
    private Pet pet;

    @Column(name = "visitAt", nullable = false)
    @Getter @Setter
    @PropertyLayout(fieldSetId = "name", sequence = "2")
    private LocalDateTime visitAt;

    @Reason
    @Column(name = "reason", length = FirstName.MAX_LEN, nullable = false)
    @Getter @Setter
    @PropertyLayout(fieldSetId = "details", sequence = "1")
    private String reason;

    private final static Comparator<Visit> comparator =
            Comparator.comparing(Visit::getPet).thenComparing(Visit::getVisitAt);

    @Override
    public int compareTo(final Visit other) {
        return comparator.compare(this, other);
    }

    @Inject @Transient TitleService titleService;
}
