// Package declaration
package cst8218.bouncer_2.entity;

// Importing necessary libraries for JPA and validation
import jakarta.persistence.Entity; // Marks the class as a JPA entity
import jakarta.persistence.GeneratedValue; // Specifies how the ID will be generated
import jakarta.persistence.GenerationType; // Strategy for ID generation (IDENTITY)
import jakarta.persistence.Id; // Marks the primary key field
import jakarta.validation.constraints.Max; // To enforce maximum value on the field
import jakarta.validation.constraints.Min; // To enforce minimum value on the field
import jakarta.validation.constraints.NotNull; // Ensures the field cannot be null
import java.io.Serializable; // Marks the class as serializable for storage or transmission

/**
 * Bouncer class represents an entity that models a moving object with certain properties,
 * such as its position, size, travel parameters, and direction changes.
 * The class implements Serializable for object serialization.
 */
@Entity // This class is marked as a JPA entity (it will be mapped to a table in the database)
public class Bouncer implements Serializable {

    // Constants for default and limit values related to the bouncer's behavior
    public static final int INITIAL_SIZE = 10; // Initial size of the bouncer
    public static final int SIZE_LIMIT = 200; // Maximum size limit for the bouncer
    public static final int CHANGE_RATE = 2; // Rate at which size or position changes
    public static final int TRAVEL_SPEED = 10; // Speed at which the bouncer moves
    public static final int MAX_DIR_CHANGES = 10; // Maximum number of direction changes allowed
    public static final int DECREASE_RATE = 1; // Rate at which max travel is decreased after excessive direction changes
    public static final int X_LIMIT = 400; // The maximum limit for the X position
    public static final int Y_LIMIT = 200; // The maximum limit for the Y position
    public static final int MAX_TRAVEL_LIMIT = 100; // Maximum travel limit for the bouncer

    // ID field, which is the primary key of the entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Specifies auto-increment for the primary key
    private Long id;

    // X position of the bouncer, restricted between 0 and X_LIMIT
    @NotNull // Ensures this field cannot be null
    @Min(0) @Max(X_LIMIT) // Ensures the value is between 0 and X_LIMIT
    private Integer x;

    // Y position of the bouncer, restricted between 0 and Y_LIMIT
    @NotNull
    @Min(0) @Max(Y_LIMIT)
    private Integer y;

    // Size of the bouncer, restricted between 1 and SIZE_LIMIT, with a default value of INITIAL_SIZE
    @NotNull
    @Min(1) @Max(SIZE_LIMIT)
    private Integer size = INITIAL_SIZE;

    // Current travel distance of the bouncer, initially set to INITIAL_SIZE
    @NotNull
    private Integer currentTravel = INITIAL_SIZE;

    // Maximum distance the bouncer can travel, cannot be null
    @NotNull
    @Min(0) @Max(MAX_TRAVEL_LIMIT)
    private Integer maxTravel;

    // Counter for direction changes, restricted to the MAX_DIR_CHANGES
    @NotNull
    @Min(0) @Max(MAX_DIR_CHANGES)
    private Integer dirChangeCount;

    // Movement direction: +1 for right, -1 for left
    private Integer mvtDirection;  // +1 = right , -1 = Left

    // Default constructor
    public Bouncer() {}

    // Constructor to initialize the bouncer with X, Y positions, and max travel
    public Bouncer(Integer x, Integer y, Integer maxTravel) {
        this.x = x;
        this.y = y;
        this.maxTravel = maxTravel;
    }

    // Getter for the X position
    public Integer getX() {
        return x;
    }

    // Getter for the Y position
    public Integer getY() {
        return y;
    }

    // Setter for the X position
    public void setX(Integer x) {
        this.x = x;
    }

    // Setter for the Y position
    public void setY(Integer y) {
        this.y = y;
    }

    // Setter for the size
    public void setSize(Integer size) {
        this.size = size;
    }

    // Setter for the current travel distance
    public void setCurrentTravel(Integer currentTravel) {
        this.currentTravel = currentTravel;
    }

    // Setter for the maximum travel distance
    public void setMaxTravel(Integer maxTravel) {
        this.maxTravel = maxTravel;
    }

    // Setter for the direction change counter
    public void setDirChangeCount(Integer dirChangeCount) {
        this.dirChangeCount = dirChangeCount;
    }

    // Setter for the movement direction
    public void setMvtDirection(Integer mvtDirection) {
        this.mvtDirection = mvtDirection;
    }

    // Getter for the size
    public Integer getSize() {
        return size;
    }

    // Getter for the current travel distance
    public Integer getCurrentTravel() {
        return currentTravel;
    }

    // Getter for the maximum travel distance
    public Integer getMaxTravel() {
        return maxTravel;
    }

    // Getter for the direction change counter
    public Integer getDirChangeCount() {
        return dirChangeCount;
    }

    // Getter for the movement direction
    public Integer getMvtDirection() {
        return mvtDirection;
    }

    // Getter for the ID
    public Long getId() {
        return id;
    }

    // Setter for the ID (should be used carefully as ID is typically auto-generated)
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Simulates a time step in the bouncer's movement and updates its position and direction.
     * If the maximum travel is reached, the direction is reversed, and the number of direction changes is updated.
     * If the number of direction changes exceeds the limit, the maximum travel is reduced.
     */
    public void timeStep() {
        if (maxTravel > 0) { // Ensure maxTravel is greater than 0
            currentTravel += mvtDirection * TRAVEL_SPEED; // Update current travel based on direction and speed
            if (Math.abs(currentTravel) >= maxTravel) { // If max travel is reached
                mvtDirection = -mvtDirection; // Reverse direction
                dirChangeCount++; // Increase direction change count
                if (dirChangeCount > MAX_DIR_CHANGES) { // If max direction changes exceeded
                    maxTravel -= DECREASE_RATE; // Decrease the max travel distance
                    dirChangeCount = 0; // Reset the direction change count
                }
            }
        }   
    }

    /**
     * Updates the current bouncer with the values from a new bouncer, preserving non-null values of the old bouncer.
     * This method is useful for updating a bouncer without losing any previously set values.
     * @param newBouncer The new Bouncer object to update the current one with.
     */
    public void updateOldBouncer(Bouncer newBouncer){
        if(newBouncer.getCurrentTravel()!=null){
            this.setCurrentTravel(newBouncer.getCurrentTravel()); // Update current travel if not null
        }
        
        if(newBouncer.getDirChangeCount()!=null){
            this.setDirChangeCount(newBouncer.getDirChangeCount()); // Update direction change count if not null
        }
        
        if(newBouncer.getMaxTravel()!=null){
            this.setMaxTravel(newBouncer.getMaxTravel()); // Update max travel if not null
        }
        
        if(newBouncer.getMvtDirection()!=null){
            this.setMvtDirection(newBouncer.getMvtDirection()); // Update movement direction if not null
        }
        
        if(newBouncer.getX()!=null){
            this.setX(newBouncer.getX()); // Update X position if not null
        }
        
        if(newBouncer.getY()!=null){
            this.setY(newBouncer.getY()); // Update Y position if not null
        }
        
        if(newBouncer.getSize()!=null){
             this.setId(newBouncer.getId()); // Update ID if not null (though ID is usually auto-generated)
        }
    }
}
