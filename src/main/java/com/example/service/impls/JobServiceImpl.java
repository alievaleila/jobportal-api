package com.example.service.impls;

import com.example.dto.JobCreateDto;
import com.example.dto.JobResponseDto;
import com.example.dto.JobSearchRequestDto;
import com.example.entity.Job;
import com.example.mapper.JobMapper;
import com.example.repository.JobRepository;
import com.example.repository.specification.JobSpecification;
import com.example.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    @Override
    public Page<JobResponseDto> searchJobs(JobSearchRequestDto request) {
        var spec = JobSpecification.build(request.keyword(), request.location(), request.jobType());

        Set<String> allowed = Set.of("createdAt", "salary", "title", "companyName");
        String sortField = allowed.contains(request.sortBy()) ? request.sortBy() : "createdAt";

        var pageable = PageRequest.of(
                request.page(),
                request.size(),
                Sort.by(Sort.Direction.DESC, sortField)
        );

        return jobRepository.findAll(spec, pageable).map(jobMapper::toDto);
    }

    @Transactional
    @Override
    public JobResponseDto createJob(JobCreateDto dto) {
        Job job = jobMapper.toEntity(dto);
        return jobMapper.toDto(jobRepository.save(job));
    }
}