/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package cst8218.bouncer_2.ejb;

import cst8218.bouncer_2.entity.Bouncer;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * The `BouncerFacade` is a stateless EJB that extends the `AbstractFacade<Bouncer>` class.
 * It provides an implementation for managing `Bouncer` entity objects using JPA.
 * This class acts as a data access layer for performing database operations on `Bouncer` entities.
 * 
 * - Uses `@Stateless` annotation, meaning this EJB does not maintain any client-specific state.
 * - Implements `getEntityManager()` to provide the `EntityManager` instance.
 * - Inherits CRUD operations from `AbstractFacade<Bouncer>`, reducing code duplication.
 * 
 * @author Saurav
 */

@Stateless  // Declares this class as a stateless session bean, ensuring better scalability.
public class BouncerFacade extends AbstractFacade<Bouncer> {
    
    @PersistenceContext(unitName = "my_persistence_unit")  
    private EntityManager em;  // Manages persistence context for `Bouncer` entity.

    /**
     * Provides the `EntityManager` instance for database interactions.
     * This method is required by `AbstractFacade` to access the database.
     * 
     * @return EntityManager instance for persistence operations.
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Constructor that initializes the `BouncerFacade` by passing `Bouncer.class`
     * to the parent `AbstractFacade` class.
     */
    public BouncerFacade() {
        super(Bouncer.class);
    }
}
