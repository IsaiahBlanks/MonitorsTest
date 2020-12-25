package MonitorHW;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class MinionThread extends Thread implements ActorThread {
    private Boolean lunchReady = false;
    private Boolean gottenKiss = false;
    public volatile Boolean atWork = false;
    private volatile Boolean waitingAtDoor = true;
    private volatile boolean eaten = false;
    private volatile boolean notAtTable = true;
    private int id;
    Object bathroom = new Object();
    private final Semaphore semaphore = new Semaphore(1);

    public MinionThread(String s, int i) {
        this.setName(s);
        id = i;
    }
    Random random = new Random();

    @Override
    public void run() {
        System.out.println(getName() + " is awake and waiting for their lunch!");
        while (!lunchReady && !gottenKiss) {
            //wait for lunch to be ready and to get a kiss from Alice
        }

        synchronized (this) {
            MonitorsTest.minionsGoingToWork.add(this);
        }

        System.out.println(getName() + " says: Thank you alice");
        System.out.println(getName() + " is waiting to leave for work");
        while (!atWork) {

        }
        System.out.println(getName() + " is going to work at the Deli");
        try {
            Thread.sleep(random.nextInt(10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(getName() + " is home from work!");

        MonitorsTest.addMinionToDoor(this);

        while (waitingAtDoor) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(getName() + " is going to play games now!");

        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(getName() + " is ready to eat!");
        while (!eaten) {
            if(notAtTable) {
                MonitorsTest.attemptToEat(this);
            }
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(getName() + " has finished eating!");

        goToBathroom();

        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MonitorsTest.incrementChildrenSleeping();
        System.out.println(getName() + " is sleeping now. Shhhh!");
    }

    public void setLunchReady(boolean val) {
        lunchReady = val;
    }

    public void setGottenKiss(boolean val) {
        gottenKiss = val;
    }

    public void setAtWork(boolean val) {
        atWork = val;
    }

    public int getMinionId() {
        return id;
    }

    public void setWaitingAtDoor(boolean val) {
        waitingAtDoor = val;
    }

    @Override
    public void setEaten(boolean val) {
        eaten = val;
    }

    @Override
    public String getTitleVal() {
        return getName();
    }

    @Override
    public void setNotAtTable(boolean val) {
        notAtTable = val;
    }

    public synchronized Object getBathroom() throws InterruptedException {
        semaphore.acquire();
        System.out.println(getName() + " is using the bathroom before bed");
        return bathroom;
    }

    private void goToBathroom() {
        try {
            getBathroom();
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
