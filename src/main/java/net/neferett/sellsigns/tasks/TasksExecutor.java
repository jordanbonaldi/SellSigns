package net.neferett.sellsigns.tasks;

import java.util.concurrent.LinkedBlockingQueue;

public class TasksExecutor implements Runnable {

    private LinkedBlockingQueue<PendingTask> pending = new LinkedBlockingQueue<>();

    public void addTask(PendingTask message) {
        pending.add(message);
    }

    @Override
    public void run() {
        while (true) {
            try {
                pending.take().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
