package com.example.mapper;


import com.example.dto.JobCreateDto;
import com.example.dto.JobResponseDto;
import com.example.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobMapper {

    Job toEntity(JobCreateDto createDto);

    JobResponseDto toDto(Job job);


}

