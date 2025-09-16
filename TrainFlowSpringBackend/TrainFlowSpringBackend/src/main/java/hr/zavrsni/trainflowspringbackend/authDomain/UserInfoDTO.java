package hr.zavrsni.trainflowspringbackend.authDomain;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private Integer id;

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name can be at most 50 characters")
    private String name;

    @NotBlank(message = "Surname is required")
    @Size(max = 50, message = "Surname can be at most 50 characters")
    private String surname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    private Integer age;

    private Integer height;

    private Integer weight;

    @NotBlank(message = "Gender is required")
    private String gender;

    private Set<RolesUser> roles;
}
