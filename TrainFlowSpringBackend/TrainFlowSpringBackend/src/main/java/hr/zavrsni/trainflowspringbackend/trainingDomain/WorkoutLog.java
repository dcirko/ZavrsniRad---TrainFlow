package hr.zavrsni.trainflowspringbackend.trainingDomain;

import hr.zavrsni.trainflowspringbackend.authDomain.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "workout_logs")
public class WorkoutLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_plan_id", nullable = false)
    private TrainingPlan trainingPlan;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_day_id", nullable = false)
    private TrainingDay trainingDay;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_day_exercise_id", nullable = false)
    private TrainingDayExercise trainingDayExercise;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;
    private LocalDate logDate;
    private Integer plannedSets;
    private Integer plannedReps;
    private Integer plannedWeight;
    private Integer actualSets;
    private Integer actualReps;
    private Integer actualWeight;
    private String notes;
}
