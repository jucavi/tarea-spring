package com.example.tareaspring.dto.converter;

import com.example.tareaspring.dto.PlayerDto;
import com.example.tareaspring.models.Player;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerDtoConverter {

    private final ModelMapper modelMapper;

    public PlayerDto convertEntityToDto(Player player) {

        return modelMapper.map(player, PlayerDto.class);
    }

    public Player convertDtoToEntity(PlayerDto playerDto) {
//        modelMapper.getConfiguration()
//                .setMatchingStrategy(MatchingStrategies.LOOSE);

        return modelMapper.map(playerDto, Player.class);
    }
}
