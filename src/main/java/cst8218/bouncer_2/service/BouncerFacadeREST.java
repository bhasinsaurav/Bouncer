/*
 * RESTful Web Service for managing Bouncer entities.
 * Provides CRUD operations using Jakarta RESTful Web Services (JAX-RS).
 */
package cst8218.bouncer_2.service;

import cst8218.bouncer_2.ejb.AbstractFacade;
import cst8218.bouncer_2.entity.Bouncer;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Stateless RESTful service that manages Bouncer entities.
 */

@RolesAllowed({"Admin", "RestUser"})
@Stateless
@Path("cst8218.bouncer_2.entity.Bouncer")
public class BouncerFacadeREST extends AbstractFacade<Bouncer> {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    // Constructor: Calls the parent AbstractFacade with Bouncer class type
    public BouncerFacadeREST() {
        super(Bouncer.class);
    }

    /**
     * Creates a new Bouncer or updates an existing one if the ID is provided.
     * - If the ID is null, a new Bouncer is created.
     * - If the ID exists, it updates the existing Bouncer with non-null values.
     * - If the ID is provided but does not exist, returns a bad request response.
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response creatRecord(Bouncer entity) {

        // Case 1: Updating an existing Bouncer
        if (entity.getId() != null && super.find(entity.getId()) != null) {
            Bouncer oldBouncer = super.find(entity.getId());
            oldBouncer.updateOldBouncer(entity); // Merging new values
            super.edit(oldBouncer);
            return Response.status(Response.Status.OK) // 200 OK for update
                           .entity(super.find(entity.getId()))
                           .build();
        }

        // Case 2: Creating a new Bouncer
        if (entity.getId() == null) {
            super.create(entity);
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
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createBouncer(@PathParam("id") Long id, Bouncer entity) {
        // Ensures the provided ID exists in the database
        if (super.find(id) == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Bad Request")
                           .build();
        }

        // Ensures the entity's ID matches the path ID
        if (!id.equals(entity.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Bad Request")
                           .build();
        }

        // Removes the old entity and creates a new one
         if( id == super.find(id).getId()){
            super.edit(super.find(id));
            
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
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(@PathParam("id") Long id, Bouncer entity) {
        // Case 1: ID not found in the database
        if (super.find(id) == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Bad Request")
                           .build();
        }

        // Case 2: Entity ID does not match the path ID
        if (!id.equals(entity.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Bad Request")
                           .build();
        }

        // Case 3: Update the existing Bouncer
        Bouncer oldBouncer = super.find(entity.getId());
        oldBouncer.updateOldBouncer(entity); // Merging values
        super.edit(oldBouncer);
        return Response.status(Response.Status.OK) // 200 OK for update
                       .entity(super.find(entity.getId()))
                       .build();
    }

    /**
     * Deletes a Bouncer by ID.
     */
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    /**
     * Retrieves a single Bouncer by ID.
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Bouncer find(@PathParam("id") Long id) {
        return super.find(id);
    }

    /**
     * Retrieves all Bouncer entities.
     */
    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Bouncer> findAll() {
        return super.findAll();
    }

    /**
     * Retrieves a range of Bouncer entities.
     */
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Bouncer> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    /**
     * Returns the total count of Bouncer entities.
     */
    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countREST() {
        Integer count = super.count();
        String countPrint = "count is " + count.toString();
        return Response.status(Response.Status.OK) // 200 OK
                       .entity(countPrint)
                       .build();
    }
    /**
     * Put on root resource
     * @return returns bad request because put on root resource is not allowed
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response putOnRoot(){
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity("Bad Request")
                       .build();
    }
    /**
     * Returns the entity manager instance used for persistence operations.
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
