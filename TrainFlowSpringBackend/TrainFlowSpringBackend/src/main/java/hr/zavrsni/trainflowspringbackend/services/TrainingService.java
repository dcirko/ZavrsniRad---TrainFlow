package hr.zavrsni.trainflowspringbackend.services;


import hr.zavrsni.trainflowspringbackend.trainingDomain.FullTrainingPlanDTO;
import hr.zavrsni.trainflowspringbackend.trainingDomain.TrainingPlanDTO;

import java.util.List;
import java.util.Optional;

public interface TrainingService {
    List<TrainingPlanDTO> getUserTrainingPlans();
    List<TrainingPlanDTO> getSuggestedTrainingPlans();
    Optional<FullTrainingPlanDTO> getFullTrainingPlan(Integer id);
    void addNewTrainingPlan(FullTrainingPlanDTO trainingPlanDTO);
    void deleteTrainingPlan(Integer planId);
    void makePlanActive(Integer planId);
    void makePlanInactive(Integer planId);
    Optional<FullTrainingPlanDTO> getActivePlan();
    void deleteSuggestedPlan(Integer planId);
    void updateTrainingPlan(FullTrainingPlanDTO trainingPlanDTO);
}
