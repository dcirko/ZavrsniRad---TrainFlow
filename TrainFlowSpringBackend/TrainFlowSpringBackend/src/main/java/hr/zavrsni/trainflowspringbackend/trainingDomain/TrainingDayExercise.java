package hr.zavrsni.trainflowspringbackend.trainingDomain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "training_day_exercises")
public class TrainingDayExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer sets;
    private Integer reps;
    private Integer restTime;

    @ManyToOne
    @JoinColumn(name = "training_day_id")
    private TrainingDay trainingDay;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;
}

