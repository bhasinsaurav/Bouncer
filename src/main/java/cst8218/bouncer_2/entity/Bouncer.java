/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8218.bouncer_2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author dell
 */
@Entity
public class Bouncer implements Serializable {

   
    //Constants
    public static final int INITIAL_SIZE = 10;
    public static final int SIZE_LIMIT = 200;
    public static final int CHANGE_RATE = 2;
    public static final int TRAVEL_SPEED = 10;
    public static final int MAX_DIR_CHANGES = 10;
    public static final int DECREASE_RATE = 1;
    public static final int X_LIMIT = 400;
    public static final int Y_LIMIT = 200;
    public static final int MAX_TRAVEL_LIMIT = 100;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Min(0) @Max(X_LIMIT)
    private Integer x;

    @NotNull
    @Min(0) @Max(Y_LIMIT)
    private Integer y;

    @NotNull
    @Min(1) @Max(SIZE_LIMIT)
    private Integer size = INITIAL_SIZE;

    @NotNull
    private Integer currentTravel = INITIAL_SIZE;

    @NotNull
    @Min(0) @Max(MAX_TRAVEL_LIMIT)
    private Integer maxTravel;
  
    @NotNull
    @Min(0) @Max(MAX_DIR_CHANGES)
    private Integer dirChangeCount;
    private Integer mvtDirection;  // +1 = right , -1 = Left
    
  
    public Bouncer() {}
    
    public Bouncer(Integer x, Integer y, Integer maxTravel) {
        this.x = x;
        this.y = y;
        this.maxTravel = maxTravel;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setCurrentTravel(Integer currentTravel) {
        this.currentTravel = currentTravel;
    }

    public void setMaxTravel(Integer maxTravel) {
        this.maxTravel = maxTravel;
    }

    public void setDirChangeCount(Integer dirChangeCount) {
        this.dirChangeCount = dirChangeCount;
    }

    public void setMvtDirection(Integer mvtDirection) {
        this.mvtDirection = mvtDirection;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getCurrentTravel() {
        return currentTravel;
    }

    public Integer getMaxTravel() {
        return maxTravel;
    }

    public Integer getDirChangeCount() {
        return dirChangeCount;
    }

    public Integer getMvtDirection() {
        return mvtDirection;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void timeStep() {
        if (maxTravel > 0) {
            currentTravel += mvtDirection * TRAVEL_SPEED;
            if (Math.abs(currentTravel) >= maxTravel) {
                mvtDirection = -mvtDirection;
                dirChangeCount++;
                if (dirChangeCount > MAX_DIR_CHANGES) {
                    maxTravel -= DECREASE_RATE;
                    dirChangeCount = 0;
            }
        }   
    }
}

    /**
     * Updates the old bouncer with the values in the new bouncer by preserving the old bouncer's not null values is they are null in the new bouncer.  
     * @param newBouncer 
     */
    public void updateOldBouncer(Bouncer newBouncer){
    
        if(newBouncer.getCurrentTravel()!=null){
            this.setCurrentTravel(newBouncer.getCurrentTravel());
        }
        
        if(newBouncer.getDirChangeCount()!=null){
            this.setDirChangeCount(newBouncer.getDirChangeCount());
        }
        
        if(newBouncer.getMaxTravel()!=null){
            this.setMaxTravel(newBouncer.getMaxTravel());
        }
        
        if(newBouncer.getMvtDirection()!=null){
            this.setMvtDirection(newBouncer.getMvtDirection());
        }
        
        if(newBouncer.getX()!=null){
            this.setX(newBouncer.getX());
        }
        
        if(newBouncer.getY()!=null){
            this.setY(newBouncer.getY());
        }
        
        if(newBouncer.getSize()!=null){
             this.setId(newBouncer.getId());
        }
    }
    
}
