package io.github.mat3e.logic;

import io.github.mat3e.TaskConfigurationProperties;
import io.github.mat3e.model.Task;
import io.github.mat3e.model.TaskGroup;
import io.github.mat3e.model.TaskGroupRepository;
import io.github.mat3e.model.TaskRepository;
import io.github.mat3e.model.projection.GroupReadModel;
import io.github.mat3e.model.projection.GroupWriteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequestScope
public class TaskGroupService {
//    @Autowired
//    List<String> temp(TaskGroupRepository repository ){
//        // FIXME: N+1
//        return repository.findAll().stream()
//                .flatMap(taskGroup -> taskGroup.getTasks().stream())
//                .map(Task::getDescription)
//                .collect(Collectors.toList());
//    }
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;
//    private TaskConfigurationProperties config;

    TaskGroupService(TaskGroupRepository repository) {
        this.repository = repository;
//        this.config = config;
    }
    TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source){
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll(){
        return repository.findAll()
                .stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId){
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)){
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}
