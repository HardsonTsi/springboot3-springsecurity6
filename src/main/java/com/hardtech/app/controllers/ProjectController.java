package com.hardtech.app.controllers;

import com.hardtech.app.dtos.ProjectDto;
import com.hardtech.app.dtos.TeamDto;
import com.hardtech.app.entities.Project;
import com.hardtech.app.services.project.ProjectServicesImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping(name = "Project controller",
        path = "/api/v1/app/projects",
        produces = "application/json")
@RestController
public class ProjectController {

    private final ProjectServicesImpl services;

    @PostMapping
    public ResponseEntity<?> saveProject(@Valid @RequestBody ProjectDto project) {
        return ResponseEntity.status(HttpStatus.CREATED).body(services.saveProject(project));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProjectById(@PathVariable Long id) {
     return ResponseEntity.ok(services.findProjectById(id));
    }

    @GetMapping
    public List<Project> findAllProjects() {
        return services.findAllProjects();
    }

    @DeleteMapping("/{id}")
    public void deleteProjectById(@PathVariable Long id) {
        services.deleteProjectById(id);
    }

    @DeleteMapping("/all")
    public void deleteAll() {
        services.deleteAll();
    }

    @PostMapping("/addTeam")
    Project addTeam(@RequestParam Long projectId, @Valid @RequestBody TeamDto team){
        return services.addTeam(projectId, team);
    }

    @GetMapping("/searchTeams/{projectId}")
    Project searchTeamsNameLike(@PathVariable Long projectId, @RequestParam(name = "team_name") String name){
        return services.searchTeamNameLike(projectId, name);
    }

}
