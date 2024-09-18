package com.demo.card_service.utils;

import org.springframework.stereotype.Component;

@Component
public class ScheduledTaskUtil {

    private boolean isSchedulingEnabled = false;

    public void setSchedulingEnabled(boolean enabled) {
        this.isSchedulingEnabled = enabled;
    }

    public boolean isSchedulingEnabled() {
        return isSchedulingEnabled;
    }
}
