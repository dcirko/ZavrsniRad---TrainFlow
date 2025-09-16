package hr.zavrsni.trainflowspringbackend.trainingDomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullTrainingPlanDTO {
    Integer id;
    String name;
    String description;
    String goal;
    String difficulty;
    Boolean isSuggested;
    Boolean isActive;
    List<TrainingDayDTO> days;
}
