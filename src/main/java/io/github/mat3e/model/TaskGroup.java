 package io.github.mat3e.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

 @Entity
@Table(name = "task_groups")
public class TaskGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private int id;
    @NotBlank(message = "Task group's descriptions must be not empty")
    private String description;
    private boolean done;

//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(column = @Column(name="updatedOn"), name = "updatedOn")
//})
//    private Audit autid = new Audit();

     @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Task> tasks;
     @ManyToOne
     @JoinColumn(name = "project_id")
     private Project project;

     public Project getProject() {
         return project;
     }

     public void setProject(Project project) {
         this.project = project;
     }

     public TaskGroup(){

    }
//    @OneToMany(fetch = FetchType.LAZY)


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void updateFrom(final TaskGroup source){
         description =  source.description;
         done = source.done;


    }

    public boolean isDone() {
        return done;
    }

    public  void setDone(boolean done) {
        this.done = done;
    }

     public Set<Task> getTasks() {
         return tasks;
     }

     public void setTasks(Set<Task> tasks) {
         this.tasks = tasks;
     }
 }

