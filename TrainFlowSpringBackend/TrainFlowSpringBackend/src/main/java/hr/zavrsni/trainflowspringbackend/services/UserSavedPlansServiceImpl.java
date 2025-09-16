package hr.zavrsni.trainflowspringbackend.services;

import hr.zavrsni.trainflowspringbackend.authDomain.UserInfo;
import hr.zavrsni.trainflowspringbackend.authServices.UserDetailsServiceImpl;
import hr.zavrsni.trainflowspringbackend.repositories.UserSavedPlansJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserSavedPlansServiceImpl implements UserSavedPlansService{
    private UserSavedPlansJpaRepository userSavedPlansJpaRepository;
    private UserDetailsServiceImpl userDetailsService;
    @Override
    public Boolean checkIfSavedPlanIsActive(Integer planId) {
        UserInfo user = userDetailsService.getUserInfo();
        if(userSavedPlansJpaRepository.findByPlan_IdAndIsActiveAndUser(planId, true, user).isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}
