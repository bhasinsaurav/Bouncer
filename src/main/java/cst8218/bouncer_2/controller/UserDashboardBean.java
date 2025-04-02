/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cst8218.bouncer_2.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 * Managed Bean for user dashboard functionality.
 * It determines the user's role and provides a logout function.
 * 
 * This bean is request-scoped, meaning a new instance is created for each HTTP request.
 * It helps manage user session and role-based access control.
 * 
 * @author dell
 */
@Named("userDashboardBean")  // Makes this class accessible in JSF pages using #{userDashboardBean}
@RequestScoped  // The bean is created per request and destroyed afterward
public class UserDashboardBean implements Serializable {

    /**
     * Checks if the currently logged-in user has the "Admin" role.
     * 
     * @return true if the user has the "Admin" role, false otherwise.
     */
    public boolean isAdmin() {
        return FacesContext.getCurrentInstance().getExternalContext()
            .isUserInRole("Admin");
    }

    /**
     * Checks if the currently logged-in user has the "JsfUser" role.
     * 
     * @return true if the user has the "JsfUser" role, false otherwise.
     */
    public boolean isJsfUser() {
        return FacesContext.getCurrentInstance().getExternalContext()
            .isUserInRole("JsfUser");
    }

    /**
     * Checks if the currently logged-in user has the "RestUser" role.
     * 
     * @return true if the user has the "RestUser" role, false otherwise.
     */
    public boolean isRestUser() {
        return FacesContext.getCurrentInstance().getExternalContext()
            .isUserInRole("RestUser");
    }

    /**
     * Logs out the user by invalidating the session and redirecting to the homepage.
     * 
     * @return A navigation string to redirect to index.xhtml after logout.
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();  // Ends the user's session
        return "/index.xhtml?faces-redirect=true";  // Redirects to the home page
    }
}
