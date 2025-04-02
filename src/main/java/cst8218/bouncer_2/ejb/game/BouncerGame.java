/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SingletonEjbClass.java to edit this template
 */
package cst8218.bouncer_2.ejb.game;

import cst8218.bouncer_2.ejb.BouncerFacade;
import cst8218.bouncer_2.entity.Bouncer;
import static cst8218.bouncer_2.entity.Bouncer.CHANGE_RATE;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import java.util.List;

/**
 *
 * @author dell
 */
@Startup
@Singleton
@LocalBean
public class BouncerGame {
    // Injecting BouncerFacade to interact with the database
    @Inject
    private BouncerFacade bouncerFacade;

    // List to hold all Bouncers
    private List<Bouncer> bouncers;

    // Method to start the game loop after construction
     @PostConstruct
    public void go() {
        // Start the game in a new thread
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    // Update all bouncers and save changes to the database
                    List<Bouncer> bouncers = bouncerFacade.findAll();  // Fetch bouncers from the database
                    for (Bouncer bouncer : bouncers) {
                        bouncer.timeStep();  // Update each bouncer's state
                        bouncerFacade.edit(bouncer);  // Persist the changes back to the database
                    }
                    // Sleep for the next frame
                    try {
                        // Wait until the next frame, based on the CHANGE_RATE
                        Thread.sleep((long) (1000 / CHANGE_RATE));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();  // Start the thread
    }
}

