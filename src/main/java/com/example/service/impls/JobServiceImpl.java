package com.example.service.impls;

import com.example.dto.JobRequestDto;
import com.example.dto.JobResponseDto;
import com.example.mapper.JobMapper;
import com.example.repository.JobRepository;
import com.example.repository.specification.JobSpecification;
import com.example.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    @Override
    public Page<JobResponseDto> searchJobs(JobRequestDto request) {

        var spec = JobSpecification.build(request.keyword(), request.location(), request.jobType());

        var pageable = PageRequest.of(
                request.page(),
                request.size(),
                Sort.by("createdAt").descending()
        );

        return jobRepository.findAll(spec, pageable)
                .map(jobMapper::toDto);
    }
}