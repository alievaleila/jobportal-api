package com.example.mapper;


import com.example.dto.JobResponseDto;
import com.example.entity.Job;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {

    JobResponseDto toDto(Job job);
}

