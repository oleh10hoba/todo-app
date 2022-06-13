//package io.github.mat3e.logic;
//
//import io.github.mat3e.model.TaskGroup;
//import io.github.mat3e.model.TaskGroupRepository;
//import io.github.mat3e.model.TaskRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class TaskGroupServiceTest {
//
//    @Test
//    void createGroup() {
//    }
//
////    @Test
////    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other  undone group exist ")
////    void toggleGroup_existsByDoneIsFalseAndGroup_Id_throwsIllegalStateException() {
////        // given
////        // when
////        // then
////        var mockTaskRepository = mock(TaskRepository.class);
//////        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(1)).thenReturn(true);
////        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);
////
//////        assertTrue(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(1));
//////        assertTrue(mockTaskRepository.findById(1).isPresent());
////
////    }
//}