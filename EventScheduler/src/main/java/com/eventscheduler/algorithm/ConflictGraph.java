package com.eventscheduler.algorithm;

import com.eventscheduler.model.Event;
import com.eventscheduler.model.Participant;
import java.util.*;

/**
 * Represents a conflict graph where events are vertices and edges represent conflicts
 * This is used for graph coloring algorithms to solve scheduling problems
 */
public class ConflictGraph {
    private Map<String, Set<String>> adjacencyList;
    private Map<String, Event> events;
    private Set<String> vertices;
    
    public ConflictGraph() {
        this.adjacencyList = new HashMap<>();
        this.events = new HashMap<>();
        this.vertices = new HashSet<>();
    }
    
    /**
     * Add an event as a vertex in the graph
     */
    public void addEvent(Event event) {
        String eventId = event.getId();
        events.put(eventId, event);
        vertices.add(eventId);
        adjacencyList.putIfAbsent(eventId, new HashSet<>());
    }
    
    /**
     * Add a conflict edge between two events
     */
    public void addConflict(String event1Id, String event2Id) {
        adjacencyList.get(event1Id).add(event2Id);
        adjacencyList.get(event2Id).add(event1Id);
    }
    
    /**
     * Build the conflict graph based on participant conflicts
     */
    public void buildConflictGraph(List<Event> eventList, List<Participant> participants) {
        // Add all events as vertices
        for (Event event : eventList) {
            addEvent(event);
        }
        
        // Add explicit conflicts defined in events
        for (Event event : eventList) {
            for (String conflictingEventId : event.getConflictingEvents()) {
                if (vertices.contains(conflictingEventId)) {
                    addConflict(event.getId(), conflictingEventId);
                }
            }
        }
        
        // Add conflicts based on participant requirements
        for (Participant participant : participants) {
            Set<String> participantEvents = new HashSet<>();
            participantEvents.addAll(participant.getRequiredEvents());
            participantEvents.addAll(participant.getOptionalEvents());
            
            // Any two events that require the same participant conflict with each other
            List<String> eventList2 = new ArrayList<>(participantEvents);
            for (int i = 0; i < eventList2.size(); i++) {
                for (int j = i + 1; j < eventList2.size(); j++) {
                    String event1 = eventList2.get(i);
                    String event2 = eventList2.get(j);
                    if (vertices.contains(event1) && vertices.contains(event2)) {
                        addConflict(event1, event2);
                    }
                }
            }
        }
    }
    
    /**
     * Get the degree of a vertex (number of conflicts)
     */
    public int getDegree(String eventId) {
        return adjacencyList.getOrDefault(eventId, new HashSet<>()).size();
    }
    
    /**
     * Get all neighbors (conflicting events) of an event
     */
    public Set<String> getNeighbors(String eventId) {
        return new HashSet<>(adjacencyList.getOrDefault(eventId, new HashSet<>()));
    }
    
    /**
     * Get all vertices sorted by degree (descending)
     * This is useful for greedy coloring algorithms
     */
    public List<String> getVerticesByDegree() {
        List<String> sortedVertices = new ArrayList<>(vertices);
        sortedVertices.sort((a, b) -> Integer.compare(getDegree(b), getDegree(a)));
        return sortedVertices;
    }
    
    /**
     * Get all vertices sorted by priority (descending)
     */
    public List<String> getVerticesByPriority() {
        List<String> sortedVertices = new ArrayList<>(vertices);
        sortedVertices.sort((a, b) -> {
            Event eventA = events.get(a);
            Event eventB = events.get(b);
            return Integer.compare(eventB.getPriority(), eventA.getPriority());
        });
        return sortedVertices;
    }
    
    /**
     * Check if two events are in conflict
     */
    public boolean hasConflict(String event1Id, String event2Id) {
        return adjacencyList.getOrDefault(event1Id, new HashSet<>()).contains(event2Id);
    }
    
    /**
     * Get the chromatic number (minimum colors needed) using Welsh-Powell approximation
     */
    public int estimateChromaticNumber() {
        if (vertices.isEmpty()) return 0;
        
        int maxDegree = vertices.stream()
                .mapToInt(this::getDegree)
                .max()
                .orElse(0);
        
        return maxDegree + 1; // Upper bound
    }
    
    /**
     * Find all maximal cliques (groups of mutually conflicting events)
     * This helps identify the most constrained scheduling groups
     */
    public List<Set<String>> findMaximalCliques() {
        List<Set<String>> cliques = new ArrayList<>();
        Set<String> candidates = new HashSet<>(vertices);
        Set<String> currentClique = new HashSet<>();
        Set<String> excluded = new HashSet<>();
        
        bronKerbosch(currentClique, candidates, excluded, cliques);
        return cliques;
    }
    
    /**
     * Bron-Kerbosch algorithm for finding maximal cliques
     */
    private void bronKerbosch(Set<String> currentClique, Set<String> candidates, 
                             Set<String> excluded, List<Set<String>> cliques) {
        if (candidates.isEmpty() && excluded.isEmpty()) {
            if (!currentClique.isEmpty()) {
                cliques.add(new HashSet<>(currentClique));
            }
            return;
        }
        
        // Choose pivot vertex to minimize branching
        String pivot = null;
        int maxConnections = -1;
        for (String v : candidates) {
            Set<String> connections = getNeighbors(v);
            connections.retainAll(candidates);
            if (connections.size() > maxConnections) {
                maxConnections = connections.size();
                pivot = v;
            }
        }
        
        Set<String> candidatesCopy = new HashSet<>(candidates);
        if (pivot != null) {
            candidatesCopy.removeAll(getNeighbors(pivot));
        }
        
        for (String vertex : candidatesCopy) {
            Set<String> newCurrentClique = new HashSet<>(currentClique);
            newCurrentClique.add(vertex);
            
            Set<String> newCandidates = new HashSet<>(candidates);
            newCandidates.retainAll(getNeighbors(vertex));
            
            Set<String> newExcluded = new HashSet<>(excluded);
            newExcluded.retainAll(getNeighbors(vertex));
            
            bronKerbosch(newCurrentClique, newCandidates, newExcluded, cliques);
            
            candidates.remove(vertex);
            excluded.add(vertex);
        }
    }
    
    public Set<String> getVertices() {
        return new HashSet<>(vertices);
    }
    
    public Map<String, Event> getEvents() {
        return new HashMap<>(events);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Conflict Graph:\n");
        for (String vertex : vertices) {
            sb.append(vertex).append(" -> ").append(adjacencyList.get(vertex)).append("\n");
        }
        return sb.toString();
    }
}