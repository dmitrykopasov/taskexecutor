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
     * Actual CPU utilization rate
     */
    @Column(name="cpu")
    private Float cpu;

    /**
     * Actual memory usage, megabytes
     */
    @Column(name="memory")
    private Integer memory;

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

    public Float getCpu() {
        return cpu;
    }

    public void setCpu(Float cpu) {
        this.cpu = cpu;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
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