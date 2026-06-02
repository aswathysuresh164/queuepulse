package com.queuepulse.service;

import com.queuepulse.dto.QueueRequest;
import com.queuepulse.dto.QueueResponse;
import com.queuepulse.entity.Organization;
import com.queuepulse.entity.Queue;
import com.queuepulse.entity.QueueStatus;
import com.queuepulse.repository.OrganizationRepository;
import com.queuepulse.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueueService {

    private final QueueRepository queueRepository;
    private final OrganizationRepository organizationRepository;

    public List<QueueResponse> findAll(Long organizationId) {
        List<Queue> queues = organizationId != null
                ? queueRepository.findAllByOrganizationId(organizationId)
                : queueRepository.findAllWithOrganization();

        return queues.stream().map(this::toResponse).toList();
    }

    public QueueResponse findById(Long id) {
        return queueRepository.findByIdWithOrganization(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Queue not found"));
    }

    @Transactional
    public QueueResponse create(QueueRequest request) {
        Organization organization = resolveOrganization(request.organizationId());

        if (queueRepository.existsByNameAndOrganizationId(request.name(), organization.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Queue name already exists for this organization");
        }

        Queue queue = Queue.builder()
                .name(request.name())
                .organization(organization)
                .status(request.status() != null ? request.status() : QueueStatus.ACTIVE)
                .build();

        return toResponse(queueRepository.save(queue));
    }

    @Transactional
    public QueueResponse update(Long id, QueueRequest request) {
        Queue queue = queueRepository.findByIdWithOrganization(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Queue not found"));

        Organization organization = resolveOrganization(request.organizationId());

        queueRepository.findByNameAndOrganizationId(request.name(), organization.getId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Queue name already exists for this organization");
                });

        queue.setName(request.name());
        queue.setOrganization(organization);
        if (request.status() != null) {
            queue.setStatus(request.status());
        }

        return toResponse(queueRepository.save(queue));
    }

    @Transactional
    public void delete(Long id) {
        if (!queueRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Queue not found");
        }
        queueRepository.deleteById(id);
    }

    private Organization resolveOrganization(Long organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found"));
    }

    private QueueResponse toResponse(Queue queue) {
        return QueueResponse.builder()
                .id(queue.getId())
                .name(queue.getName())
                .organizationId(queue.getOrganization().getId())
                .status(queue.getStatus())
                .createdAt(queue.getCreatedAt())
                .build();
    }
}
