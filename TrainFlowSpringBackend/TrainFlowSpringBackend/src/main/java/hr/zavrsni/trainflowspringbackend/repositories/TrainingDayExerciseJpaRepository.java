package hr.zavrsni.trainflowspringbackend.repositories;

import hr.zavrsni.trainflowspringbackend.trainingDomain.TrainingDayExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingDayExerciseJpaRepository extends JpaRepository<TrainingDayExercise, Integer> {
    List<TrainingDayExercise> findByTrainingDayId(Integer trainingDayId);
}
