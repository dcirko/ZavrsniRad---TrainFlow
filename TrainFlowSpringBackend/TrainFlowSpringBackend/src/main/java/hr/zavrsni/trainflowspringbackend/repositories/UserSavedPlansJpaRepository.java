package hr.zavrsni.trainflowspringbackend.repositories;

import hr.zavrsni.trainflowspringbackend.authDomain.UserInfo;
import hr.zavrsni.trainflowspringbackend.trainingDomain.UserSavedPlans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSavedPlansJpaRepository extends JpaRepository<UserSavedPlans, Integer> {
    void deleteByPlanIdAndUserId(Integer planId, Integer userId);

    List<UserSavedPlans> findByPlan_IdAndIsActiveAndUser(Integer planId, Boolean isActive, UserInfo user);

    UserSavedPlans findByUserAndPlan_Id(UserInfo user, Integer planId);

    Optional<UserSavedPlans> findByUserAndIsActive(UserInfo user, Boolean isActive);
}
