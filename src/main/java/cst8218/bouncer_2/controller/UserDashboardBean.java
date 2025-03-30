/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8218.bouncer_2.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 *
 * @author dell
 */
@Named("userDashboardBean")
@RequestScoped
public class UserDashboardBean implements Serializable {
    public boolean isAdmin() {
        return FacesContext.getCurrentInstance().getExternalContext()
            .isUserInRole("Admin");
    }
    
    public boolean isJsfUser() {
        return FacesContext.getCurrentInstance().getExternalContext()
            .isUserInRole("JsfUser");
    }
    
    public boolean isRestUser() {
        return FacesContext.getCurrentInstance().getExternalContext()
            .isUserInRole("RestUser");
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }
}
