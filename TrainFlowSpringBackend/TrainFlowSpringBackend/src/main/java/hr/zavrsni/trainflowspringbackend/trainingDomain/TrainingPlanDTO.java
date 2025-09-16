package hr.zavrsni.trainflowspringbackend.trainingDomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingPlanDTO {
    private Integer id;
    private Integer userId;
    private String name;
    private String description;
    private String goal;
    private String difficulty;
    private Boolean isSuggested;
    private Boolean isActive;
    private List<TrainingDayDTO> trainingDays;
    //private List<UserInfo> usersSaved;
}
