package MonitorHW;


import java.util.Random;

public class AliceThread extends Thread implements ActorThread {
    private boolean timeToWakeUpBob = false;
    private volatile boolean timeToEat = false;
    private volatile boolean eaten = false;
    private volatile boolean notAtTable = true;
    private volatile boolean reading = false;
    Random random = new Random();

    public AliceThread(String s) {
        setName(s);
    }

    @Override
    public void run() {
        System.out.println("Alice is awake!");

        makeEveryoneLunch();

        while (!timeToWakeUpBob) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Alice: I am waking up Bob now");
        MonitorsTest.wakeUpBob();

        synchronized (MonitorsTest.bob) {
            MonitorsTest.bob.setGottenKiss(true);
            MonitorsTest.bob.setLunchReady(true);
        }

        while (!timeToEat) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        System.out.println("Alice is ready to eat!");
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

        System.out.println("Alice has finished eating!");

        while (!reading) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Alice is reading now");

        try {
            Thread.sleep(random.nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setReading(false);
        System.out.println("Alice is done reading. Good night!");

    }

    private void makeEveryoneLunch() {
        for(GnomeThread gnome : MonitorsTest.gnomeThreads) {
            gnome.setLunchReady(true);
            gnome.setGottenKiss(true);
        }
        for(MinionThread minion : MonitorsTest.minionThreads) {
            minion.setLunchReady(true);
            minion.setGottenKiss(true);
        }
    }

    public void setTimeToWakeUpBob(boolean val) {
        timeToWakeUpBob = val;
    }

    public void sendMinionsToPlay() {
        while (!MonitorsTest.minionsAtDoor.isEmpty()) {
            MonitorsTest.minionsAtDoor.removeFirst().setWaitingAtDoor(false);
        }
    }

    public void sendGnomesToPlay() {
        while (!MonitorsTest.gnomesAtDoor.isEmpty()) {
            MonitorsTest.gnomesAtDoor.removeLast().setWaitingAtDoor(false);
        }
    }

    public void waitToEat() {

    }

    @Override
    public void setEaten(boolean val) {
        eaten = val;
    }

    @Override
    public String getTitleVal() {
        return getName();
    }

    public void setTimeToEat(boolean val) {
        timeToEat = val;
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
