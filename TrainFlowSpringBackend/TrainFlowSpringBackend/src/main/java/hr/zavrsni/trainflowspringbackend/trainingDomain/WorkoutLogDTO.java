package hr.zavrsni.trainflowspringbackend.trainingDomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutLogDTO {
    private Integer id;
    private Integer userId;
    private Integer trainingPlanId;
    private Integer trainingDayId;
    private Integer trainingDayExerciseId;
    private Integer exerciseId;
    private LocalDate logDate;
    private Integer plannedSets;
    private Integer plannedReps;
    private Integer plannedWeight;
    private Integer actualSets;
    private Integer actualReps;
    private Integer actualWeight;
    private String notes;
}
