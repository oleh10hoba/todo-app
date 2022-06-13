package io.github.mat3e.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private int id;
    @NotBlank(message = "Tasks descriptions must be not empty")
    private String description;
    private boolean done;
    @Column()
    private LocalDateTime deadline;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(column = @Column(name="updatedOn"), name = "updatedOn")
})
    private Audit autid = new Audit();
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    Task(){

    }

    public Task(String description, LocalDateTime deadline){
        this(description, deadline, null);
    }

    public Task(String description, LocalDateTime deadline, TaskGroup group){
        this.description = description;
        this.deadline = deadline;
        if(group != null){
            this.group = group;
        }
    }

    public int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

     void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public TaskGroup getGroup() {
        return group;
    }

    public void setGroup(TaskGroup group) {
        this.group = group;
    }

    public void updateFrom(final Task source){
         description =  source.description;
         done = source.done;
         deadline = source.deadline;
         group = source.group;
    }

    public boolean isDone() {
        return done;
    }

    public  void setDone(boolean done) {
        this.done = done;
    }

}

