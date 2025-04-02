package cst8218.bouncer_2.controller;

import cst8218.bouncer_2.entity.Bouncer;
import cst8218.bouncer_2.util.JsfUtil;
import cst8218.bouncer_2.util.PaginationHelper;
import cst8218.bouncer_2.persistence.BouncerJpaController;
import jakarta.annotation.PostConstruct;

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
import java.util.Locale;

@Named("bouncerController") // Defines this bean as a JSF managed bean
@SessionScoped // Maintains state during a user session
public class BouncerController implements Serializable {

    @Resource
    private UserTransaction utx = null; // Handles transactions
    
    @PersistenceUnit(unitName = "my_persistence_unit")
    private EntityManagerFactory emf = null; // Manages entity persistence

    private Bouncer current; // Stores the currently selected Bouncer entity
    private DataModel items = null; // Holds the list of Bouncer entities
    private BouncerJpaController jpaController = null; // Controller for database operations
    private PaginationHelper pagination; // Handles pagination of results
    private int selectedItemIndex; // Tracks the currently selected item
    private Locale locale; // Stores the current locale for internationalization
    
    // Default constructor
    public BouncerController() {}

    // Initializes the locale based on user request settings
    @PostConstruct
    public void init(){
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }
    
    public Locale getLocale(){
        return locale;
    }
    
    public String getLanguage(){
        return locale.getLanguage();
    }
    
    public void setLanguage(String language){
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }
    
    public Bouncer getSelected() {
        if (current == null) {
            current = new Bouncer();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    private BouncerJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new BouncerJpaController(utx, emf);
        }
        return jpaController;
    }

    // Provides pagination support
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getBouncerCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findBouncerEntities(getPageSize(), getPageFirstItem()));
                }
            };
        }
        return pagination;
    }

    // Navigation methods
    public String prepareList() {
        recreateModel();
        return "List";
    }
    
    public String prepareView() {
        current = (Bouncer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Bouncer();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BouncerCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Bouncer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BouncerUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Bouncer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    private void performDestroy() {
        try {
            getJpaController().destroy(current.getId());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BouncerDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

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

    @FacesConverter(forClass = Bouncer.class)
    public static class BouncerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BouncerController controller = (BouncerController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "bouncerController");
            return controller.getJpaController().findBouncer(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Bouncer) {
                return String.valueOf(((Bouncer) object).getId());
            } else {
                throw new IllegalArgumentException("Invalid object type: " + object.getClass().getName());
            }
        }
    }
}
