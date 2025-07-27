package com.eventscheduler.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents an event that needs to be scheduled
 * This could be a conference talk, exam, or competition
 */
public class Event {
    private String id;
    private String name;
    private String type; // "CONFERENCE", "EXAM", "COMPETITION"
    private int duration; // in minutes
    private Set<String> requiredParticipants;
    private Set<String> conflictingEvents;
    private String roomRequirement; // room type needed
    private int capacity; // minimum room capacity needed
    private LocalDateTime scheduledTime;
    private String assignedRoom;
    private int priority; // 1-10, higher is more important
    
    public Event(String id, String name, String type, int duration) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.duration = duration;
        this.requiredParticipants = new HashSet<>();
        this.conflictingEvents = new HashSet<>();
        this.priority = 5; // default priority
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public Set<String> getRequiredParticipants() { return requiredParticipants; }
    public void setRequiredParticipants(Set<String> requiredParticipants) {
        this.requiredParticipants = requiredParticipants;
    }
    
    public void addRequiredParticipant(String participantId) {
        this.requiredParticipants.add(participantId);
    }
    
    public Set<String> getConflictingEvents() { return conflictingEvents; }
    public void setConflictingEvents(Set<String> conflictingEvents) {
        this.conflictingEvents = conflictingEvents;
    }
    
    public void addConflictingEvent(String eventId) {
        this.conflictingEvents.add(eventId);
    }
    
    public String getRoomRequirement() { return roomRequirement; }
    public void setRoomRequirement(String roomRequirement) {
        this.roomRequirement = roomRequirement;
    }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
    
    public String getAssignedRoom() { return assignedRoom; }
    public void setAssignedRoom(String assignedRoom) { this.assignedRoom = assignedRoom; }
    
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    public boolean isScheduled() {
        return scheduledTime != null && assignedRoom != null;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Event event = (Event) obj;
        return Objects.equals(id, event.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", duration=" + duration +
                ", scheduledTime=" + scheduledTime +
                ", assignedRoom='" + assignedRoom + '\'' +
                '}';
    }
}