package hr.zavrsni.trainflowspringbackend.trainingDomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDayDTO {
    Integer id;
    String name;
    String day;
    List<TrainingDayExerciseDTO> exercises;
}
