package petclinic.modules.pets.dom.pet;

import java.util.Comparator;

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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.types.FirstName;
import petclinic.modules.pets.types.Notes;
import petclinic.modules.pets.types.PetName;


@Entity
@Table(
    schema="pets",
    name = "Pet",
    uniqueConstraints = {
        @UniqueConstraint(name = "Pet__owner_name__UNQ", columnNames = {"owner_id", "name"})
    }
)
@EntityListeners(CausewayEntityListener.class)
@Named("app.pets.Pet")
@DomainObject(entityChangePublishing = Publishing.ENABLED)
@DomainObjectLayout()
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Pet implements Comparable<Pet> {

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


    Pet(PetOwner petOwner, String name, PetSpecies petSpecies) {
        this.petOwner = petOwner;
        this.name = name;
        this.petSpecies = petSpecies;
    }


    public String title() {
        return getName() + " " + getPetOwner().getLastName();
    }

    public String iconName() {
        return getPetSpecies().name().toLowerCase();
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    @PropertyLayout(fieldSetId = "name", sequence = "1")
    @Getter @Setter
    private PetOwner petOwner;

    @PetName
    @Column(name = "name", length = FirstName.MAX_LEN, nullable = false)
    @Getter @Setter
    @Property(maxLength = FirstName.MAX_LEN)
    @PropertyLayout(fieldSetId = "name", sequence = "2")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "petSpecies", nullable = false)
    @Getter @Setter
    @PropertyLayout(fieldSetId = "details", sequence = "1")
    private PetSpecies petSpecies;

    @Notes
    @Column(name = "notes", length = Notes.MAX_LEN, nullable = true)
    @Getter @Setter
    @Property(commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @PropertyLayout(fieldSetId = "notes", sequence = "1")
    private String notes;


    private final static Comparator<Pet> comparator =
            Comparator.comparing(Pet::getPetOwner).thenComparing(Pet::getName);

    @Override
    public int compareTo(final Pet other) {
        return comparator.compare(this, other);
    }

}
