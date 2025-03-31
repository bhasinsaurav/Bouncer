package cst8218.bouncer_2.service;

import jakarta.enterprise.context.ApplicationScoped; // Marks the class as a CDI (Contexts and Dependency Injection) bean with application scope
import jakarta.inject.Named; // Marks the class as a named CDI bean, so it can be referenced by its name
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition; // Used to define basic authentication
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition; // Used to define form-based authentication
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue; // Defines the login and error pages for authentication
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition; // Defines the database identity store for authentication
import jakarta.security.enterprise.identitystore.PasswordHash; // Specifies the password hash algorithm to be used
import jakarta.ws.rs.ApplicationPath; // Defines the base URI for the RESTful services
import jakarta.ws.rs.core.Application; // Base class for JAX-RS (Jakarta RESTful Web Services) applications

/**
 * Configures Jakarta RESTful Web Services for the application.
 * This class configures authentication mechanisms and identity stores.
 * @author Saurav and Deep
 */
@FormAuthenticationMechanismDefinition(
    loginToContinue = @LoginToContinue( // Specifies the login and error pages for form-based authentication
        loginPage = "/index.xhtml", // Redirects to this page if the user is not authenticated
        errorPage = "/index.xhtml"  // Redirects to this page in case of authentication failure
    )
)
@DatabaseIdentityStoreDefinition(
    dataSourceLookup = "${'java:comp/DefaultDataSource'}", // Specifies the JNDI data source for connecting to the database
    callerQuery = "#{'select password from app.appuser where userid = ?'}", // SQL query to fetch the password for a user based on the provided userid
    groupsQuery = "select groupname from app.appuser where userid = ?", // SQL query to fetch the user's group(s) based on the provided userid
    hashAlgorithm = PasswordHash.class, // Specifies the class responsible for hashing the password
    priority = 10 // Defines the priority of this identity store (higher numbers have higher priority)
)
@Named // Makes the class available as a CDI bean, with a name derived from the class name
@ApplicationScoped // Ensures that the bean has a singleton lifespan for the entire application
@ApplicationPath("resources") // Sets the base path for REST resources (all resources are under "/resources")
public class JakartaRestConfiguration extends Application {
    // This class configures Jakarta RESTful Web Services and authentication for the application
}
