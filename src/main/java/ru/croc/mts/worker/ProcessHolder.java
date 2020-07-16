package ru.croc.mts.worker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

public class ProcessHolder {

    private static Logger logger = LogManager.getLogger(Scheduler.class);

    private Process process;
    private long pid;

    public ProcessHolder(Process process){
        this.process = process;
        findPid();
    }

    /**
     * Hey guys, go get Java9 - it has an API for that!
     */
    private void findPid(){
        try {
            if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
                Field f = process.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = f.getLong(process);
                f.setAccessible(false);
            }
        } catch (Exception e) {
            logger.error("Failed to get PID of the spawned process");
            throw new RuntimeException(e);
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        ProcessHolder.logger = logger;
    }
}
