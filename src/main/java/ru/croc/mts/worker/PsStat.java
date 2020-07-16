package ru.croc.mts.worker;

public class PsStat {
    private double cpu;
    private double memory;
    /**
     * Number of instances merged
     */
    private int count = 1;
    public PsStat(double cpu, double memory) {
        this.cpu = cpu;
        this.memory = memory;
    }

    public void merge(PsStat stat){
        this.cpu+=stat.getCpu();
        this.memory+=stat.getMemory();
        this.count++;
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public double getMemory() {
        return memory;
    }

    public void setMemory(double memory) {
        this.memory = memory;
    }

    public double getAvgCpu(){
        return cpu/count;
    }

    public double getAvgMemory(){
        return memory/count;
    }
}
