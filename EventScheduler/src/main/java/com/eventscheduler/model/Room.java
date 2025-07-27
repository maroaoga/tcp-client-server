package com.eventscheduler.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a room available for scheduling events
 */
public class Room {
    private String id;
    private String name;
    private String type; // "LECTURE_HALL", "CLASSROOM", "LAB", "AUDITORIUM", "GYM"
    private int capacity;
    private Set<String> features; // "PROJECTOR", "COMPUTER", "WHITEBOARD", etc.
    private Set<TimeSlot> unavailableSlots;
    
    public Room(String id, String name, String type, int capacity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.features = new HashSet<>();
        this.unavailableSlots = new HashSet<>();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public Set<String> getFeatures() { return features; }
    public void setFeatures(Set<String> features) { this.features = features; }
    
    public void addFeature(String feature) {
        this.features.add(feature);
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
    
    public boolean canAccommodate(Event event) {
        // Check capacity
        if (this.capacity < event.getCapacity()) {
            return false;
        }
        
        // Check room type compatibility
        if (event.getRoomRequirement() != null && 
            !this.type.equals(event.getRoomRequirement())) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Room room = (Room) obj;
        return Objects.equals(id, room.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                '}';
    }
    
    /**
     * Inner class to represent a time slot when the room is unavailable
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