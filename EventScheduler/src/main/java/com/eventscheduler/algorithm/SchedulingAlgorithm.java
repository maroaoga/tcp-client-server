package com.eventscheduler.algorithm;

import com.eventscheduler.model.Event;
import com.eventscheduler.model.Room;
import com.eventscheduler.model.Participant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Interface for different scheduling algorithms
 */
public interface SchedulingAlgorithm {
    
    /**
     * Schedule events given constraints
     * @param events List of events to schedule
     * @param rooms List of available rooms
     * @param participants List of participants
     * @param startTime Earliest possible start time
     * @param endTime Latest possible end time
     * @return SchedulingResult containing the schedule and statistics
     */
    SchedulingResult schedule(List<Event> events, List<Room> rooms, 
                            List<Participant> participants, 
                            LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Get the name of the algorithm
     */
    String getAlgorithmName();
    
    /**
     * Result class to encapsulate scheduling results
     */
    class SchedulingResult {
        private final List<Event> scheduledEvents;
        private final List<Event> unscheduledEvents;
        private final Map<String, String> eventRoomAssignments;
        private final int timeSlotColors; // Number of time slots used
        private final double schedulingEfficiency;
        private final long executionTimeMs;
        private final String algorithmUsed;
        
        public SchedulingResult(List<Event> scheduledEvents, List<Event> unscheduledEvents,
                              Map<String, String> eventRoomAssignments, int timeSlotColors,
                              double schedulingEfficiency, long executionTimeMs, String algorithmUsed) {
            this.scheduledEvents = scheduledEvents;
            this.unscheduledEvents = unscheduledEvents;
            this.eventRoomAssignments = eventRoomAssignments;
            this.timeSlotColors = timeSlotColors;
            this.schedulingEfficiency = schedulingEfficiency;
            this.executionTimeMs = executionTimeMs;
            this.algorithmUsed = algorithmUsed;
        }
        
        // Getters
        public List<Event> getScheduledEvents() { return scheduledEvents; }
        public List<Event> getUnscheduledEvents() { return unscheduledEvents; }
        public Map<String, String> getEventRoomAssignments() { return eventRoomAssignments; }
        public int getTimeSlotColors() { return timeSlotColors; }
        public double getSchedulingEfficiency() { return schedulingEfficiency; }
        public long getExecutionTimeMs() { return executionTimeMs; }
        public String getAlgorithmUsed() { return algorithmUsed; }
        
        public boolean isFullyScheduled() {
            return unscheduledEvents.isEmpty();
        }
        
        public int getTotalEvents() {
            return scheduledEvents.size() + unscheduledEvents.size();
        }
        
        @Override
        public String toString() {
            return String.format(
                "SchedulingResult{\n" +
                "  Algorithm: %s\n" +
                "  Scheduled: %d/%d events (%.2f%%)\n" +
                "  Time slots used: %d\n" +
                "  Scheduling efficiency: %.2f%%\n" +
                "  Execution time: %d ms\n" +
                "}",
                algorithmUsed,
                scheduledEvents.size(), getTotalEvents(),
                (scheduledEvents.size() * 100.0 / getTotalEvents()),
                timeSlotColors,
                schedulingEfficiency * 100,
                executionTimeMs
            );
        }
    }
}