package domainapp.webapp.application.services.health;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.springframework.stereotype.Service;

import org.apache.causeway.applib.services.health.Health;
import org.apache.causeway.applib.services.health.HealthCheckService;

import domainapp.modules.petowner.dom.petowner.PetOwners;

@Service
@Named("domainapp.HealthCheckServiceImpl")
public class HealthCheckServiceImpl implements HealthCheckService {

    private final PetOwners petOwners;

    @Inject
    public HealthCheckServiceImpl(PetOwners petOwners) {
        this.petOwners = petOwners;
    }

    @Override
    public Health check() {
        try {
            petOwners.ping();
            return Health.ok();
        } catch (Exception ex) {
            return Health.error(ex);
        }
    }
}
