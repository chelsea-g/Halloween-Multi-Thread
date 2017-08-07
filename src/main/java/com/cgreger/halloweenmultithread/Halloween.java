package com.cgreger.halloweenmultithread;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Halloween {

    private final Logger log = Logger.getLogger(this.getClass());
    private long startTime;
    private long stopTime;
    private long durration;
    private static final int MAX_CHILDREN_AT_DOOR = 10;
    private List<Child> childrenAtDoor;
    private boolean continueHalloween = true;
    private Thread rileyThread;
    private Thread genThread;


    public Halloween() {

        childrenAtDoor = new ArrayList<Child>();

    }

    public void simulateHalloween() {

        //keep track of application runtime
        startTime = System.currentTimeMillis();
        log.info("Trick or Treating has begun");

        //Create threads
        Riley riley = new Riley(this);
        ChildGenerator gen = new ChildGenerator(this);

        rileyThread = new Thread(riley);
        genThread = new Thread(gen);

        //Start threads
        rileyThread.start();
        genThread.start();

        try {

            // run Halloween as long as riley and generator are still running
            rileyThread.join();
            genThread.join();

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

    }

    public void startTrickOrTreating() {

        synchronized (childrenAtDoor) {

            // There are no children at the door, and trick or treating is not over
            while (childrenAtDoor.size() == 0 && continueHalloween) {

                log.info("Riley is watching TV");

                try {

                    // Wait until notified (when children at door)
                    childrenAtDoor.wait();

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }

            }

            //Answer door when there are children at door
            log.info("Riley answers the door");

            // This list is used to avoid ConcurrentModificationException while removing children from childrenAtDoor list
            List<Child> toRemove = new ArrayList<Child>();

            // Hand out candy to each child at the door for 3 seconds
            for (Child child : childrenAtDoor) {

                log.info("Riley hands out candy to child " + child.getId());

                try {

                    rileyThread.sleep(3000);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }

                // Set child up for removal
                log.info("Child " + child.getId() + " leaves with candy");
                toRemove.add(child);

            }

            // Remove children who got candy from list
            childrenAtDoor.removeAll(toRemove);

        }

    }

    public void stopTrickOrTreating() {

        // Calculate the runtime of the application and output it
        stopTime = System.currentTimeMillis();
        durration = (stopTime - startTime) / 1000;
        log.info("Trick or Treating has stopped.");
        log.info("Trick or Treating lasted " + durration + " seconds");

    }

    public void addChildAtDoor(Child child) {

        synchronized (childrenAtDoor) {

            // Let the child pass if there are already too many children at the door
            if (childrenAtDoor.size() == MAX_CHILDREN_AT_DOOR) {

                log.info("Too many children at the door");
                log.info("Child " + child.getId() + " passes by");

            } else {

                // There are still spots left at the door, ring the doorbell
                childrenAtDoor.add(child);
                log.info("Child " + child.getId() + " rings doorbell");

            }

            // notify childrenAtDoor when there is 1 child there (makes riley answer the door)
            if (childrenAtDoor.size() == 1) {

                childrenAtDoor.notify();

            }

        }

    }

    public void setContinueHalloween(boolean continueHalloween) {

        this.continueHalloween = continueHalloween;

    }

    public boolean getContinueHalloween() {

        return continueHalloween;

    }

    public List<Child> getChildrenAtDoor() {

        return childrenAtDoor;

    }

}
