package hr.zavrsni.trainflowspringbackend.controllers;


import hr.zavrsni.trainflowspringbackend.authDomain.*;
import hr.zavrsni.trainflowspringbackend.authRepositories.RefreshTokenRepository;
import hr.zavrsni.trainflowspringbackend.authServices.JwtService;
import hr.zavrsni.trainflowspringbackend.authServices.RefreshTokenService;
import hr.zavrsni.trainflowspringbackend.authServices.RolesServiceImpl;
import hr.zavrsni.trainflowspringbackend.authServices.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class AuthController {
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    private JwtService jwtService;

    private RefreshTokenService refreshTokenService;

    private UserDetailsServiceImpl userDetailsService;

    private RolesServiceImpl rolesService;

    @PostMapping("/login")
    public JwtResponseDTO login(@RequestBody AuthRequestDTO authRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()){
            String email = authRequest.getEmail();
            String accessToken = jwtService.generateToken(email);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(email);

            return getJwtResponseDTO(response, refreshToken, accessToken);
        }else{
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserInfoDTO userInfoDTO, HttpServletResponse response){
        if(userDetailsService.checkForExistingEmail(userInfoDTO.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }else{
            UserInfo newUser = new UserInfo();
            newUser.setName(userInfoDTO.getName());
            newUser.setSurname(userInfoDTO.getSurname());
            newUser.setEmail(userInfoDTO.getEmail());
            newUser.setPassword(passwordEncoder.encode(userInfoDTO.getPassword()));
            newUser.setAge(userInfoDTO.getAge());
            newUser.setHeight(userInfoDTO.getHeight());
            newUser.setWeight(userInfoDTO.getWeight());
            newUser.setGender(userInfoDTO.getGender());
            RolesUser rolesUser = this.rolesService.findByName("ROLE_USER");
            newUser.setRoles(Set.of(rolesUser));

            userDetailsService.saveUser(newUser);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("message", "User registered successfully"));

        }
    }

    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshTokenCookie, HttpServletResponse response){
        if(refreshTokenCookie == null){
            throw new RuntimeException("Missing refresh token");
        }

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenCookie).orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenService.verifyExpiration(refreshToken);

        String email = refreshToken.getUser().getEmail();
        String newAccessToken =  jwtService.generateToken(email);

        refreshTokenService.deleteRefreshToken(refreshToken);
        refreshTokenService.createRefreshToken(email);


        return getJwtResponseDTO(response, refreshToken, newAccessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "refreshToken", required = false) String refreshTokenCookie){
        refreshTokenService.findByToken(refreshTokenCookie).ifPresent(refreshTokenService::deleteRefreshToken);

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .build();

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
    }

    private JwtResponseDTO getJwtResponseDTO(HttpServletResponse response, RefreshToken refreshToken, String newAccessToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return JwtResponseDTO.builder().accessToken(newAccessToken).build();
    }

}
