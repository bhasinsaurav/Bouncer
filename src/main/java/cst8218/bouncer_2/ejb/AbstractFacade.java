/*
 * Copyright (c), Eclipse Foundation, Inc. and its licensors.
 *
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v1.0, which is available at
 * https://www.eclipse.org/org/documents/edl-v10.php
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package cst8218.bouncer_2.ejb;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * AbstractFacade is a generic Data Access Object (DAO) that provides 
 * basic CRUD (Create, Read, Update, Delete) operations for entity classes.
 * 
 * It is meant to be extended by specific entity facades, ensuring code reusability 
 * and enforcing a consistent data access approach.
 * 
 * @param <T> The entity type that this facade manages.
 * 
 * @author ian
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;  // Holds the class type of the entity being managed

    /**
     * Constructor to initialize the entity class type.
     * 
     * @param entityClass The class of the entity being managed.
     */
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Abstract method to obtain an instance of EntityManager.
     * Implementing classes must provide the EntityManager.
     * 
     * @return EntityManager instance for database operations.
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Persists (creates) a new entity in the database.
     * 
     * @param entity The entity to be created.
     */
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Merges (updates) an existing entity in the database.
     * 
     * @param entity The entity to be updated.
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Removes an entity from the database.
     * 
     * @param entity The entity to be removed.
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));  // Merging ensures the entity is managed before removal
    }

    /**
     * Finds an entity by its primary key.
     * 
     * @param id The primary key of the entity.
     * @return The entity if found, otherwise null.
     */
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Retrieves all entities of type T from the database.
     * 
     * @return A list containing all entities.
     */
    public List<T> findAll() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Retrieves a range of entities (pagination support).
     * 
     * @param range An array where range[0] is the starting index and range[1] is the ending index.
     * @return A list containing entities within the specified range.
     */
    public List<T> findRange(int[] range) {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);  // Limit the number of results
        q.setFirstResult(range[0]);  // Define the starting position
        return q.getResultList();
    }

    /**
     * Counts the total number of entities in the database.
     * 
     * @return The total count of entities.
     */
    public int count() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();  // Convert count result to int
    }
}
