/*
 * RESTful Web Service for managing Bouncer entities.
 * Provides CRUD operations using Jakarta RESTful Web Services (JAX-RS).
 */
package cst8218.bouncer_2.service;

import cst8218.bouncer_2.ejb.AbstractFacade;
import cst8218.bouncer_2.entity.Bouncer;
import jakarta.annotation.security.RolesAllowed; // Enforces role-based access control
import jakarta.ejb.Stateless; // Marks the class as a stateless EJB (Enterprise Java Bean)
import jakarta.persistence.EntityManager; // Manages JPA entities and their persistence
import jakarta.persistence.PersistenceContext; // Marks the EntityManager for DI
import jakarta.ws.rs.Consumes; // Specifies the types of request payload the method can handle
import jakarta.ws.rs.DELETE; // HTTP DELETE method annotation
import jakarta.ws.rs.GET; // HTTP GET method annotation
import jakarta.ws.rs.POST; // HTTP POST method annotation
import jakarta.ws.rs.PUT; // HTTP PUT method annotation
import jakarta.ws.rs.Path; // URL path for the REST endpoint
import jakarta.ws.rs.PathParam; // Binds a method parameter to a path parameter
import jakarta.ws.rs.Produces; // Specifies the types of response the method can produce
import jakarta.ws.rs.core.MediaType; // Defines media types (e.g., JSON, XML)
import jakarta.ws.rs.core.Response; // Represents the HTTP response
import java.util.List; // List to hold the collection of Bouncer entities

/**
 * Stateless RESTful service that manages Bouncer entities.
 */

@RolesAllowed({"Admin", "RestUser"}) // Restricts access to users with the "Admin" or "RestUser" role
@Stateless // Indicates that this class is a stateless session bean
@Path("cst8218.bouncer_2.entity.Bouncer") // URL path for accessing Bouncer resources
public class BouncerFacadeREST extends AbstractFacade<Bouncer> {

    @PersistenceContext(unitName = "my_persistence_unit") // Injects the EntityManager for persistence operations
    private EntityManager em;

    // Constructor: Calls the parent AbstractFacade with Bouncer class type
    public BouncerFacadeREST() {
        super(Bouncer.class); // Passes the Bouncer class to the parent for CRUD operations
    }

    /**
     * Creates a new Bouncer or updates an existing one if the ID is provided.
     * - If the ID is null, a new Bouncer is created.
     * - If the ID exists, it updates the existing Bouncer with non-null values.
     * - If the ID is provided but does not exist, returns a bad request response.
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) // Accepts XML and JSON input
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) // Produces XML and JSON output
    public Response creatRecord(Bouncer entity) {

        // Case 1: Updating an existing Bouncer
        if (entity.getId() != null && super.find(entity.getId()) != null) {
            Bouncer oldBouncer = super.find(entity.getId());
            oldBouncer.updateOldBouncer(entity); // Merging new values
            super.edit(oldBouncer); // Updates the existing Bouncer entity
            return Response.status(Response.Status.OK) // 200 OK for update
                           .entity(super.find(entity.getId()))
                           .build();
        }

        // Case 2: Creating a new Bouncer
        if (entity.getId() == null) {
            super.create(entity); // Creates the new Bouncer entity
            return Response.status(Response.Status.CREATED) // 201 Created
                           .entity(entity)
                           .build();
        }

        // Case 3: ID is given but does not exist in the database
        if (entity.getId() != null && super.find(entity.getId()) == null) {
            return Response.status(Response.Status.BAD_REQUEST) // 400 Bad Request
                           .entity("Bad Request. Cannot find id")
                           .build();
        }

        // Default case: Unexpected scenario (shouldn't reach here)
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(entity)
                       .build();
    }

    /**
     * Creates a Bouncer by ID. (This method seems redundant and incorrect.)
     * - If the ID does not match the found Bouncer's ID, returns a bad request.
     * - If the ID matches, it removes the old entity and creates a new one.
     */
    @POST
    @Path("{id}") // Path with the ID parameter
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) // Accepts XML and JSON input
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) // Produces XML and JSON output
    public Response createBouncer(@PathParam("id") Long id, Bouncer entity) {
        // Ensures the provided ID exists in the database
        if (super.find(id) == null) {
            return Response.status(Response.Status.BAD_REQUEST) // 400 Bad Request
                           .entity("Bad Request")
                           .build();
        }

        // Ensures the entity's ID matches the path ID
        if (!id.equals(entity.getId())) {
            return Response.status(Response.Status.BAD_REQUEST) // 400 Bad Request
                           .entity("Bad Request")
                           .build();
        }

        // Removes the old entity and creates a new one
        if (id == super.find(id).getId()) {
            super.edit(super.find(id)); // Updates the existing entity
            return Response.status(Response.Status.CREATED) // 201 Created
                           .entity(entity)
                           .build();
        }
        return Response.status(Response.Status.CREATED) // 201 Created
                       .entity(entity)
                       .build();
    }

    /**
     * Updates an existing Bouncer.
     * - Ensures the ID exists in the database.
     * - Ensures the ID in the entity matches the path ID.
     * - Updates the existing Bouncer with new non-null values.
     */
    @PUT
    @Path("{id}") // Path with the ID parameter
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) // Accepts XML and JSON input
    public Response edit(@PathParam("id") Long id, Bouncer entity) {
        // Case 1: ID not found in the database
        if (super.find(id) == null) {
            return Response.status(Response.Status.BAD_REQUEST) // 400 Bad Request
                           .entity("Bad Request")
                           .build();
        }

        // Case 2: Entity ID does not match the path ID
        if (!id.equals(entity.getId())) {
            return Response.status(Response.Status.BAD_REQUEST) // 400 Bad Request
                           .entity("Bad Request")
                           .build();
        }

        // Case 3: Update the existing Bouncer
        Bouncer oldBouncer = super.find(entity.getId());
        oldBouncer.updateOldBouncer(entity); // Merging values
        super.edit(oldBouncer); // Update the entity
        return Response.status(Response.Status.OK) // 200 OK for update
                       .entity(super.find(entity.getId()))
                       .build();
    }

    /**
     * Deletes a Bouncer by ID.
     */
    @DELETE
    @Path("{id}") // Path with the ID parameter
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id)); // Removes the entity from the database
    }

    /**
     * Retrieves a single Bouncer by ID.
     */
    @GET
    @Path("{id}") // Path with the ID parameter
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) // Produces XML and JSON output
    public Bouncer find(@PathParam("id") Long id) {
        return super.find(id); // Finds and returns the Bouncer by ID
    }

    /**
     * Retrieves all Bouncer entities.
     */
    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) // Produces XML and JSON output
    public List<Bouncer> findAll() {
        return super.findAll(); // Returns all Bouncer entities
    }

    /**
     * Retrieves a range of Bouncer entities.
     */
    @GET
    @Path("{from}/{to}") // Path with the range parameters
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) // Produces XML and JSON output
    public List<Bouncer> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to}); // Finds and returns the range of Bouncer entities
    }

    /**
     * Returns the total count of Bouncer entities.
     */
    @GET
    @Path("count") // Path for count query
    @Produces(MediaType.APPLICATION_JSON) // Produces JSON output
    public Response countREST() {
        Integer count = super.count(); // Counts the total number of Bouncer entities
        String countPrint = "count is " + count.toString(); // Prepares the count string
        return Response.status(Response.Status.OK) // 200 OK for count
                       .entity(countPrint)
                       .build();
    }

    /**
     * Put on root resource
     * @return returns bad request because put on root resource is not allowed
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) // Accepts XML and JSON input
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) // Produces XML and JSON output
    public Response putOnRoot() {
        return Response.status(Response.Status.BAD_REQUEST) // 400 Bad Request
                       .entity("Bad Request")
                       .build();
    }

    /**
     * Returns the entity manager instance used for persistence operations.
     */
    @Override
    protected EntityManager getEntityManager() {
        return em; // Returns the injected EntityManager
    }
}
