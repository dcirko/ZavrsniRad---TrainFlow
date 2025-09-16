package hr.zavrsni.trainflowspringbackend.repositories;

import hr.zavrsni.trainflowspringbackend.trainingDomain.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WorkoutLogJpaRepository extends JpaRepository<WorkoutLog, Integer> {
    List<WorkoutLog> findByTrainingPlan_IdAndUser_Id(Integer trainingPlanId, Integer userId);
}
