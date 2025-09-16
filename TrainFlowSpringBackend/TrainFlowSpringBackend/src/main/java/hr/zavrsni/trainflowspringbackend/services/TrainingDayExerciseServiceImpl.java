package hr.zavrsni.trainflowspringbackend.services;

import hr.zavrsni.trainflowspringbackend.repositories.TrainingDayExerciseJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TrainingDayExerciseServiceImpl implements TrainingDayExerciseService{
    private TrainingDayExerciseJpaRepository trainingDayExerciseJpaRepository;


}
