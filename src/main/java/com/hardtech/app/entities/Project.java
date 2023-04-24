package com.hardtech.app.entities;

import jakarta.persistence.*;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@DynamicInsert
@DynamicUpdate
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    @NotBlank(message = "Project name blank")
    String name;

    @NotBlank(message = "Project description blank")
    String description;

    //@NotBlank(message = "Project status blank ")
    @Enumerated(EnumType.STRING)
    Status status = Status.NON_DEMARRE;

    //@NotBlank(message = "Project startAt blank")
    LocalDate startAt;

    //@NotBlank(message = "Project endAt blank")
    LocalDate endAt;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updateAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    List<Team> teams = new ArrayList<>();

}
