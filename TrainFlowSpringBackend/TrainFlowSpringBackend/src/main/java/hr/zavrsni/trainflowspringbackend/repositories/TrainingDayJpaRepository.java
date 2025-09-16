package hr.zavrsni.trainflowspringbackend.repositories;

import hr.zavrsni.trainflowspringbackend.trainingDomain.TrainingDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TrainingDayJpaRepository extends JpaRepository<TrainingDay, Integer> {
    List<TrainingDay> findByPlan_Id(Integer planId);
}
