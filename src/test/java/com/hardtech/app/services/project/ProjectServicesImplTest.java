package com.hardtech.app.services.project;

import com.hardtech.app.dtos.ProjectDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectServicesImplTest {

    private final List<ProjectDto> projectsDto = new ArrayList<>();
    @Autowired
    private ProjectServicesImpl services;
    private ProjectDto projectDto;

    @BeforeEach
    void setUp() {
        this.projectDto = new ProjectDto("Benin", "Benin révélé", LocalDate.now(), LocalDate.now());
        this.projectsDto.add(new ProjectDto("Canada", "Canada révélé", LocalDate.now(), LocalDate.now()));
        this.projectsDto.add(new ProjectDto("USA", "USA révélé", LocalDate.now(), LocalDate.now()));
        this.projectsDto.add(new ProjectDto("France", "France révélé", LocalDate.now(), LocalDate.now()));
        this.projectsDto.add(this.projectDto);
    }

    @Test
    void saveProject() {
        var saved = services.saveProject(this.projectDto);
        assertNotNull(saved, "Project saved successfully");
    }

    @Test
    void findProjectById() {
        Long id = 10L;
        //assertTrue(!services.findProjectById(id).isEmpty(), "Project by " + id + " not found");
    }

    @Test
    void findAllProjects() {
        services.deleteAll();
        this.projectsDto.forEach(project -> services.saveProject(project));

        assertTrue(services.findAllProjects().size() == 4, "Projects not save");

    }

    @Test
    void deleteProjectById() {
        Long id = 23L;
          services.deleteProjectById(id);
    }

}