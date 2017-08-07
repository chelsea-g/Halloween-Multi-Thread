package com.cgreger.halloweenmultithread;


import org.apache.log4j.Logger;


public class ChildGenerator implements Runnable {

    private final Logger log = Logger.getLogger(this.getClass());
    private Halloween halloween;
    private static final int NUM_CHILDREN_TO_GENERATE = 25;

    public ChildGenerator(Halloween halloween) {

        this.halloween = halloween;

    }

    public void run() {

        generateChildren();

    }

    public void generateChildren() {

        //Generate a child every second
        for (int i = 0; i < NUM_CHILDREN_TO_GENERATE; i++) {

            generateChild(i + 1);

            try {

                Thread.sleep(1000);

            }  catch(InterruptedException iex) {

                iex.printStackTrace();

            }

        }

        synchronized (halloween.getChildrenAtDoor()) {

            //Halloween is finished when all children have been
            //generated and there are no more children at the door
            if (halloween.getChildrenAtDoor().size() == 0) {

                halloween.setContinueHalloween(false);

            }

        }

    }

    public void generateChild(int id) {

        // Create new child thread
        Child child = new Child(halloween, id);
        Thread childThread = new Thread(child);

        log.info("Child " + id + " generated");

        childThread.start();

    }

}
