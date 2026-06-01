package com.queuepulse.controller;

import com.queuepulse.dto.QueueRequest;
import com.queuepulse.dto.QueueResponse;
import com.queuepulse.service.QueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/queues")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    @GetMapping
    public List<QueueResponse> list(@RequestParam(required = false) Long organizationId) {
        return queueService.findAll(organizationId);
    }

    @GetMapping("/{id}")
    public QueueResponse get(@PathVariable Long id) {
        return queueService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QueueResponse create(@Valid @RequestBody QueueRequest request) {
        return queueService.create(request);
    }

    @PutMapping("/{id}")
    public QueueResponse update(@PathVariable Long id, @Valid @RequestBody QueueRequest request) {
        return queueService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        queueService.delete(id);
    }
}
