package com.cgreger.halloweenmultithread;

public class Riley implements Runnable {

    private Halloween halloween;

    public Riley(Halloween halloween) {

        this.halloween = halloween;

    }

    public void run() {

        watchTV();

    }

    public void watchTV() {

        // Continue handing out candy and watching tv until halloween is over
        while (halloween.getContinueHalloween()) {

            halloween.startTrickOrTreating();

        }

        // Stops application
        halloween.stopTrickOrTreating();

    }

}
