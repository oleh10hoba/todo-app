package io.github.mat3e.logic;

import io.github.mat3e.TaskConfigurationProperties;
import io.github.mat3e.model.*;
import io.github.mat3e.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("ProjectSerticeCreateGroup should throw IllegalStateException when configured to allow just 1 group and the other  undone group exist ")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_throwsIllegalStateExceptions() {
        // given
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);
        // and
        TaskConfigurationProperties mockConfig = confugurationReturning(false);
        // system under test
        var toTest = new ProjectService(null, mockGroupRepository, null, mockConfig);

        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                // chy zawiera "one ..."
                .hasMessageContaining("one undone group");


        //        // when + then
//        assertThatIllegalStateException()
//                .isThrownBy(() -> toTest.createGroup(LocalDateTime.now(), 0));



        // or
        // when + then
//        assertThatExceptionOfType(IllegalStateException.class)
//                .isThrownBy(() -> toTest.createGroup(LocalDateTime.now(), 0));

                        // or
//        // when + then
//        assertThatThrownBy(() -> toTest.createGroup(LocalDateTime.now(), 0))
//                .isInstanceOf(IllegalStateException.class);




        //        try{
//            // when
//            toTest.createGroup(LocalDateTime.now(), 0);
//        }catch(IllegalStateException e){
//            // then
//            assertThat(e).isEqualTo();
        }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given if")
    void createGroup_configurationOk_And_noProjects_throwsArgumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        TaskConfigurationProperties mockConfig = confugurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, null, null, mockConfig);

        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        // then
        assertThat(exception)
//                    .isInstanceOf(NullPointerException.class);
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

   @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOK_existingProject_createsAndSavesGroup(){
        // given
        var today = LocalDate.now().atStartOfDay();
        // and
        var project = projectWith("bar", Set.of(-1, -2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        // and
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository ();
        var serviceWithInMemoryRepo = dummyGroupService(inMemoryGroupRepo);
        int countBeforeCall = inMemoryGroupRepo.count();
        // and
        TaskConfigurationProperties mockConfig = confugurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo, serviceWithInMemoryRepo, mockConfig);

        // when
        GroupReadModel result = toTest.createGroup(today, 1);

        //then
//        assertThat(result)
       assertThat(result.getDescription()).isEqualTo("bar");
       assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
       assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("Test"));
       assertThat(countBeforeCall + 1)
                .isEqualTo(inMemoryGroupRepo.count());
    }

    private TaskGroupService dummyGroupService(InMemoryGroupRepository inMemoryGroupRepo) {
        return new TaskGroupService(inMemoryGroupRepo, null);
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline){
        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("Test");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private TaskGroupRepository groupRepositoryReturning(boolean result){
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties confugurationReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        // and
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository(){
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository{
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count(){
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() == 0){
                try{
                    var field= TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                }catch(NoSuchFieldException | IllegalAccessException e){
                    e.printStackTrace();
                }
            }
            map.put(index, entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .filter(group -> group.getProject() != null && group.getProject().getId() == projectId)
                    .findAny()
                    .isPresent();
        }
    };
}