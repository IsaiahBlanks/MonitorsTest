package MonitorHW;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

public class MonitorsTest {
    public volatile boolean everyoneEaten = false;
    public static ArrayList<MinionThread> minionThreads;
    public static ArrayList<GnomeThread> gnomeThreads;

    public static Vector<MinionThread> minionsGoingToWork;
    public static Vector<GnomeThread> gnomesGoingToWork;

    public static LinkedList<MinionThread> minionsAtDoor;
    public static LinkedList<GnomeThread> gnomesAtDoor;

    public static LinkedList<ActorThread> tableList;

    private static volatile int childrenSleeping = 0;

    private int minionsHome = 0;
    private int gnomesHome = 0;

    public static BobThread bob;

    public static void main(String[] args) {
        MonitorsTest test = new MonitorsTest();
        try {
            test.doDay();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void doDay() throws InterruptedException {

        //Populate two arraylists, one with 10 minion threads and one with 7 gnome threads
        //instantiateMinions
        minionThreads = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            minionThreads.add(new MinionThread("Minion " + (i + 1), i));
        }
        //instantiateGnomes
        gnomeThreads = new ArrayList<>();
        for(int i = 0; i < 7; i++) {
            gnomeThreads.add(new GnomeThread("Gnome " + (i + 1), i));
        }

        //Call start() of each thread
        minionsGoingToWork = new Vector<>();
        gnomesGoingToWork = new Vector<>();
        minionsAtDoor = new LinkedList<>();
        gnomesAtDoor = new LinkedList<>();
        tableList = new LinkedList<>();
        this.getEveryoneMoving();


        AliceThread aliceThread = new AliceThread("Alice");
        aliceThread.start();

        int counter = 0;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(minionsGoingToWork.size() < 10) {
            counter++;
            if(counter % 100000 == 0) {
                System.out.println("still waiting " + "size: " + minionsGoingToWork.size());
            }
            //wait for minions to get ready to go
        }
        System.out.println("got the full minion list");

        for (MinionThread minion : minionsGoingToWork) {
                minionThreads.get(minion.getMinionId()).setAtWork(true);
                System.out.println(minionsGoingToWork.size());
        }


        while(gnomesGoingToWork.size() < 7) {
            counter++;
            if(counter % 100000 == 0) {
                System.out.println("still waiting " + "size: " + gnomesGoingToWork.size());
            }
            //wait for minions to get ready to go
        }
        for (GnomeThread gnome : gnomesGoingToWork) {
                gnomeThreads.get(gnome.getGnomeId()).setAtWork(true);
                System.out.println(gnomesGoingToWork.size());
        }

        aliceThread.setTimeToWakeUpBob(true);

        while(minionsAtDoor.size() < 10) {
            Thread.sleep(30);
        }

        System.out.println("All the minions are at the door!");

        aliceThread.sendMinionsToPlay();

        do {
            Thread.sleep(50);
        } while (gnomesAtDoor.size() < 7);

        aliceThread.sendGnomesToPlay();
        bob.setTimeToComeHome(true);

        aliceThread.setTimeToEat(true);
        int eatingSize = 0;
        while (!everyoneEaten) {
            if (tableList.size() >= 5 || eatingSize >= 15) {
                synchronized (this){
                    eatingSize += clearTable();
                }
            }
            if (eatingSize >= 19) {
                everyoneEaten = true;
            }
        }

        System.out.println("Everyone is finished eating");

        while (childrenSleeping < 17) {
            Thread.sleep(25);
        }

        aliceThread.setReading(true);
        bob.setReading(true);

        while (aliceThread.getReading() || bob.getReading()) {
            Thread.sleep(25);
        }

        System.out.println("Lights out in the living room. Good night everyone!");
    }

    private void getEveryoneMoving() {
        for(int i = 0; i < 7; i++) {
            gnomeThreads.get(i).start();
            System.out.println("Starting gnome thread " + i);
        }
        for(int i = 0; i < 10; i++) {
            minionThreads.get(i).start();
            System.out.println("Starting minion thread " + i);
        }
    }

    private void sendMinionToWork(MinionThread minionThread) {
        minionThread.setAtWork(true);
    }

    private void sendGnomeToWork(GnomeThread gnomeThread) {
        gnomeThread.setAtWork(true);
    }

    public static void wakeUpBob() {
        bob = new BobThread("Bob");
        bob.start();
    }

    public synchronized static void addMinionToDoor(MinionThread minionThread) {
        minionsAtDoor.add(minionThread);
    }

    public synchronized static void addGnomeToDoor(GnomeThread gnomeThread) {
        gnomesAtDoor.add(gnomeThread);
    }

    public synchronized static void attemptToEat(ActorThread thread) {
        System.out.println(thread.getTitleVal() + " is trying to join the table");
        if(tableList.size() < 5) {
            tableList.add(thread);
            thread.setNotAtTable(false);
            System.out.println(thread.getTitleVal() + " has joined the table");
        }
    }

    public int clearTable() {
        int counter = 0;
        while(!tableList.isEmpty()) {
            tableList.removeFirst().setEaten(true);
            counter++;
        }
        return counter;
    }

    public synchronized static void incrementChildrenSleeping() {
        childrenSleeping++;
    }
}
