package com.eventscheduler.algorithm;

import com.eventscheduler.model.Event;
import com.eventscheduler.model.Room;
import com.eventscheduler.model.Participant;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Scheduling algorithm based on graph coloring theory
 * Uses the conflict graph where events are vertices and conflicts are edges
 * Colors represent time slots
 */
public class GraphColoringScheduler implements SchedulingAlgorithm {
    

    
    private final ColoringStrategy strategy;
    
    public static enum ColoringStrategy {
        WELSH_POWELL,    // Sort by degree, assign lowest available color
        GREEDY_PRIORITY, // Sort by priority, assign lowest available color
        DSATUR           // Dynamic saturation algorithm
    }
    
    public GraphColoringScheduler(ColoringStrategy strategy) {
        this.strategy = strategy;
    }
    
    public GraphColoringScheduler() {
        this.strategy = ColoringStrategy.WELSH_POWELL; // Default
    }
    
    @Override
    public SchedulingResult schedule(List<Event> events, List<Room> rooms,
                                   List<Participant> participants,
                                   LocalDateTime startTime, LocalDateTime endTime) {
        long startTimeMs = System.currentTimeMillis();
        
        // Build conflict graph
        ConflictGraph conflictGraph = new ConflictGraph();
        conflictGraph.buildConflictGraph(events, participants);
        
        // Apply graph coloring algorithm
        Map<String, Integer> coloring = colorGraph(conflictGraph);
        
        // Convert colors to time slots and assign rooms
        Map<String, String> eventRoomAssignments = new HashMap<>();
        List<Event> scheduledEvents = new ArrayList<>();
        List<Event> unscheduledEvents = new ArrayList<>();
        
        // Group events by color (time slot)
        Map<Integer, List<String>> colorGroups = new HashMap<>();
        for (Map.Entry<String, Integer> entry : coloring.entrySet()) {
            colorGroups.computeIfAbsent(entry.getValue(), k -> new ArrayList<>()).add(entry.getKey());
        }
        
        // Calculate time slot duration (assuming uniform slots)
        int maxDuration = events.stream().mapToInt(Event::getDuration).max().orElse(60);
        int timeSlotDuration = maxDuration + 15; // Add buffer time
        
        // Assign time slots and rooms
        LocalDateTime currentTime = startTime;
        int colorCount = 0;
        
        for (Integer color : colorGroups.keySet().stream().sorted().collect(java.util.stream.Collectors.toList())) {
            List<String> eventsInSlot = colorGroups.get(color);
            
            // Check if we have enough time for this slot
            if (currentTime.plusMinutes(timeSlotDuration).isAfter(endTime)) {
                // Add remaining events to unscheduled
                for (String eventId : eventsInSlot) {
                    Event event = conflictGraph.getEvents().get(eventId);
                    if (event != null) {
                        unscheduledEvents.add(event);
                    }
                }
                continue;
            }
            
            // Assign rooms to events in this time slot
            List<Room> availableRooms = new ArrayList<>();
            for (Room room : rooms) {
                if (room.isAvailable(currentTime, timeSlotDuration)) {
                    availableRooms.add(room);
                }
            }
            
            // Sort events by priority and room requirements
            List<Event> slotEvents = new ArrayList<>();
            for (String eventId : eventsInSlot) {
                Event event = conflictGraph.getEvents().get(eventId);
                if (event != null) {
                    slotEvents.add(event);
                }
            }
            slotEvents.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));
            
            // Assign rooms using best-fit strategy
            for (Event event : slotEvents) {
                Room assignedRoom = findBestRoom(event, availableRooms);
                if (assignedRoom != null) {
                    event.setScheduledTime(currentTime);
                    event.setAssignedRoom(assignedRoom.getId());
                    scheduledEvents.add(event);
                    eventRoomAssignments.put(event.getId(), assignedRoom.getId());
                    availableRooms.remove(assignedRoom);
                    
                    // Mark room as unavailable for this time slot
                    assignedRoom.addUnavailableSlot(
                        new Room.TimeSlot(currentTime, currentTime.plusMinutes(timeSlotDuration))
                    );
                } else {
                    unscheduledEvents.add(event);
                }
            }
            
            currentTime = currentTime.plusMinutes(timeSlotDuration);
            colorCount++;
        }
        
        long endTimeMs = System.currentTimeMillis();
        double efficiency = scheduledEvents.size() / (double) events.size();
        
        return new SchedulingResult(
            scheduledEvents,
            unscheduledEvents,
            eventRoomAssignments,
            colorCount,
            efficiency,
            endTimeMs - startTimeMs,
            getAlgorithmName()
        );
    }
    
    /**
     * Apply graph coloring algorithm based on selected strategy
     */
    private Map<String, Integer> colorGraph(ConflictGraph graph) {
        switch (strategy) {
            case WELSH_POWELL:
                return welshPowellColoring(graph);
            case GREEDY_PRIORITY:
                return greedyPriorityColoring(graph);
            case DSATUR:
                return dsaturColoring(graph);
            default:
                return welshPowellColoring(graph);
        }
    }
    
    /**
     * Welsh-Powell graph coloring algorithm
     * Sort vertices by degree (descending), assign lowest available color
     */
    private Map<String, Integer> welshPowellColoring(ConflictGraph graph) {
        Map<String, Integer> coloring = new HashMap<>();
        List<String> vertices = graph.getVerticesByDegree();
        
        for (String vertex : vertices) {
            Set<Integer> usedColors = new HashSet<>();
            
            // Find colors used by neighbors
            for (String neighbor : graph.getNeighbors(vertex)) {
                if (coloring.containsKey(neighbor)) {
                    usedColors.add(coloring.get(neighbor));
                }
            }
            
            // Assign lowest available color
            int color = 0;
            while (usedColors.contains(color)) {
                color++;
            }
            coloring.put(vertex, color);
        }
        
        return coloring;
    }
    
    /**
     * Greedy coloring with priority ordering
     */
    private Map<String, Integer> greedyPriorityColoring(ConflictGraph graph) {
        Map<String, Integer> coloring = new HashMap<>();
        List<String> vertices = graph.getVerticesByPriority();
        
        for (String vertex : vertices) {
            Set<Integer> usedColors = new HashSet<>();
            
            // Find colors used by neighbors
            for (String neighbor : graph.getNeighbors(vertex)) {
                if (coloring.containsKey(neighbor)) {
                    usedColors.add(coloring.get(neighbor));
                }
            }
            
            // Assign lowest available color
            int color = 0;
            while (usedColors.contains(color)) {
                color++;
            }
            coloring.put(vertex, color);
        }
        
        return coloring;
    }
    
    /**
     * DSATUR (Degree of Saturation) algorithm
     * Select vertex with highest saturation degree, break ties by degree
     */
    private Map<String, Integer> dsaturColoring(ConflictGraph graph) {
        Map<String, Integer> coloring = new HashMap<>();
        Set<String> uncolored = new HashSet<>(graph.getVertices());
        
        while (!uncolored.isEmpty()) {
            // Find vertex with highest saturation degree
            String selectedVertex = null;
            int maxSaturation = -1;
            int maxDegree = -1;
            
            for (String vertex : uncolored) {
                int saturation = getSaturationDegree(vertex, graph, coloring);
                int degree = graph.getDegree(vertex);
                
                if (saturation > maxSaturation || 
                    (saturation == maxSaturation && degree > maxDegree)) {
                    selectedVertex = vertex;
                    maxSaturation = saturation;
                    maxDegree = degree;
                }
            }
            
            // Color the selected vertex
            Set<Integer> usedColors = new HashSet<>();
            for (String neighbor : graph.getNeighbors(selectedVertex)) {
                if (coloring.containsKey(neighbor)) {
                    usedColors.add(coloring.get(neighbor));
                }
            }
            
            int color = 0;
            while (usedColors.contains(color)) {
                color++;
            }
            coloring.put(selectedVertex, color);
            uncolored.remove(selectedVertex);
        }
        
        return coloring;
    }
    
    /**
     * Calculate saturation degree (number of different colors used by neighbors)
     */
    private int getSaturationDegree(String vertex, ConflictGraph graph, Map<String, Integer> coloring) {
        Set<Integer> neighborColors = new HashSet<>();
        for (String neighbor : graph.getNeighbors(vertex)) {
            if (coloring.containsKey(neighbor)) {
                neighborColors.add(coloring.get(neighbor));
            }
        }
        return neighborColors.size();
    }
    
    /**
     * Find best room for an event using capacity and type matching
     */
    private Room findBestRoom(Event event, List<Room> availableRooms) {
        Room bestRoom = null;
        int bestScore = -1;
        
        for (Room room : availableRooms) {
            if (!room.canAccommodate(event)) {
                continue;
            }
            
            int score = 0;
            
            // Prefer rooms with capacity close to requirement
            if (room.getCapacity() >= event.getCapacity()) {
                score += 100 - (room.getCapacity() - event.getCapacity());
            }
            
            // Prefer exact room type match
            if (event.getRoomRequirement() != null && 
                room.getType().equals(event.getRoomRequirement())) {
                score += 50;
            }
            
            if (score > bestScore) {
                bestScore = score;
                bestRoom = room;
            }
        }
        
        return bestRoom;
    }
    
    @Override
    public String getAlgorithmName() {
        return "Graph Coloring (" + strategy.name() + ")";
    }
}