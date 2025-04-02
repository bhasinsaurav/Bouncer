/*
 * JUnit test class for Bouncer entity
 * Tests core functionality of the Bouncer class including movement, updates, and validation
 */
package tests;

import cst8218.bouncer_2.entity.Bouncer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BouncerUnitTests {
    
    private Bouncer bouncer;  // Test fixture - the Bouncer instance we'll test
    
    // Empty constructor - not really needed for JUnit 5
    public BouncerUnitTests() {
    }
    
    // Setup and teardown methods ==============================================
    
    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
        // Could initialize expensive resources here (runs once before all tests)
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
        // Could clean up expensive resources here (runs once after all tests)
    }
    
    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
        // Runs before EACH test method
        // Create a fresh Bouncer instance with known initial state
        bouncer = new Bouncer(100, 100, 50);  // x=100, y=100, maxTravel=50
        bouncer.setSize(Bouncer.INITIAL_SIZE); // size=10 (from constant)
        bouncer.setCurrentTravel(0);           // currentTravel=0
        bouncer.setDirChangeCount(0);          // dirChangeCount=0
        bouncer.setMvtDirection(1);            // Start moving right (+1)
    }
    
    // Test methods ============================================================
    
    @Test
    public void testConstructorInitialization() {
        // Verify constructor properly sets initial values
        assertEquals(Integer.valueOf(100), bouncer.getX());
        assertEquals(Integer.valueOf(100), bouncer.getY());
        assertEquals(Integer.valueOf(50), bouncer.getMaxTravel());
        assertEquals(Bouncer.INITIAL_SIZE, bouncer.getSize());
        assertEquals(Integer.valueOf(0), bouncer.getCurrentTravel());
        assertEquals(Integer.valueOf(0), bouncer.getDirChangeCount());
        assertEquals(Integer.valueOf(1), bouncer.getMvtDirection());
    }
 
    @Test
    public void testSetValidSize() {
        // Test that size can be set to a valid value
        bouncer.setSize(50);
        assertEquals(Integer.valueOf(50), bouncer.getSize());
    }
    
    @Test
    public void testMaxTravelDecreaseAfterMaxDirectionChanges() {
        // Set direction change count to max
        bouncer.setDirChangeCount(Bouncer.MAX_DIR_CHANGES);
        int initialMaxTravel = bouncer.getMaxTravel();
        
        // Trigger direction change by setting current travel to max
        bouncer.setCurrentTravel(bouncer.getMaxTravel());
        bouncer.timeStep();  // This should trigger max travel decrease
        
        // Verify max travel decreased and direction count reset
        assertEquals(initialMaxTravel - Bouncer.DECREASE_RATE, bouncer.getMaxTravel().intValue());
        assertEquals(0, bouncer.getDirChangeCount().intValue()); 
    }
    
    @Test
    public void testNoMovementWhenMaxTravelZero() {
        // Set max travel to zero - should prevent movement
        bouncer.setMaxTravel(0);
        int initialX = bouncer.getX();
        bouncer.timeStep();
        assertEquals(initialX, bouncer.getX().intValue()); // Position shouldn't change
    }
    
    // Test updateOldBouncer method ============================================
    
    @Test
    public void testUpdateOldBouncer() {
        // Create a new bouncer with different values
        Bouncer newBouncer = new Bouncer();
        newBouncer.setX(200);
        newBouncer.setY(150);
        newBouncer.setSize(20);
        newBouncer.setCurrentTravel(10);
        newBouncer.setMaxTravel(60);
        newBouncer.setDirChangeCount(5);
        newBouncer.setMvtDirection(-1);
        newBouncer.setId(2L);
        
        // Update original bouncer with new values
        bouncer.updateOldBouncer(newBouncer);
        
        // Verify all fields were updated
        assertEquals(Integer.valueOf(200), bouncer.getX());
        assertEquals(Integer.valueOf(150), bouncer.getY());
        assertEquals(Integer.valueOf(10), bouncer.getSize());
        assertEquals(Integer.valueOf(10), bouncer.getCurrentTravel());
        assertEquals(Integer.valueOf(60), bouncer.getMaxTravel());
        assertEquals(Integer.valueOf(5), bouncer.getDirChangeCount());
        assertEquals(Integer.valueOf(-1), bouncer.getMvtDirection());
        assertEquals(Long.valueOf(2), bouncer.getId());
    }
    
    @Test
    public void testUpdateOldBouncerWithNullValues() {
        // Save original values for comparison
        Integer originalX = bouncer.getX();
        Integer originalY = bouncer.getY();
        Integer originalSize = bouncer.getSize();
        
        // Create new bouncer with only some fields set
        Bouncer newBouncer = new Bouncer();
        newBouncer.setCurrentTravel(15);
        newBouncer.setMvtDirection(-1);
        
        // Perform update
        bouncer.updateOldBouncer(newBouncer);
        
        // Verify only specified fields were updated, others remain unchanged
        assertEquals(originalX, bouncer.getX());
        assertEquals(originalY, bouncer.getY());
        assertEquals(originalSize, bouncer.getSize());
        assertEquals(Integer.valueOf(15), bouncer.getCurrentTravel());
        assertEquals(Integer.valueOf(-1), bouncer.getMvtDirection());
    }
    
    
    
    @Test
    public void testDirectionChange() {
        bouncer.setCurrentTravel(bouncer.getMaxTravel());
        bouncer.timeStep();
        assertEquals(-1, bouncer.getMvtDirection().intValue());
    }
    
}