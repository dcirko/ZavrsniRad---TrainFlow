package hr.zavrsni.trainflowspringbackend.trainingDomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDTO {
    private Integer id;
    private String name;
    private ExerciseCategory category;
    private String description;
}
