package hr.zavrsni.trainflowspringbackend.services;

import hr.zavrsni.trainflowspringbackend.trainingDomain.Exercise;
import hr.zavrsni.trainflowspringbackend.trainingDomain.ExerciseDTO;
import hr.zavrsni.trainflowspringbackend.repositories.ExerciseJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ExerciseServiceImpl implements ExerciseService{
    private ExerciseJpaRepository exerciseJpaRepository;
    @Override
    public Optional<ExerciseDTO> getById(Integer id) {
        return exerciseJpaRepository.findById(id).stream().map(this::convertToDTO).findFirst();
    }

    private ExerciseDTO convertToDTO(Exercise exercise){
        return new ExerciseDTO(
                exercise.getId(),
                exercise.getName(),
                exercise.getCategory(),
                exercise.getDescription()
        );
    }
}
