package hr.zavrsni.trainflowspringbackend.authServices;


import hr.zavrsni.trainflowspringbackend.authDomain.RolesUser;
import hr.zavrsni.trainflowspringbackend.authDomain.UserInfo;
import hr.zavrsni.trainflowspringbackend.authDomain.UserInfoDTO;
import hr.zavrsni.trainflowspringbackend.authRepositories.UserRepository;
import hr.zavrsni.trainflowspringbackend.repositories.TrainingJpaRepository;
import hr.zavrsni.trainflowspringbackend.repositories.UserSavedPlansJpaRepository;
import hr.zavrsni.trainflowspringbackend.trainingDomain.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TrainingJpaRepository trainingJpaRepository;
    @Autowired
    private UserSavedPlansJpaRepository userSavedPlansJpaRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserInfo user = this.repository.findByEmail(email);

        if(user == null) {
            throw new UsernameNotFoundException("Unknown user " + email);
        }

        Set<RolesUser> userRoles =  user.getRoles();

        String[] authorities = userRoles.stream()
                .map(RolesUser::getName)
                .toArray(String[]::new);

        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();

    }

    public Boolean isAdmin(){
        UserInfo user = this.getUserInfo();
        return user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    public void saveUser(UserInfo user){
        this.repository.save(user);
    }

    public UserInfoDTO getUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo user = repository.findByEmail(userDetails.getUsername());
        return this.convertToUserDTO(user);
    }

    public UserInfo getUserInfo(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo user = repository.findByEmail(userDetails.getUsername());
        return user;
    }

    @Transactional
    public void savePlanForUser(Integer planId) {
        TrainingPlan trainingPlan = trainingJpaRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo user = repository.findByEmail(userDetails.getUsername());

        /*List<TrainingPlan> copy = new ArrayList<>(user.getSavedPlans());
        copy.add(trainingPlan);
        user.setSavedPlans(copy);
        repository.save(user);*/

        UserSavedPlans userSavedPlans = new UserSavedPlans();
        userSavedPlans.setUser(user);
        userSavedPlans.setPlan(trainingPlan);
        userSavedPlans.setSavedAt(LocalDateTime.now());
        userSavedPlans.setIsActive(false);

        userSavedPlansJpaRepository.save(userSavedPlans);
    }

    @Transactional
    public void removeSavedPlan(Integer planId){
        TrainingPlan trainingPlan = trainingJpaRepository.findById(planId).orElseThrow(() -> new RuntimeException("Plan not found"));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo user = repository.findByEmail(userDetails.getUsername());

        /*List<TrainingPlan> copy = new ArrayList<>(user.getSavedPlans());
        copy.remove(trainingPlan);
        user.setSavedPlans(copy);
        repository.save(user);*/

        userSavedPlansJpaRepository.deleteByPlanIdAndUserId(trainingPlan.getId(), user.getId());
    }


    public List<TrainingPlanDTO> getSavedPlans(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo user = repository.findByEmail(userDetails.getUsername());
        return user.getSavedPlans().stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    @Transactional
    public Boolean checkIfUserHasPlan(Integer planId) {
        TrainingPlan trainingPlan = trainingJpaRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //UserInfo user = repository.findByEmailWithPlans(userDetails.getUsername());
        UserInfo user = repository.findByEmail(userDetails.getUsername());

        return user.getSavedPlans().contains(trainingPlan);
    }

    public boolean checkForExistingEmail(String email){
        return this.repository.findByEmail(email) != null;
    }


    private UserInfoDTO convertToUserDTO(UserInfo user){
        return new UserInfoDTO(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPassword(),
                user.getAge(),
                user.getHeight(),
                user.getWeight(),
                user.getGender(),
                user.getRoles()
        );
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
