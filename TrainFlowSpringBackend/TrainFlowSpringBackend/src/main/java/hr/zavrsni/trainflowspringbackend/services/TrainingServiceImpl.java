package hr.zavrsni.trainflowspringbackend.services;

import hr.zavrsni.trainflowspringbackend.authDomain.UserInfo;
import hr.zavrsni.trainflowspringbackend.authRepositories.UserRepository;
import hr.zavrsni.trainflowspringbackend.authServices.UserDetailsServiceImpl;
import hr.zavrsni.trainflowspringbackend.repositories.*;
import hr.zavrsni.trainflowspringbackend.trainingDomain.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService{
    private TrainingJpaRepository trainingJpaRepository;
    private ExerciseJpaRepository exerciseJpaRepository;
    private TrainingDayJpaRepository trainingDayJpaRepository;
    private TrainingDayExerciseJpaRepository trainingDayExerciseJpaRepository;
    private UserRepository repository;
    private UserDetailsServiceImpl userDetailsService;;
    private UserSavedPlansJpaRepository userSavedPlansJpaRepository;

    @Override
    public List<TrainingPlanDTO> getUserTrainingPlans() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo user = repository.findByEmail(userDetails.getUsername());
        return trainingJpaRepository.findTrainingPlansByUser_Id(user.getId()).stream().map(this::convertToDTO).toList();
    }

    @Override
    public List<TrainingPlanDTO> getSuggestedTrainingPlans() {
        return trainingJpaRepository.findByIsSuggested(true).stream().map(this::convertToDTO).toList();
    }


    @Override
    public Optional<FullTrainingPlanDTO> getFullTrainingPlan(Integer planId){
        TrainingPlan plan = trainingJpaRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Training plan not found"));

        List<TrainingDay> days = trainingDayJpaRepository.findByPlan_Id(planId);

        return getFullTrainingPlanDTOMethod(plan, days);
    }

    @Override
    public void addNewTrainingPlan(FullTrainingPlanDTO fullTrainingPlanDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo user = repository.findByEmail(userDetails.getUsername());
        String name = fullTrainingPlanDTO.getName();
        String description = fullTrainingPlanDTO.getDescription();
        String goal = fullTrainingPlanDTO.getGoal();
        String difficulty = fullTrainingPlanDTO.getDifficulty();
        Boolean isSuggested = fullTrainingPlanDTO.getIsSuggested();
        Boolean isActive = fullTrainingPlanDTO.getIsActive();

        TrainingPlan trainingPlan = new TrainingPlan();
        if(userDetailsService.isAdmin()){
            UserInfo admin = repository.findById(9999).orElseThrow(() -> new RuntimeException("Admin user not found"));
            trainingPlan.setUser(admin);
        }else {
            trainingPlan.setUser(user);
        }
        trainingPlan.setName(name);
        trainingPlan.setDescription(description);
        trainingPlan.setGoal(goal);
        trainingPlan.setDifficulty(difficulty);
        trainingPlan.setIsSuggested(isSuggested);
        trainingPlan.setIsActive(isActive);

        saveTrainingDaysMethod(fullTrainingPlanDTO, trainingPlan);
        ResponseEntity.ok().build();
    }

    @Override
    public void deleteTrainingPlan(Integer planId) {
        trainingJpaRepository.deleteById(planId);
        ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public void makePlanActive(Integer planId) {
        UserInfo user = userDetailsService.getUserInfo();
        if (!trainingJpaRepository.findTrainingPlansByIsActiveAndUserEquals(true, user).isEmpty()) {
            throw new IllegalStateException("There is already an active training plan");
        }else if(userSavedPlansJpaRepository.findByUserAndIsActive( user,true).isPresent()){
            throw new IllegalStateException("There is already an active suggested training plan");
        }

        TrainingPlan trainingPlan = trainingJpaRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Training plan not found"));

        if(trainingPlan.getIsSuggested().equals(true)){
            UserSavedPlans userSavedPlan = userSavedPlansJpaRepository.findByUserAndPlan_Id(user, trainingPlan.getId());
            userSavedPlan.setIsActive(true);
            userSavedPlansJpaRepository.save(userSavedPlan);
        }else{
            trainingPlan.setIsActive(true);
            trainingJpaRepository.save(trainingPlan);
        }


    }

    @Override
    @Transactional
    public void makePlanInactive(Integer planId) {
        UserInfo user = userDetailsService.getUserInfo();
        TrainingPlan trainingPlan = trainingJpaRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Training plan not found"));

        if(trainingPlan.getIsSuggested().equals(true)){
            UserSavedPlans userSavedPlan = userSavedPlansJpaRepository.findByUserAndPlan_Id(user, trainingPlan.getId());
            userSavedPlan.setIsActive(false);
            userSavedPlansJpaRepository.save(userSavedPlan);
        }else{
            trainingPlan.setIsActive(false);
            trainingJpaRepository.save(trainingPlan);
        }
    }


    @Override
    public Optional<FullTrainingPlanDTO> getActivePlan() {
        UserInfo user = userDetailsService.getUserInfo();
        List<TrainingPlan> trainingPlans = trainingJpaRepository.findTrainingPlansByIsActiveAndUserEquals(true, user);

        if (trainingPlans.isEmpty()) {
            UserSavedPlans savedPlan = userSavedPlansJpaRepository.findByUserAndIsActive(user, true).orElseThrow(() -> new IllegalStateException("There is no active training plan"));
            Optional<TrainingPlan> trainingPlan = trainingJpaRepository.findById(savedPlan.getPlan().getId());
            List<TrainingDay> days = trainingDayJpaRepository.findByPlan_Id(trainingPlan.get().getId());
            return getFullTrainingPlanDTOMethod(trainingPlan.get(), days);
        }else {
            Optional<TrainingPlan> trainingPlan = trainingPlans.stream().findFirst();
            List<TrainingDay> days = trainingDayJpaRepository.findByPlan_Id(trainingPlan.get().getId());
            return getFullTrainingPlanDTOMethod(trainingPlan.get(), days);
        }
    }

    @Override
    public void deleteSuggestedPlan(Integer planId) {
        trainingJpaRepository.deleteById(planId);
        ResponseEntity.ok().build();
    }

    @Transactional
    @Override
    public void updateTrainingPlan(FullTrainingPlanDTO updatedTrainingPlanDTO) {
        TrainingPlan trainingPlan = trainingJpaRepository.findById(updatedTrainingPlanDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Training plan not found"));
        UserInfo user = trainingPlan.getUser();
        trainingPlan.setName(updatedTrainingPlanDTO.getName());
        trainingPlan.setDescription(updatedTrainingPlanDTO.getDescription());
        trainingPlan.setGoal(updatedTrainingPlanDTO.getGoal());
        trainingPlan.setDifficulty(updatedTrainingPlanDTO.getDifficulty());
        trainingPlan.setIsSuggested(updatedTrainingPlanDTO.getIsSuggested());
        trainingPlan.setIsActive(updatedTrainingPlanDTO.getIsActive());

        if(userDetailsService.isAdmin() && trainingPlan.getIsSuggested().equals(true)){
            UserInfo admin = repository.findById(9999).orElseThrow(() -> new RuntimeException("Admin user not found"));
            trainingPlan.setUser(admin);
        }else {
            trainingPlan.setUser(user);
        }

        trainingPlan.getTrainingDays().clear();
        trainingPlan.getTrainingDays().addAll(buildTrainingDays(updatedTrainingPlanDTO, trainingPlan));

        //saveTrainingDaysMethod(updatedTrainingPlanDTO, trainingPlan);
        ResponseEntity.ok().build();
    }

    private List<TrainingDay> buildTrainingDays(FullTrainingPlanDTO updatedTrainingPlanDTO, TrainingPlan trainingPlan) {
        return updatedTrainingPlanDTO.getDays().stream().map(trainingDayDTO -> {
            TrainingDay trainingDay = new TrainingDay();
            trainingDay.setName(trainingDayDTO.getName());
            trainingDay.setDay(DayOfWeekEnum.valueOf(trainingDayDTO.getDay()));
            trainingDay.setPlan(trainingPlan);

            List<TrainingDayExercise> exercises = trainingDayDTO.getExercises().stream().map(trainingDayExerciseDTO -> {
                TrainingDayExercise trainingDayExercise = new TrainingDayExercise();
                trainingDayExercise.setSets(trainingDayExerciseDTO.getSets());
                trainingDayExercise.setReps(trainingDayExerciseDTO.getReps());
                trainingDayExercise.setRestTime(trainingDayExerciseDTO.getRestTime());
                trainingDayExercise.setTrainingDay(trainingDay);

                Exercise exercise;
                if (exerciseJpaRepository.findExerciseByName(trainingDayExerciseDTO.getExerciseName()).isEmpty()) {
                    exercise = new Exercise();
                    exercise.setName(trainingDayExerciseDTO.getExerciseName());
                    exercise.setCategory(ExerciseCategory.valueOf(trainingDayExerciseDTO.getCategory()));
                    exercise.setDescription(trainingDayExerciseDTO.getDescription());
                    exercise = exerciseJpaRepository.save(exercise);
                } else {
                    exercise = exerciseJpaRepository.findExerciseByName(trainingDayExerciseDTO.getExerciseName()).getFirst();
                }

                trainingDayExercise.setExercise(exercise);
                return trainingDayExercise;
            }).toList();

            trainingDay.setExercises(exercises);
            return trainingDay;
        }).toList();
    }


    private void saveTrainingDaysMethod(FullTrainingPlanDTO updatedTrainingPlanDTO, TrainingPlan trainingPlan) {
        List<TrainingDay> days = updatedTrainingPlanDTO.getDays().stream().map(trainingDayDTO ->{
            TrainingDay trainingDay = new TrainingDay();
            trainingDay.setName(trainingDayDTO.getName());
            trainingDay.setDay(DayOfWeekEnum.valueOf(trainingDayDTO.getDay()));
            trainingDay.setPlan(trainingPlan);
            trainingDay.setExercises(trainingDayDTO.getExercises().stream().map(trainingDayExerciseDTO -> {
                TrainingDayExercise trainingDayExercise = new TrainingDayExercise();
                trainingDayExercise.setSets(trainingDayExerciseDTO.getSets());
                trainingDayExercise.setReps(trainingDayExerciseDTO.getReps());
                trainingDayExercise.setRestTime(trainingDayExerciseDTO.getRestTime());
                trainingDayExercise.setTrainingDay(trainingDay);

                Exercise exercise;
                if (exerciseJpaRepository.findExerciseByName(trainingDayExerciseDTO.getExerciseName()).isEmpty()) {
                    exercise = new Exercise();
                    exercise.setName(trainingDayExerciseDTO.getExerciseName());
                    exercise.setCategory(ExerciseCategory.valueOf(trainingDayExerciseDTO.getCategory()));
                    exercise.setDescription(trainingDayExerciseDTO.getDescription());

                    exercise = exerciseJpaRepository.save(exercise);
                } else {
                    exercise = exerciseJpaRepository.findExerciseByName(trainingDayExerciseDTO.getExerciseName()).getFirst(); // koristi postojeći
                }

                trainingDayExercise.setExercise(exercise);

                return trainingDayExercise;
            }).collect(Collectors.toList()));
            return trainingDay;
        }).collect(Collectors.toList());

        trainingPlan.setTrainingDays(days);
        trainingJpaRepository.save(trainingPlan);
    }


    private Optional<FullTrainingPlanDTO> getFullTrainingPlanDTOMethod(TrainingPlan trainingPlan, List<TrainingDay> days) {
        List<TrainingDayDTO> dayDTOs = days.stream().map(day -> {
            List<TrainingDayExercise> tdExercises = trainingDayExerciseJpaRepository.findByTrainingDayId(day.getId());

            List<TrainingDayExerciseDTO> exerciseDTOs = tdExercises.stream().map(tde -> {
                Exercise ex = tde.getExercise();
                return new TrainingDayExerciseDTO(
                        tde.getId(),
                        ex.getId(),
                        ex.getName(),
                        ex.getCategory().toString(),
                        ex.getDescription(),
                        tde.getSets(),
                        tde.getReps(),
                        tde.getRestTime()
                );
            }).toList();

            return new TrainingDayDTO(
                    day.getId(),
                    day.getName(),
                    day.getDay().name(),
                    exerciseDTOs
            );
        }).toList();

        return Optional.of(new FullTrainingPlanDTO(
                trainingPlan.getId(),
                trainingPlan.getName(),
                trainingPlan.getDescription(),
                trainingPlan.getGoal(),
                trainingPlan.getDifficulty(),
                trainingPlan.getIsSuggested(),
                trainingPlan.getIsActive(),
                dayDTOs
        ));
    }


    private TrainingPlanDTO convertToDTO(TrainingPlan trainingPlan){
        return new TrainingPlanDTO(
                trainingPlan.getId(),
                trainingPlan.getUser().getId(),
                trainingPlan.getName(),
                trainingPlan.getDescription(),
                trainingPlan.getGoal(),
                trainingPlan.getDifficulty(),
                trainingPlan.getIsSuggested(),
                trainingPlan.getIsActive(),
                trainingPlan.getTrainingDays().stream().map(this::convertToDayDto).toList()
        );
    }

    private TrainingDayDTO convertToDayDto(TrainingDay trainingDay){
        return new TrainingDayDTO(
                trainingDay.getId(),
                trainingDay.getName(),
                trainingDay.getDay().toString(),
                trainingDay.getExercises().stream().map(this::convertToExerciseDto).toList()
        );
    }

    private TrainingDayExerciseDTO convertToExerciseDto(TrainingDayExercise trainingDayExercise){
        return new TrainingDayExerciseDTO(
                trainingDayExercise.getId(),
                trainingDayExercise.getExercise().getId(),
                trainingDayExercise.getExercise().getName(),
                trainingDayExercise.getExercise().getCategory().toString(),
                trainingDayExercise.getExercise().getDescription(),
                trainingDayExercise.getSets(),
                trainingDayExercise.getReps(),
                trainingDayExercise.getRestTime()
        );
    }
}
