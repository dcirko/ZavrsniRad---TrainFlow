package hr.zavrsni.trainflowspringbackend.services;

import hr.zavrsni.trainflowspringbackend.repositories.TrainingDayJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TrainingDayServiceImpl implements TrainingDayService{
    private TrainingDayJpaRepository trainingDayJpaRepository;

}
