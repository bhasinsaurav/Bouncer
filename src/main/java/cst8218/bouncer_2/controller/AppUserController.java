package cst8218.bouncer_2.controller;

import cst8218.bouncer_2.entity.AppUser;
import cst8218.bouncer_2.util.JsfUtil;
import cst8218.bouncer_2.util.PaginationHelper;
import cst8218.bouncer_2.persistence.AppUserJpaController;

import java.io.Serializable;
import java.util.ResourceBundle;
import jakarta.annotation.Resource;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.model.DataModel;
import jakarta.faces.model.ListDataModel;
import jakarta.faces.model.SelectItem;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.UserTransaction;

@Named("appUserController") // JSF managed bean with the name "appUserController"
@SessionScoped // Scope of the bean is for the entire session
public class AppUserController implements Serializable {

    @Resource // Injects the UserTransaction for managing transactions
    private UserTransaction utx = null;

    @PersistenceUnit(unitName = "my_persistence_unit") // Injects the EntityManagerFactory
    private EntityManagerFactory emf = null;

    private AppUser current; // Holds the currently selected AppUser
    private DataModel items = null; // Stores the list of users in a DataModel
    private AppUserJpaController jpaController = null; // JPA Controller for AppUser operations
    private PaginationHelper pagination; // Helper for pagination
    private int selectedItemIndex; // Stores the index of the selected user

    public AppUserController() {
    }

    // Returns the currently selected AppUser, creates a new one if none is selected
    public AppUser getSelected() {
        if (current == null) {
            current = new AppUser();
            selectedItemIndex = -1;
        }
        return current;
    }

    // Returns an instance of the JPA Controller
    private AppUserJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new AppUserJpaController(utx, emf);
        }
        return jpaController;
    }

    // Provides pagination for the user list
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getJpaController().getAppUserCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findAppUserEntities(getPageSize(), getPageFirstItem()));
                }
            };
        }
        return pagination;
    }

    // Navigates to the list view
    public String prepareList() {
        recreateModel();
        return "List";
    }

    // Prepares the view page for a selected user
    public String prepareView() {
        current = (AppUser) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    // Prepares the create page by initializing a new AppUser object
    public String prepareCreate() {
        current = new AppUser();
        selectedItemIndex = -1;
        return "Create";
    }

    // Creates a new AppUser in the database
    public String create() {
        try {
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AppUserCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    // Prepares the edit page for a selected user
    public String prepareEdit() {
        current = (AppUser) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    // Updates an existing AppUser in the database
    public String update() {
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AppUserUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    // Deletes the selected user
    public String destroy() {
        current = (AppUser) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    private void performDestroy() {
        try {
            getJpaController().destroy(current.getId());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AppUserDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    // Retrieves the list of users for display
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    // Pagination control: move to the next page
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    // Pagination control: move to the previous page
    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    // Converter for JSF to handle AppUser objects in UI components
    @FacesConverter(forClass = AppUser.class)
    public static class AppUserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AppUserController controller = (AppUserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "appUserController");
            return controller.getJpaController().findAppUser(getKey(value));
        }

        java.lang.Long getKey(String value) {
            return Long.valueOf(value);
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AppUser) {
                return String.valueOf(((AppUser) object).getId());
            } else {
                throw new IllegalArgumentException("Expected AppUser, but received: " + object.getClass().getName());
            }
        }
    }
}
