package com.hardtech.app.controllers;

import com.hardtech.app.entities.Team;
import com.hardtech.app.services.team.TeamServicesImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(name = "Team controller",
        path = "/api/v1/app/teams",
        produces = "application/json")
@AllArgsConstructor
@RestController
public class TeamController {

    private TeamServicesImpl services;


    @GetMapping("search")
    List<Team> searchTeamNameLike(@RequestParam(name = "projectId") Long projectId,
                                  @RequestParam(name = "name") String name){
        return services.searchTeamNameLike(projectId, name);
    }

    @DeleteMapping("/{id}")
    void deleteTeamById(@PathVariable Long id){
        services.deleteTeamById(id);
    }

}
