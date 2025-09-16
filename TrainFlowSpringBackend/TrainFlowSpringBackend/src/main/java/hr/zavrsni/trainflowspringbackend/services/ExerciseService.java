package hr.zavrsni.trainflowspringbackend.services;

import hr.zavrsni.trainflowspringbackend.trainingDomain.ExerciseDTO;

import java.util.Optional;

public interface ExerciseService {
    Optional<ExerciseDTO> getById(Integer id);
}
