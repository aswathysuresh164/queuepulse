package com.queuepulse.repository;

import com.queuepulse.entity.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QueueRepository extends JpaRepository<Queue, Long> {

    @Query("SELECT q FROM Queue q JOIN FETCH q.organization WHERE q.id = :id")
    Optional<Queue> findByIdWithOrganization(@Param("id") Long id);

    @Query("SELECT q FROM Queue q JOIN FETCH q.organization")
    List<Queue> findAllWithOrganization();

    @Query("SELECT q FROM Queue q JOIN FETCH q.organization WHERE q.organization.id = :organizationId")
    List<Queue> findAllByOrganizationId(@Param("organizationId") Long organizationId);

    Optional<Queue> findByNameAndOrganizationId(String name, Long organizationId);

    boolean existsByNameAndOrganizationId(String name, Long organizationId);
}
