package hr.zavrsni.trainflowspringbackend.services;

import hr.zavrsni.trainflowspringbackend.trainingDomain.WorkoutLogDTO;

import java.util.List;

public interface WorkoutLogService {
    void saveWorkoutLogs(WorkoutLogDTO[] workoutLogsDTO);
    List<WorkoutLogDTO> getWorkoutLogsForPlan(Integer planId);
    void deleteWorkoutLog(Integer workoutLogId);
}
