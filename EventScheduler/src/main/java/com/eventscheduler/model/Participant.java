package com.eventscheduler.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a participant who can attend events
 */
public class Participant {
    private String id;
    private String name;
    private String type; // "STUDENT", "TEACHER", "SPEAKER", "JUDGE"
    private Set<String> requiredEvents;
    private Set<String> optionalEvents;
    private Set<TimeSlot> unavailableSlots;
    
    public Participant(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.requiredEvents = new HashSet<>();
        this.optionalEvents = new HashSet<>();
        this.unavailableSlots = new HashSet<>();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Set<String> getRequiredEvents() { return requiredEvents; }
    public void setRequiredEvents(Set<String> requiredEvents) {
        this.requiredEvents = requiredEvents;
    }
    
    public void addRequiredEvent(String eventId) {
        this.requiredEvents.add(eventId);
    }
    
    public Set<String> getOptionalEvents() { return optionalEvents; }
    public void setOptionalEvents(Set<String> optionalEvents) {
        this.optionalEvents = optionalEvents;
    }
    
    public void addOptionalEvent(String eventId) {
        this.optionalEvents.add(eventId);
    }
    
    public Set<TimeSlot> getUnavailableSlots() { return unavailableSlots; }
    public void setUnavailableSlots(Set<TimeSlot> unavailableSlots) {
        this.unavailableSlots = unavailableSlots;
    }
    
    public void addUnavailableSlot(TimeSlot slot) {
        this.unavailableSlots.add(slot);
    }
    
    public boolean isAvailable(LocalDateTime startTime, int durationMinutes) {
        LocalDateTime endTime = startTime.plusMinutes(durationMinutes);
        
        for (TimeSlot slot : unavailableSlots) {
            if (slot.overlaps(startTime, endTime)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Participant participant = (Participant) obj;
        return Objects.equals(id, participant.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Participant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
    
    /**
     * Inner class to represent a time slot when the participant is unavailable
     */
    public static class TimeSlot {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        
        public TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
        
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        
        public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
            return startTime.isBefore(otherEnd) && endTime.isAfter(otherStart);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TimeSlot timeSlot = (TimeSlot) obj;
            return Objects.equals(startTime, timeSlot.startTime) &&
                   Objects.equals(endTime, timeSlot.endTime);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(startTime, endTime);
        }
    }
}