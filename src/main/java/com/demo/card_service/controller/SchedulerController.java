package com.demo.card_service.controller;

import com.demo.card_service.utils.ScheduledTaskUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

    private final ScheduledTaskUtil scheduledTaskUtil;

    public SchedulerController(ScheduledTaskUtil scheduledTaskUtil) {
        this.scheduledTaskUtil = scheduledTaskUtil;
    }

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleScheduler(@RequestParam boolean enable) {
        scheduledTaskUtil.setSchedulingEnabled(enable);
        return enable ? ResponseEntity.ok("Scheduler enabled") : ResponseEntity.ok("Scheduler disabled");
    }
}


