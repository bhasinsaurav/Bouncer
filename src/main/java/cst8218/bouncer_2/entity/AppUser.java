package cst8218.bouncer_2.entity;

// Import necessary classes for JPA, CDI, security, and validation
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.security.enterprise.identitystore.PasswordHash;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.SeBootstrap.Instance;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * AppUser Entity class representing a user in the system.
 * This class is mapped to the "APPUSER" table in the database.
 */
@Entity
@Table(name = "APPUSER")
@XmlRootElement
@NamedQueries({
    // Named queries for fetching users based on different fields
    @NamedQuery(name = "AppUser.findAll", query = "SELECT c FROM AppUser c"),
    @NamedQuery(name = "AppUser.findById", query = "SELECT c FROM AppUser c WHERE c.id = :id"),
    @NamedQuery(name = "AppUser.findByUserID", query = "SELECT c FROM AppUser c WHERE c.userid = :userid"),
    @NamedQuery(name = "AppUser.findByPassword", query = "SELECT c FROM AppUser c WHERE c.password = :password"),
    @NamedQuery(name = "AppUser.findByGroupName", query = "SELECT c FROM AppUser c WHERE c.groupname = :groupname")
})
public class AppUser implements Serializable {

    // Serializable unique identifier for the AppUser class.
    private static final long serialVersionUID = 1L;
    
    // The primary key for the AppUser entity, auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    // User ID, unique for each user
    @Size(max = 255)
    @Column(name = "UserID")
    private String userid;
    
    // Password for the user. The actual password value is hashed.
    @Size(max = 255)
    @Column(name = "Password")
    private String password;
    
    // The group name the user belongs to
    @Size(max = 255)
    @Column(name = "GroupName")
    private String groupname;

    // Default constructor
    public AppUser() {
    }

    // Constructor with user ID as parameter
    public AppUser(Long id) {
        this.id = id;
    }

    // Getter for the user's ID
    public Long getId() {
        return id;
    }

    // Setter for the user's ID
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for the user ID (username)
    public String getUserid() {
        return userid;
    }

    // Setter for the user ID (username)
    public void setUserid(String userid) {
        this.userid = userid;
    }

    // Getter for the password (note: it returns an empty string for security reasons)
    public String getPassword() {
        return "";
    }

    // Setter for the password: hashes the password using PBKDF2 before storing it
    public void setPassword(String password) {
        if (password != null && !password.isEmpty()) {
            // Getting the PBKDF2PasswordHash instance from CDI container
            var instance = CDI.current().select(Pbkdf2PasswordHash.class);
            Pbkdf2PasswordHash passwordHash = instance.get();
            
            // Initialize the hashing instance (with default parameters)
            passwordHash.initialize(new HashMap<>());
            
            // Hash the password and set it
            this.password = passwordHash.generate(password.toCharArray());
        } else if (this.password == null) {
            // If password is empty or null, handle default case (new user)
            this.password = "";
        }
    }

    // Getter for the user's group name
    public String getGroupName() {
        return groupname;
    }

    // Setter for the user's group name
    public void setGroupName(String groupname) {
        this.groupname = groupname;
    }

    // Override hashCode to ensure uniqueness in collections (like HashMap)
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    // Override equals method to compare users based on their ID
    @Override
    public boolean equals(Object object) {
        // If object is not an instance of AppUser, return false
        if (!(object instanceof AppUser)) {
            return false;
        }
        AppUser other = (AppUser) object;
        
        // Check if the IDs are the same, return false if they are not
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    // Override toString method to provide a string representation of the AppUser
    @Override
    public String toString() {
        return "cst8218.bhas0011.lab4.entity.AppUser [ id=" + id + ", userid=" + userid + ", groupname=" + groupname + " ]";
    }
}
