package io.github.mat3e.logic;

import io.github.mat3e.model.Task;
import io.github.mat3e.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskServise {
    private static final Logger logger = LoggerFactory.getLogger(TaskServise.class);
    private final TaskRepository repository;

    public TaskServise(TaskRepository repository) {
        this.repository = repository;
    }

    @Async
    public CompletableFuture<List<Task>> findAllAsync(){
        logger.info("Supply Async ");
        return CompletableFuture.supplyAsync(repository::findAll);
    }


}
