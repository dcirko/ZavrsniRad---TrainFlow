package hr.zavrsni.trainflowspringbackend.controllers;

import hr.zavrsni.trainflowspringbackend.authDomain.UserInfo;
import hr.zavrsni.trainflowspringbackend.authDomain.UserInfoDTO;
import hr.zavrsni.trainflowspringbackend.authServices.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class UserController {

    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/getUserByEmail")
    public UserInfoDTO getUserByEmail() {
        return userDetailsService.getUser();
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody UserInfo userInfo) {
        userDetailsService.saveUser(userInfo);
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/isAdmin")
    public Boolean isAdmin() {
        return userDetailsService.isAdmin();
    }
}
