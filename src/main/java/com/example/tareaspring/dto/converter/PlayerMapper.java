package com.example.tareaspring.dto.converter;

import com.example.tareaspring.dto.PlayerDto;
import com.example.tareaspring.models.FieldPosition;
import com.example.tareaspring.models.Gender;
import com.example.tareaspring.models.Player;
import com.example.tareaspring.utils.DateUtils;

// What's difference between @Component or create a @Bean
/**
 * Mapper DAO/DTO for Player
 */
public class PlayerMapper implements MapperInterface<Player, PlayerDto> {

    /**
     * Converts to DTO
     * @param player instance of Player
     * @return an instance of PlayerDto
     */
    @Override
    public PlayerDto mapDaoToDto(Player player) {
        return PlayerDto.builder()
                .id(player.getId())
                .firstname(player.getFirstname())
                .lastname(player.getLastname())
                .email(player.getEmail())
                .birthdate(
                        DateUtils.fromLocalDateToString(player.getBirthdate(), null))
                .position(player.getPosition().toString())
                .gender(player.getGender().toString())
                .imc(player.getImc())
                .weight(player.getWeight())
                .high(player.getHigh())
                .fat(player.getFat())
                .build();
    }


    /**
     * Converts to DAO
     * @param playerDto  instance of Player
     * @return an instance of PlayerDto
     */
    @Override
    public Player mapDtoToDao(PlayerDto playerDto) {
        return Player.builder()
                .id(playerDto.getId())
                .firstname(playerDto.getFirstname())
                .lastname(playerDto.getLastname())
                .email(playerDto.getEmail())
                .birthdate(
                        DateUtils.fromStringToLocaldate(playerDto.getBirthdate(), null))
                .position(
                        FieldPosition.valueOf(playerDto.getPosition()))
                .gender(Gender.valueOf(playerDto.getGender()))
                .imc(playerDto.getImc())
                .weight(playerDto.getWeight())
                .high(playerDto.getHigh())
                .fat(playerDto.getFat())
                .build();
    }
}
