package hr.zavrsni.trainflowspringbackend.services;

import hr.zavrsni.trainflowspringbackend.authDomain.UserInfo;
import hr.zavrsni.trainflowspringbackend.authServices.UserDetailsServiceImpl;
import hr.zavrsni.trainflowspringbackend.repositories.*;
import hr.zavrsni.trainflowspringbackend.trainingDomain.WorkoutLog;
import hr.zavrsni.trainflowspringbackend.trainingDomain.WorkoutLogDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class WorkoutLogServiceImpl implements WorkoutLogService{
    private WorkoutLogJpaRepository workoutLogJpaRepository;
    private UserDetailsServiceImpl userDetailsService;
    private TrainingJpaRepository trainingJpaRepository;
    private TrainingDayJpaRepository trainingDayJpaRepository;
    private TrainingDayExerciseJpaRepository trainingDayExerciseJpaRepository;
    private ExerciseJpaRepository exerciseJpaRepository;

    @Override
    public void saveWorkoutLogs(WorkoutLogDTO[] workoutLogsDTO) {
        Arrays.stream(workoutLogsDTO)
                .map(this::convertToWorkoutLog)
                .forEach(workoutLogJpaRepository::save);
    }

    @Override
    public List<WorkoutLogDTO> getWorkoutLogsForPlan(Integer planId){
        UserInfo user = userDetailsService.getUserInfo();
        return workoutLogJpaRepository.findByTrainingPlan_IdAndUser_Id(planId, user.getId()).stream().map(this::convertToWorkoutLogDTO).toList();
    }

    @Override
    public void deleteWorkoutLog(Integer workoutLogId) {
        workoutLogJpaRepository.deleteById(workoutLogId);
    }


    private WorkoutLog convertToWorkoutLog(WorkoutLogDTO workoutLogDTO){
        return new WorkoutLog(
                workoutLogDTO.getId(),
                userDetailsService.getUserInfo(),
                trainingJpaRepository.findById(workoutLogDTO.getTrainingPlanId()).orElseThrow(),
                trainingDayJpaRepository.findById(workoutLogDTO.getTrainingDayId()).orElseThrow(),
                trainingDayExerciseJpaRepository.findById(workoutLogDTO.getTrainingDayExerciseId()).orElseThrow(),
                exerciseJpaRepository.findById(workoutLogDTO.getExerciseId()).orElseThrow(),
                workoutLogDTO.getLogDate(),
                workoutLogDTO.getPlannedSets(),
                workoutLogDTO.getPlannedReps(),
                workoutLogDTO.getPlannedWeight(),
                workoutLogDTO.getActualSets(),
                workoutLogDTO.getActualReps(),
                workoutLogDTO.getActualWeight(),
                workoutLogDTO.getNotes()
        );
    }

    private WorkoutLogDTO convertToWorkoutLogDTO(WorkoutLog workoutLog){
        return new WorkoutLogDTO(
                workoutLog.getId(),
                workoutLog.getUser().getId(),
                workoutLog.getTrainingPlan().getId(),
                workoutLog.getTrainingDay().getId(),
                workoutLog.getTrainingDayExercise().getId(),
                workoutLog.getExercise().getId(),
                workoutLog.getLogDate(),
                workoutLog.getPlannedSets(),
                workoutLog.getPlannedReps(),
                workoutLog.getPlannedWeight(),
                workoutLog.getActualSets(),
                workoutLog.getActualReps(),
                workoutLog.getActualWeight(),
                workoutLog.getNotes()
        );
    }
}
