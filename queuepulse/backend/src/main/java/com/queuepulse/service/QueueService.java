package com.queuepulse.service;

import com.queuepulse.dto.QueueRequest;
import com.queuepulse.dto.QueueResponse;
import com.queuepulse.entity.Queue;
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

    public List<QueueResponse> findAll() {
        return queueRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public QueueResponse findById(Long id) {
        return queueRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Queue not found"));
    }

    @Transactional
    public QueueResponse create(QueueRequest request) {
        if (queueRepository.existsByName(request.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Queue name already exists");
        }

        Queue queue = Queue.builder()
                .name(request.name())
                .description(request.description())
                .active(request.active() != null ? request.active() : true)
                .build();

        return toResponse(queueRepository.save(queue));
    }

    @Transactional
    public QueueResponse update(Long id, QueueRequest request) {
        Queue queue = queueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Queue not found"));

        queueRepository.findByName(request.name())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Queue name already exists");
                });

        queue.setName(request.name());
        queue.setDescription(request.description());
        if (request.active() != null) {
            queue.setActive(request.active());
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

    private QueueResponse toResponse(Queue queue) {
        return QueueResponse.builder()
                .id(queue.getId())
                .name(queue.getName())
                .description(queue.getDescription())
                .active(queue.isActive())
                .createdAt(queue.getCreatedAt())
                .updatedAt(queue.getUpdatedAt())
                .build();
    }
}
