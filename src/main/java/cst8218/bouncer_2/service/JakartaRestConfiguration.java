package cst8218.bouncer_2.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;

import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.PasswordHash;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configures Jakarta RESTful Web Services for the application.
 * @author Juneau
 */
@FormAuthenticationMechanismDefinition(
loginToContinue = @LoginToContinue(
loginPage = "/index.xhtml",
errorPage = "/index.xhtml"))
@DatabaseIdentityStoreDefinition(
dataSourceLookup = "${'java:comp/DefaultDataSource'}",
callerQuery = "#{'select password from app.appuser where userid = ?'}",
groupsQuery = "select groupname from app.appuser where userid = ?",
hashAlgorithm = PasswordHash.class,
priority = 10
)
@Named
@ApplicationScoped
@ApplicationPath("resources")
public class JakartaRestConfiguration extends Application {
    
}
