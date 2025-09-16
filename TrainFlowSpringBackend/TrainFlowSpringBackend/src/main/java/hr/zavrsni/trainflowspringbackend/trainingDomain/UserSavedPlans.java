package hr.zavrsni.trainflowspringbackend.trainingDomain;

import hr.zavrsni.trainflowspringbackend.authDomain.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_saved_plans")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSavedPlans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;
    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private TrainingPlan plan;
    private LocalDateTime savedAt = LocalDateTime.now();
    private Boolean isActive = false;

}
