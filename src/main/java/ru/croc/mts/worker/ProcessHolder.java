package ru.croc.mts.worker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Date;

public class ProcessHolder {

    private static Logger logger = LogManager.getLogger(Scheduler.class);

    private String execPath;
    private Process process;
    private long pid;
    private long start;

    private PsStat summaryStat;

    public ProcessHolder(String execPath, Process process){
        this.execPath = execPath;
        this.process = process;
        this.start = new Date().getTime();
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

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getExecPath() {
        return execPath;
    }

    public void setExecPath(String execPath) {
        this.execPath = execPath;
    }

    public PsStat getSummaryStat() {
        return summaryStat;
    }

    public void setSummaryStat(PsStat summaryStat) {
        this.summaryStat = summaryStat;
    }

    /**
     * Duration in seconds
     * @return
     */
    public int getDuration(){
        return Math.round((new Date().getTime() - start)/1000);
    }
}
