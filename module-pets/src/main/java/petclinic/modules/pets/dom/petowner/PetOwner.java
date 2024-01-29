package petclinic.modules.pets.dom.petowner;

import java.util.Comparator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import static org.apache.causeway.applib.annotation.SemanticsOf.IDEMPOTENT;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

import petclinic.modules.pets.types.EmailAddress;
import petclinic.modules.pets.types.FirstName;
import petclinic.modules.pets.types.LastName;
import petclinic.modules.pets.types.Notes;
import petclinic.modules.pets.types.PhoneNumber;


@Entity
@Table(
    schema="pets",
    name = "PetOwner",
    uniqueConstraints = {
        @UniqueConstraint(name = "PetOwner__lastName__UNQ", columnNames = {"lastName"})
    }
)
@NamedQueries({
        @NamedQuery(
                name = PetOwner.NAMED_QUERY__FIND_BY_LAST_NAME_LIKE,
                query = "SELECT so " +
                        "FROM PetOwner so " +
                        "WHERE so.lastName LIKE :lastName"
        )
})
@EntityListeners(CausewayEntityListener.class)
@Named("pets.PetOwner")
@DomainObject(entityChangePublishing = Publishing.ENABLED)
@DomainObjectLayout()
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class PetOwner implements Comparable<PetOwner> {

    static final String NAMED_QUERY__FIND_BY_LAST_NAME_LIKE = "PetOwner.findByLastNameLike";

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

    public static PetOwner withName(String name) {
        return withName(name, null);
    }

    public static PetOwner withName(String lastName, String firstName) {
        val simpleObject = new PetOwner();
        simpleObject.setLastName(lastName);
        simpleObject.setFirstName(firstName);
        return simpleObject;
    }

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;


    public String title() {
        return getLastName() + (getFirstName() != null ? ", " + getFirstName() : "");
    }

    @Transient
    @PropertyLayout(fieldSetId = "name", sequence = "1")
    public String getName() {
        return (getFirstName() != null ? getFirstName() + " ": "")  + getLastName();
    }

    @LastName
    @Column(name = "lastName", length = LastName.MAX_LEN, nullable = false)
    @Getter @Setter @ToString.Include
    @Property()
    @PropertyLayout(hidden = Where.EVERYWHERE)
    private String lastName;

    @FirstName
    @Column(name = "firstName", length = FirstName.MAX_LEN, nullable = true)
    @Getter @Setter @ToString.Include
    @Property()
    @PropertyLayout(hidden = Where.EVERYWHERE)
    private String firstName;

    @PhoneNumber
    @Column(name = "phoneNumber", length = PhoneNumber.MAX_LEN, nullable = true)
    @PropertyLayout(fieldSetId = "contactDetails", sequence = "1")
    @Getter @Setter
    private String phoneNumber;

    @EmailAddress
    @Column(name = "emailAddress", length = EmailAddress.MAX_LEN, nullable = true)
    @PropertyLayout(fieldSetId = "contactDetails", sequence = "2")
    @Getter @Setter
    private String emailAddress;

    @Notes
    @Column(name = "notes", length = Notes.MAX_LEN, nullable = true)
    @Getter @Setter
    @Property(commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @PropertyLayout(fieldSetId = "notes", sequence = "1")
    private String notes;


    @Action(semantics = IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "name")
    public PetOwner updateName(
            @LastName final String lastName,
            @FirstName final String firstName) {
        setLastName(lastName);
        setFirstName(firstName);
        return this;
    }
    public String default0UpdateName() {
        return getLastName();
    }
    public String default1UpdateName() {
        return getFirstName();
    }



    private final static Comparator<PetOwner> comparator =
            Comparator.comparing(PetOwner::getLastName);

    @Override
    public int compareTo(final PetOwner other) {
        return comparator.compare(this, other);
    }

}
