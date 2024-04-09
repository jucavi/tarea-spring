package com.example.tareaspring.dto.converter;

import com.example.tareaspring.dto.TeamDto;
import com.example.tareaspring.models.Team;
import com.example.tareaspring.utils.DateUtils;

/**
 * Mapper DAO/DTO for Team
 */
public class TeamMapper implements  MapperInterface<Team, TeamDto> {

    /**
     * Converts to DTO
     * @param team instance of Team
     * @return an instance of TeamDto
     */
    @Override
    public TeamDto mapDaoToDto(Team team) {
        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .email(team.getEmail())
                .since(
                        DateUtils.fromLocalDateToString(team.getSince(), null))
                .city(team.getCity())
                .build();
    }


    /**
     * Converts to DAO
     * @param teamDto instance of TeamDto
     * @return an instance of Team
     */
    @Override
    public Team mapDtoToDao(TeamDto teamDto) {
        return Team.builder()
                .id(teamDto.getId())
                .name(teamDto.getName())
                .email(teamDto.getEmail())
                .since(
                        DateUtils.fromStringToLocaldate(teamDto.getSince(), null))
                .city(teamDto.getCity())
                .build();
    }
}
