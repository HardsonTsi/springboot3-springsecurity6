package com.hardtech.app.services.project;

import com.hardtech.app.dtos.ProjectDto;
import com.hardtech.app.dtos.TeamDto;
import com.hardtech.app.entities.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectServices {

    Project saveProject(ProjectDto project);

//    Project updateProject(Project project);

    Project findProjectById(Long id);

    List<Project> findAllProjects();

    void deleteProjectById(Long id);

    void deleteAll();

    Project addTeam(Long projectId, TeamDto team);

    Project searchTeamNameLike(Long id, String name);

}
