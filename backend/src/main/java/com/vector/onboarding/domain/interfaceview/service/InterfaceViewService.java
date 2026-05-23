package com.vector.onboarding.domain.interfaceview.service;

import com.vector.onboarding.domain.interfaceview.dto.InterfaceViewResponseDto;
import com.vector.onboarding.domain.interfaceview.repository.InterfaceViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterfaceViewService {

    private final InterfaceViewRepository interfaceViewRepository;

    public List<InterfaceViewResponseDto> getInterfaceViewBySpaceId(Long spaceId) {
        return interfaceViewRepository.findAllBySpaceIdOrderByNameAsc(spaceId).stream()
                .map(InterfaceViewResponseDto::from)
                .collect(Collectors.toList());
    }
}
