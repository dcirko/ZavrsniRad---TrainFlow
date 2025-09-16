package hr.zavrsni.trainflowspringbackend.trainingDomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDayExerciseDTO {
    Integer id;
    Integer exerciseId;
    String exerciseName;
    String category;
    String description;
    Integer sets;
    Integer reps;
    Integer restTime;
}
