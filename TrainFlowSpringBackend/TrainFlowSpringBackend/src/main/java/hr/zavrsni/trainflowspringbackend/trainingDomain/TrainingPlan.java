package hr.zavrsni.trainflowspringbackend.trainingDomain;

import hr.zavrsni.trainflowspringbackend.authDomain.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "training_plans")
public class TrainingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String goal;

    @Column(nullable = false)
    private String difficulty;

    @Column(name = "is_suggested", nullable = false)
    private Boolean isSuggested;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingDay> trainingDays;

    @ManyToMany(mappedBy = "savedPlans")
    private List<UserInfo> usersSaved = new ArrayList<>();
}
