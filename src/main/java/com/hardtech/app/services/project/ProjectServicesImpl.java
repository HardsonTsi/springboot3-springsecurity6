package com.hardtech.app.services.project;

import com.hardtech.app.dtos.ProjectDto;
import com.hardtech.app.dtos.TeamDto;
import com.hardtech.app.entities.Project;
import com.hardtech.app.entities.Status;
import com.hardtech.app.entities.Team;
import com.hardtech.app.exceptions.DuplicateValueException;
import com.hardtech.app.exceptions.NotFoundException;
import com.hardtech.app.repositories.ProjectRepository;
import com.hardtech.app.services.team.TeamServicesImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Slf4j
@Service
public class ProjectServicesImpl implements ProjectServices {

    private final ProjectRepository repository;
    private final TeamServicesImpl teamServices;

    @Override
    public Project saveProject(ProjectDto project) {

        try {
            Project p = Project.builder()
                    .name(project.name())
                    .description(project.description())
                    .status(Status.NON_DEMARRE)
                    .startAt(project.startAt())
                    .endAt(project.endAt())
                    .build();
            Project saved = repository.save(p);
            log.info("Project {} saved", project.name());
            return saved;
        } catch (Exception e) {
            throw new DuplicateValueException("Project " + project.name() + " alraydy exists");
        }


    }


    @Override
    public Project findProjectById(Long id) {
        Optional<Project> optional = repository.findById(id);
        if (optional.isEmpty())
            throw new NotFoundException("Project " + id + " not " + "found");
        return optional.get();
    }

    @Override
    public List<Project> findAllProjects() {
        return repository.findAll();
    }

    @Override
    public void deleteProjectById(Long id) {
        var alraydy = findProjectById(id);
        if (alraydy != null)
            repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public Project addTeam(Long projectId, TeamDto team) {
        Project project = findProjectById(projectId);
        Team newTeam = Team.builder()
                .name(team.name())
                .description(team.description())
                .project(project)
                .build();
        project.getTeams().add(newTeam);
        return repository.save(project);
    }

    @Override
    public Project searchTeamNameLike(Long id, String name) {
        Project alraydy = findProjectById(id);
        if (alraydy != null) {
            alraydy.setTeams(teamServices.searchTeamNameLike(id, name));
            return alraydy;
        }
        return null;
    }


}
