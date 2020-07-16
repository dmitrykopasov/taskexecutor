package ru.croc.mts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="task")
@JsonIgnoreProperties({ "executable", "status" })
public class Task {

    public static final int DEFAULT_PRIORITY = 1;

    @Id
    @Column(name = "id", length=32)
    private String id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "path")
    private Executable executable;

    @Column(name = "priority")
    private Integer priority;

    /**
     * Planned CPU utilization rate
     */
    @Column(name="cpu")
    private Float cpu;

    /**
     * Planned memory usage, megabytes
     */
    @Column(name="memory")
    private Integer memory;

    /**
     * Planned execution time, seconds
     */
    @Column(name = "exec_time")
    private Integer execTime;

    @Column(name = "created_at", columnDefinition="TIMESTAMP")
    private Date createdAt;

    @Column(name = "status", columnDefinition = "integer not null default 0")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @JsonProperty("path")
    public String getPath(){
        if (executable!=null) {
            return executable.getPath();
        }
        return null;
    }

    @JsonProperty("path")
    public void setPath(String path){
        Executable exec = new Executable();
        exec.setPath(path);
        executable = exec;
    }

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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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

    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}