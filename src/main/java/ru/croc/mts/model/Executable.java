package ru.croc.mts.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="executable")
public class Executable {

    @Id
    @Column(name = "path")
    private String path;

    /**
     * Average CPU utilization rate, 0 - 100
     */
    @Column(name = "avg_cpu")
    private Double avgCpu;

    /**
     * Average memory utilization rate, 0 - 100
     */
    @Column(name = "avg_memory")
    private Double avgMemory;

    @Column(name = "avg_duration")
    private Integer avgDuration;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Double getAvgCpu() {
        return avgCpu;
    }

    public void setAvgCpu(Double avgCpu) {
        this.avgCpu = avgCpu;
    }

    public Double getAvgMemory() {
        return avgMemory;
    }

    public void setAvgMemory(Double avgMemory) {
        this.avgMemory = avgMemory;
    }

    public Integer getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Integer avgDuration) {
        this.avgDuration = avgDuration;
    }
}
