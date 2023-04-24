package com.hardtech.app.services.team;

import com.hardtech.app.entities.Team;

import java.util.List;

public interface TeamServices {
    void deleteTeamById(Long id);

    List<Team> searchTeamNameLike(Long id, String name);
}
