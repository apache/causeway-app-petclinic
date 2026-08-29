package domainapp.webapp.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import domainapp.modules.petowner.PetOwnerModule;
import domainapp.modules.visit.VisitModule;

@Configuration
@Import({
        PetOwnerModule.class,
        VisitModule.class,
})
@ComponentScan
public class ApplicationModule {

}
