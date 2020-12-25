package MonitorHW;

import java.util.Random;

public class BobThread extends Thread implements ActorThread {
    private volatile boolean timeToComeHome = false;
    private volatile boolean eaten = false;
    private Boolean lunchReady = false;
    private Boolean gottenKiss = false;
    private Boolean atWork = false;
    private volatile boolean notAtTable = true;
    private volatile boolean reading = false;
    Random random = new Random();

    public BobThread(String s) {
        setName(s);
    }


    @Override
    public void run() {
        System.out.println("Bob: I'm awake!");
        while (!lunchReady && !gottenKiss) {
            //wait for lunch to be ready and to get a kiss from Alice
        }
        System.out.println("Thanks alice! See you after I finish my accountant work");
        while (!timeToComeHome) {

        }

        System.out.println("Hey everyone, it's Bob! I'm home!");

        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Bob is ready to eat!");
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
        System.out.println("Bob has finished eating!");

        while (!reading) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Bob is reading now");

        try {
            Thread.sleep(random.nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setReading(false);
        System.out.println("Bob is done reading. Good night!");
    }

    public void setLunchReady(boolean val) {
        lunchReady = val;
    }

    public void setGottenKiss(boolean val) {
        lunchReady = val;
    }

    public void setTimeToComeHome(boolean val) {
        timeToComeHome = val;
    }

    public void waitToEat() {
        while (!eaten) {
            MonitorsTest.attemptToEat(this);
        }
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

    public void setReading(boolean val) {
        reading = val;
    }

    public boolean getReading() {
        return reading;
    }
}
