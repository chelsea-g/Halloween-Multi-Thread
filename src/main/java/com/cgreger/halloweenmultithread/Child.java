package com.cgreger.halloweenmultithread;


import org.apache.log4j.Logger;

public class Child implements Runnable {

    private final Logger log = Logger.getLogger(this.getClass());
    private int id;
    private Halloween halloween;

    public Child(Halloween halloween, int id) {
        this.halloween = halloween;
        this.id = id;
    }

    public void run() {

        goTrickOrTreating();

    }

    private synchronized void goTrickOrTreating() {

        // Try to get candy!
        halloween.addChildAtDoor(this);

    }

    public int getId(){

        return id;

    }

}