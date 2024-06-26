package com.example.tareaspring.config;

import com.example.tareaspring.dto.converter.PlayerMapper;
import com.example.tareaspring.dto.converter.SigningMapper;
import com.example.tareaspring.dto.converter.TeamMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomAppConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PlayerMapper playerMapper() {
        return new PlayerMapper();
    }

    @Bean
    public TeamMapper teamMapper() {
        return new TeamMapper();
    }

    @Bean
    public SigningMapper signingMapper() {
        return new SigningMapper();
    }
}
