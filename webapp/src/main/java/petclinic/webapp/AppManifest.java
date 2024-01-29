package petclinic.webapp;

import org.apache.causeway.viewer.graphql.viewer.CausewayModuleViewerGraphqlViewer;
import org.apache.causeway.viewer.restfulobjects.viewer.CausewayModuleViewerRestfulObjectsViewer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import org.apache.causeway.core.config.presets.CausewayPresets;
import org.apache.causeway.core.runtimeservices.CausewayModuleCoreRuntimeServices;
import org.apache.causeway.extensions.flyway.impl.CausewayModuleExtFlywayImpl;
import org.apache.causeway.persistence.jpa.eclipselink.CausewayModulePersistenceJpaEclipselink;
import org.apache.causeway.security.shiro.CausewayModuleSecurityShiro;
import org.apache.causeway.testing.fixtures.applib.CausewayModuleTestingFixturesApplib;
import org.apache.causeway.testing.h2console.ui.CausewayModuleTestingH2ConsoleUi;
import org.apache.causeway.viewer.wicket.viewer.CausewayModuleViewerWicketViewer;

import petclinic.webapp.application.ApplicationModule;
import petclinic.webapp.application.fixture.scenarios.PetClinicDemo;
import petclinic.webapp.custom.CustomModule;
import petclinic.webapp.quartz.QuartzModule;

@Configuration
@Import({
        CausewayModuleCoreRuntimeServices.class,
        CausewayModuleSecurityShiro.class,
        CausewayModulePersistenceJpaEclipselink.class,
        CausewayModuleViewerGraphqlViewer.class,
        CausewayModuleViewerRestfulObjectsViewer.class,
        CausewayModuleViewerWicketViewer.class,

        CausewayModuleTestingFixturesApplib.class,
        CausewayModuleTestingH2ConsoleUi.class,

        CausewayModuleExtFlywayImpl.class,

        ApplicationModule.class,
        CustomModule.class,
        QuartzModule.class,

        // discoverable fixtures
        PetClinicDemo.class
})
@PropertySources({
        @PropertySource(CausewayPresets.DebugDiscovery),
})
public class AppManifest {
}
