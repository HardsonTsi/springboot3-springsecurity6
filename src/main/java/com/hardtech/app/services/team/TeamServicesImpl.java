package com.hardtech.app.services.team;

import com.hardtech.app.Utils;
import com.hardtech.app.entities.Team;
import com.hardtech.app.exceptions.NotFoundException;
import com.hardtech.app.repositories.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TeamServicesImpl extends Utils implements TeamServices{

    private final TeamRepository repository;

    @Override
    public void deleteTeamById(Long id) {
       try{
           repository.deleteById(id);
       }catch (Exception e){
           throw new NotFoundException("Team " + id + " not found, delete operation univailable");
       }
    }

    @Override
    public List<Team> searchTeamNameLike(Long id,String name) {
        return repository.findByProject_IdAndNameLike(id, surroundString(name));
    }
}
