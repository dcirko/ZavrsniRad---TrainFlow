package hr.zavrsni.trainflowspringbackend.repositories;

import hr.zavrsni.trainflowspringbackend.trainingDomain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseJpaRepository extends JpaRepository<Exercise, Integer> {
    Optional<Exercise> findById(Integer integer);

    List<Exercise> findExerciseByName(String name);
}
