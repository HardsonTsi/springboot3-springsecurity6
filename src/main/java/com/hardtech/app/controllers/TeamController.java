package com.hardtech.app.controllers;

import com.hardtech.app.services.team.TeamServicesImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(name = "Team controller",
        path = "/api/v1/app/teams",
        produces = "application/json")
@AllArgsConstructor
@RestController
public class TeamController {

    private TeamServicesImpl services;

    @DeleteMapping("/{id}")
    void deleteTeamById(@PathVariable Long id){
        services.deleteTeamById(id);
    }

}
