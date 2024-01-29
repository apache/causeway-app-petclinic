package petclinic.webapp.application.services.homepage;

import java.time.LocalDateTime;

import javax.inject.Named;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.causeway.applib.annotation.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.visits.dom.visit.Visit;

@Named("petclinic.VisitPlusPetOwner")
@DomainObject(nature=Nature.VIEW_MODEL)
@DomainObjectLayout(named = "Visit")
@XmlRootElement
@NoArgsConstructor
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class VisitPlusPetOwner {

    @Property(projecting = Projecting.PROJECTED)
    @PropertyLayout(hidden = Where.EVERYWHERE)
    @Getter
    private Visit visit;

    VisitPlusPetOwner(Visit visit) {this.visit = visit;}

    public Pet getPet() {return visit.getPet();}
    public String getReason() {return visit.getReason();}
    public LocalDateTime getVisitAt() {return visit.getVisitAt();}

    public PetOwner getPetOwner() {
        return getPet().getPetOwner();
    }
}
