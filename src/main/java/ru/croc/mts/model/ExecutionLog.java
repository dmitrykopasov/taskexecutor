package ru.croc.mts.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="execution_log")
public class ExecutionLog {

    @Id
    @Column(name = "id", length=32)
    private String id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "path")
    private Executable executable;

    /**
     * Actual CPU utilization rate, 0 - 100
     */
    @Column(name="cpu")
    private Double cpu;

    /**
     * Actual memory usage, 0 - 100
     */
    @Column(name="memory")
    private Double memory;

    /**
     * Actual execution time, seconds
     */
    @Column(name = "exec_time")
    private Integer execTime;

    @Column(name = "created_at")
    private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Executable getExecutable() {
        return executable;
    }

    public void setExecutable(Executable executable) {
        this.executable = executable;
    }

    public Double getCpu() {
        return cpu;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    public Double getMemory() {
        return memory;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }

    public Integer getExecTime() {
        return execTime;
    }

    public void setExecTime(Integer execTime) {
        this.execTime = execTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
