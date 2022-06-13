package io.github.mat3e.adapter;

import io.github.mat3e.model.Task;
import io.github.mat3e.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
    @Override

    //lub po kluczu 'id' jako nazwa parametru
    @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id=:id")
    boolean existsById(@Param("id") Integer id);

    //tutaj ?1 chodzi o numer parametru
//    @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id=?1")
//    boolean existsById(Integer id);
    
    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);
}
