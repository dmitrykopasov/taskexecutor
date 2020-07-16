package ru.croc.mts.worker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import ru.croc.mts.rest.WebController;

import java.util.HashMap;
import java.util.Map;

@Component
public class Scheduler implements DisposableBean, Runnable {

    private Logger logger = LogManager.getLogger(Scheduler.class);

    private Thread worker;
    private volatile boolean isActive = true;

    private Map<String, ProcessHolder> processMap = new HashMap<>();

    public Scheduler(){
        worker = new Thread(this);
        worker.start();
    }

    public void run(){
        while (isActive) {

        }
    }

    @Override
    public void destroy(){
        isActive = false;
        try {
            worker.join();
        } catch (InterruptedException e) {

        }
    }



}
