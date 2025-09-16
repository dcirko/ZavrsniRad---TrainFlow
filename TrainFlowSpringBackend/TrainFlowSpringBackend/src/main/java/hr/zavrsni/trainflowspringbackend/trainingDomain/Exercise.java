package hr.zavrsni.trainflowspringbackend.trainingDomain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ExerciseCategory category;

    @Column(length = 1000)
    private String description;
}
