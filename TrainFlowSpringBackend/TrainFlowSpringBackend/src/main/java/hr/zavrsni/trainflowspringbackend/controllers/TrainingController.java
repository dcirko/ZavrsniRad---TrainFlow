package hr.zavrsni.trainflowspringbackend.controllers;

import hr.zavrsni.trainflowspringbackend.authServices.UserDetailsServiceImpl;
import hr.zavrsni.trainflowspringbackend.services.ExerciseService;
import hr.zavrsni.trainflowspringbackend.services.UserSavedPlansService;
import hr.zavrsni.trainflowspringbackend.services.WorkoutLogService;
import hr.zavrsni.trainflowspringbackend.trainingDomain.ExerciseDTO;
import hr.zavrsni.trainflowspringbackend.trainingDomain.FullTrainingPlanDTO;
import hr.zavrsni.trainflowspringbackend.trainingDomain.TrainingPlanDTO;
import hr.zavrsni.trainflowspringbackend.services.TrainingService;
import hr.zavrsni.trainflowspringbackend.trainingDomain.WorkoutLogDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/training")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TrainingController {
    private TrainingService trainingService;
    private UserDetailsServiceImpl userDetailsService;
    private WorkoutLogService workoutLogService;
    private ExerciseService exerciseService;
    private UserSavedPlansService userSavedPlansService;

    @GetMapping("/getSuggestedPlans")
    public List<TrainingPlanDTO> getSuggestedPlans() {
        return trainingService.getSuggestedTrainingPlans();
    }

    @GetMapping("/getFullTrainingPlan/{id}")
    public FullTrainingPlanDTO getFullTrainingPlan(@PathVariable Integer id){
        return trainingService.getFullTrainingPlan(id).orElseThrow(() -> new RuntimeException("Training plan not found"));
    }

    @GetMapping("/getUserTrainingPlans")
    public List<TrainingPlanDTO> getUserTrainingPlans(){
        return trainingService.getUserTrainingPlans();
    }

    @PostMapping("/savePlanForUser")
    public ResponseEntity<?> savePlanForUser(@RequestBody Map<String, Integer> body){
        Integer planId = body.get("planId");
        userDetailsService.savePlanForUser(planId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteSavedPlan/{planId}")
    public ResponseEntity<?> deleteSavedPlan(@PathVariable Integer planId){
        userDetailsService.removeSavedPlan(planId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/checkIfAlreadySavedPlan/{planId}")
    public Boolean checkIfAlreadySavedPlan(@PathVariable Integer planId){
        return userDetailsService.checkIfUserHasPlan(planId);
    }

    @GetMapping("/checkIfSavedPlanIsActive/{planId}")
    public Boolean checkIfSavedPlanIsActive(@PathVariable Integer planId){
        return userSavedPlansService.checkIfSavedPlanIsActive(planId);
    }

    @GetMapping("/getSavedPlansForUser")
    public List<TrainingPlanDTO> getSavedPlansForUser(){
        return userDetailsService.getSavedPlans();
    }


    @PostMapping("/addNewTrainingPlan")
    public ResponseEntity<?> addNewTrainingPlan(@RequestBody FullTrainingPlanDTO trainingPlanDTO){
        System.out.println("Received plan: " + trainingPlanDTO);
        System.out.println("Days: " + trainingPlanDTO.getDays());

        trainingService.addNewTrainingPlan(trainingPlanDTO);
        return ResponseEntity.ok(trainingPlanDTO);
    }

    @DeleteMapping("/deleteTrainingPlan/{planId}")
    public ResponseEntity<?> deleteTrainingPlan(@PathVariable Integer planId){
        trainingService.deleteTrainingPlan(planId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/makePlanActive")
    public ResponseEntity<?> makePlanActive(@RequestBody Map<String, Integer> body){
        Integer planId = body.get("planid");
        trainingService.makePlanActive(planId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/makePlanInactive")
    public ResponseEntity<?> makePlanInactive(@RequestBody Map<String, Integer> body){
        Integer planId = body.get("planid");
        trainingService.makePlanInactive(planId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getActivePlan")
    public FullTrainingPlanDTO getActivePlan(){
        return trainingService.getActivePlan().orElseThrow(() -> new RuntimeException("No active plan found"));
    }

    @PostMapping("/saveWorkoutLogs")
    public ResponseEntity<?> saveWorkoutLogs(@RequestBody WorkoutLogDTO[] workoutLogsDTO){
        workoutLogService.saveWorkoutLogs(workoutLogsDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getWorkoutLogsForPlan/{planId}")
    public List<WorkoutLogDTO> getWorkoutLogsForPlan(@PathVariable Integer planId){
        return workoutLogService.getWorkoutLogsForPlan(planId);
    }

    @GetMapping("/getExerciseById/{id}")
    public ExerciseDTO getExerciseById(@PathVariable Integer id){
        return exerciseService.getById(id).orElseThrow(() -> new RuntimeException("Exercise not found"));
    }

    @DeleteMapping("/deleteLog/{logId}")
    public ResponseEntity<?> deleteLog(@PathVariable Integer logId){
        workoutLogService.deleteWorkoutLog(logId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteSuggestedPlan/{planId}")
    public ResponseEntity<?> deleteSuggestedPlan(@PathVariable Integer planId){
        trainingService.deleteSuggestedPlan(planId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateTrainingPlan")
    public ResponseEntity<?> updateTrainingPlan(@RequestBody FullTrainingPlanDTO trainingPlanDTO){
        trainingService.updateTrainingPlan(trainingPlanDTO);
        return ResponseEntity.ok(trainingPlanDTO);
    }
}
