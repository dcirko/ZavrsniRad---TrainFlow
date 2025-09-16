package hr.zavrsni.trainflowspringbackend.repositories;

import hr.zavrsni.trainflowspringbackend.authDomain.UserInfo;
import hr.zavrsni.trainflowspringbackend.trainingDomain.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainingJpaRepository extends JpaRepository<TrainingPlan, Integer> {
    List<TrainingPlan> findByIsSuggested(Boolean isSuggested);
    List<TrainingPlan> findTrainingPlansByUser_Id(Integer userId);
    List<TrainingPlan> findTrainingPlansByIsActiveAndUserEquals(Boolean isActive, UserInfo user);
    List<TrainingPlan> findTrainingPlansByIsActiveAndIsSuggested(Boolean isActive, Boolean isSuggested);
    Optional<TrainingPlan> findByIsActive(Boolean isActive);
}
