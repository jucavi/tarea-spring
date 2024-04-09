package com.example.tareaspring.dto.converter;

import com.example.tareaspring.dto.SigningDto;
import com.example.tareaspring.models.Player;
import com.example.tareaspring.models.Signing;
import com.example.tareaspring.models.Team;
import com.example.tareaspring.utils.DateUtils;

public class SigningMapper implements MapperInterface<Signing, SigningDto> {
    @Override
    public SigningDto mapDaoToDto(Signing signing) {
        return SigningDto.builder()
                .id(signing.getId())
                .playerId(
                        signing.getPlayer().getId())
                .teamId(
                        signing.getTeam().getId())
                .since(
                        DateUtils.fromLocalDateToString(signing.getSince(), null))
                .until(
                        DateUtils.fromLocalDateToString(signing.getUntil(), null))
                .squadNumber(signing.getSquadNumber())
                .build();
    }

    @Override
    public Signing mapDtoToDao(SigningDto signingDto) {
        return Signing.builder()
                .id(signingDto.getId())
                .player(
                        Player.builder()
                                .id(signingDto.getPlayerId())
                                .build())
                .team(
                        Team.builder()
                                .id(signingDto.getTeamId())
                                .build())
                .since(
                        DateUtils.fromStringToLocaldate(signingDto.getSince(), null))
                .until(
                        DateUtils.fromStringToLocaldate(signingDto.getUntil(), null))
                .squadNumber(signingDto.getSquadNumber())
                .build();
    }
}
