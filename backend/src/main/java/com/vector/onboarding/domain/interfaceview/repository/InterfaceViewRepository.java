package com.vector.onboarding.domain.interfaceview.repository;

import com.vector.onboarding.domain.interfaceview.entity.InterfaceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceViewRepository extends JpaRepository<InterfaceView, Long> {
    List<InterfaceView> findAllBySpaceIdOrderByNameAsc(Long spaceId);
}
