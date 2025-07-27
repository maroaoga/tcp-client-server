package com.eventscheduler;

import com.eventscheduler.algorithm.*;
import com.eventscheduler.model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Main application class demonstrating event scheduling using discrete structures
 * Handles conference talks, exams, and competitions scheduling
 */
public class EventSchedulerApp {
    
    public static void main(String[] args) {
        EventSchedulerApp app = new EventSchedulerApp();
        
        System.out.println("====================================================");
        System.out.println("EVENT SCHEDULER - Discrete Structures Implementation");
        System.out.println("====================================================\n");
        
        // Run different scheduling scenarios
        app.runConferenceSchedulingDemo();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        app.runExamSchedulingDemo();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        app.runCompetitionSchedulingDemo();
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        app.compareAlgorithms();
    }
    
    /**
     * Demonstrate conference talk scheduling
     */
    public void runConferenceSchedulingDemo() {
        System.out.println("CONFERENCE SCHEDULING DEMO");
        System.out.println("Using Graph Coloring to schedule conference talks\n");
        
        // Create conference events
        List<Event> events = createConferenceEvents();
        List<Room> rooms = createConferenceRooms();
        List<Participant> participants = createConferenceParticipants();
        
        // Set up scheduling constraints
        LocalDateTime startTime = LocalDateTime.of(2024, 6, 15, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 6, 15, 17, 0);
        
        // Use Welsh-Powell graph coloring algorithm
        SchedulingAlgorithm scheduler = new GraphColoringScheduler();
        SchedulingAlgorithm.SchedulingResult result = scheduler.schedule(
            events, rooms, participants, startTime, endTime
        );
        
        displaySchedulingResults("Conference", result);
    }
    
    /**
     * Demonstrate exam scheduling
     */
    public void runExamSchedulingDemo() {
        System.out.println("EXAM SCHEDULING DEMO");
        System.out.println("Ensuring no student has conflicting exams\n");
        
        // Create exam events
        List<Event> events = createExamEvents();
        List<Room> rooms = createExamRooms();
        List<Participant> participants = createExamParticipants();
        
        // Set up scheduling constraints
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 10, 8, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 12, 14, 18, 0);
        
        // Use priority-based graph coloring
        SchedulingAlgorithm scheduler = new GraphColoringScheduler(
            GraphColoringScheduler.ColoringStrategy.GREEDY_PRIORITY
        );
        SchedulingAlgorithm.SchedulingResult result = scheduler.schedule(
            events, rooms, participants, startTime, endTime
        );
        
        displaySchedulingResults("Exam", result);
    }
    
    /**
     * Demonstrate competition scheduling
     */
    public void runCompetitionSchedulingDemo() {
        System.out.println("SPORTS COMPETITION SCHEDULING DEMO");
        System.out.println("Scheduling sports events with venue constraints\n");
        
        // Create competition events
        List<Event> events = createCompetitionEvents();
        List<Room> rooms = createCompetitionVenues();
        List<Participant> participants = createCompetitionParticipants();
        
        // Set up scheduling constraints
        LocalDateTime startTime = LocalDateTime.of(2024, 7, 20, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 7, 22, 20, 0);
        
        // Use DSATUR algorithm for complex competitions
        SchedulingAlgorithm scheduler = new GraphColoringScheduler(
            GraphColoringScheduler.ColoringStrategy.DSATUR
        );
        SchedulingAlgorithm.SchedulingResult result = scheduler.schedule(
            events, rooms, participants, startTime, endTime
        );
        
        displaySchedulingResults("Competition", result);
    }
    
    /**
     * Compare different algorithms on the same problem
     */
    public void compareAlgorithms() {
        System.out.println("ALGORITHM COMPARISON");
        System.out.println("Testing different graph coloring strategies\n");
        
        List<Event> events = createMixedEvents();
        List<Room> rooms = createMixedRooms();
        List<Participant> participants = createMixedParticipants();
        
        LocalDateTime startTime = LocalDateTime.of(2024, 8, 1, 8, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 8, 3, 18, 0);
        
        // Test all algorithms
        SchedulingAlgorithm[] algorithms = {
            new GraphColoringScheduler(GraphColoringScheduler.ColoringStrategy.WELSH_POWELL),
            new GraphColoringScheduler(GraphColoringScheduler.ColoringStrategy.GREEDY_PRIORITY),
            new GraphColoringScheduler(GraphColoringScheduler.ColoringStrategy.DSATUR)
        };
        
        System.out.printf("%-25s %-12s %-12s %-15s %-15s%n", 
                         "Algorithm", "Scheduled", "Time Slots", "Efficiency", "Exec Time (ms)");
        System.out.println("-".repeat(80));
        
        for (SchedulingAlgorithm algorithm : algorithms) {
            // Reset events for fair comparison
            List<Event> eventsCopy = copyEvents(events);
            List<Room> roomsCopy = copyRooms(rooms);
            
            SchedulingAlgorithm.SchedulingResult result = algorithm.schedule(
                eventsCopy, roomsCopy, participants, startTime, endTime
            );
            
            System.out.printf("%-25s %-12s %-12d %-15.1f%% %-15d%n",
                             result.getAlgorithmUsed(),
                             result.getScheduledEvents().size() + "/" + result.getTotalEvents(),
                             result.getTimeSlotColors(),
                             result.getSchedulingEfficiency() * 100,
                             result.getExecutionTimeMs());
        }
    }
    
    /**
     * Display detailed scheduling results
     */
    private void displaySchedulingResults(String scenarioType, SchedulingAlgorithm.SchedulingResult result) {
        System.out.println(result);
        System.out.println();
        
        if (!result.getScheduledEvents().isEmpty()) {
            System.out.println("SCHEDULED EVENTS:");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm");
            
            // Group by time slot
            Map<LocalDateTime, List<Event>> timeSlots = new TreeMap<>();
            for (Event event : result.getScheduledEvents()) {
                timeSlots.computeIfAbsent(event.getScheduledTime(), k -> new ArrayList<>()).add(event);
            }
            
            for (Map.Entry<LocalDateTime, List<Event>> entry : timeSlots.entrySet()) {
                System.out.println("\n" + entry.getKey().format(formatter) + ":");
                for (Event event : entry.getValue()) {
                    System.out.printf("  %-15s %-25s (Room: %s, %d min)%n",
                                     event.getType(),
                                     event.getName(),
                                     event.getAssignedRoom(),
                                     event.getDuration());
                }
            }
        }
        
        if (!result.getUnscheduledEvents().isEmpty()) {
            System.out.println("\nUNSCHEDULED EVENTS:");
            for (Event event : result.getUnscheduledEvents()) {
                System.out.printf("  %-15s %s%n", event.getType(), event.getName());
            }
        }
        
        System.out.println("\nCONSTRAINT ANALYSIS:");
        System.out.println("- Participant conflicts resolved by graph coloring");
        System.out.println("- Room capacity and type constraints enforced");
        System.out.println("- Time window constraints respected");
        if (result.isFullyScheduled()) {
            System.out.println("✅ All events successfully scheduled!");
        } else {
            System.out.println("⚠️  Some events could not be scheduled due to constraints");
        }
    }
    
    // Event creation methods for different scenarios
    
    private List<Event> createConferenceEvents() {
        List<Event> events = Arrays.asList(
            new Event("TALK_001", "AI in Healthcare", "CONFERENCE", 45),
            new Event("TALK_002", "Blockchain Technologies", "CONFERENCE", 45),
            new Event("TALK_003", "Machine Learning Ethics", "CONFERENCE", 45),
            new Event("TALK_004", "Quantum Computing", "CONFERENCE", 45),
            new Event("KEYNOTE_001", "Future of Technology", "CONFERENCE", 60),
            new Event("WORKSHOP_001", "Hands-on Deep Learning", "CONFERENCE", 90),
            new Event("PANEL_001", "Industry Perspectives", "CONFERENCE", 60)
        );
        
        // Set priorities and requirements
        events.get(4).setPriority(10); // Keynote highest priority
        events.get(5).setPriority(8);  // Workshop high priority
        events.get(6).setPriority(7);  // Panel high priority
        
        for (Event event : events.subList(0, 4)) {
            event.setPriority(5); // Regular talks
            event.setCapacity(50);
            event.setRoomRequirement("LECTURE_HALL");
        }
        
        events.get(4).setCapacity(200);
        events.get(4).setRoomRequirement("AUDITORIUM");
        events.get(5).setCapacity(30);
        events.get(5).setRoomRequirement("LAB");
        events.get(6).setCapacity(100);
        events.get(6).setRoomRequirement("LECTURE_HALL");
        
        return events;
    }
    
    private List<Room> createConferenceRooms() {
        return Arrays.asList(
            new Room("ROOM_A", "Main Auditorium", "AUDITORIUM", 250),
            new Room("ROOM_B", "Lecture Hall 1", "LECTURE_HALL", 80),
            new Room("ROOM_C", "Lecture Hall 2", "LECTURE_HALL", 80),
            new Room("ROOM_D", "Computer Lab", "LAB", 40),
            new Room("ROOM_E", "Meeting Room", "CLASSROOM", 25)
        );
    }
    
    private List<Participant> createConferenceParticipants() {
        List<Participant> participants = Arrays.asList(
            new Participant("SPEAKER_001", "Dr. Alice Johnson", "SPEAKER"),
            new Participant("SPEAKER_002", "Prof. Bob Smith", "SPEAKER"),
            new Participant("SPEAKER_003", "Dr. Carol Wilson", "SPEAKER"),
            new Participant("ATTENDEE_001", "Important Attendee", "ATTENDEE")
        );
        
        // Set participant-event relationships
        participants.get(0).addRequiredEvent("TALK_001");
        participants.get(0).addRequiredEvent("KEYNOTE_001");
        participants.get(1).addRequiredEvent("TALK_002");
        participants.get(1).addRequiredEvent("PANEL_001");
        participants.get(2).addRequiredEvent("TALK_003");
        participants.get(2).addRequiredEvent("WORKSHOP_001");
        participants.get(3).addRequiredEvent("KEYNOTE_001");
        participants.get(3).addRequiredEvent("PANEL_001");
        
        return participants;
    }
    
    private List<Event> createExamEvents() {
        List<Event> events = Arrays.asList(
            new Event("EXAM_001", "Mathematics Final", "EXAM", 120),
            new Event("EXAM_002", "Physics Final", "EXAM", 120),
            new Event("EXAM_003", "Chemistry Final", "EXAM", 120),
            new Event("EXAM_004", "Computer Science Final", "EXAM", 180),
            new Event("EXAM_005", "English Literature Final", "EXAM", 120),
            new Event("EXAM_006", "History Final", "EXAM", 120)
        );
        
        for (Event event : events) {
            event.setPriority(9); // All exams high priority
            event.setCapacity(30);
            event.setRoomRequirement("CLASSROOM");
        }
        
        return events;
    }
    
    private List<Room> createExamRooms() {
        return Arrays.asList(
            new Room("EXAM_A", "Classroom A", "CLASSROOM", 35),
            new Room("EXAM_B", "Classroom B", "CLASSROOM", 35),
            new Room("EXAM_C", "Classroom C", "CLASSROOM", 35),
            new Room("EXAM_D", "Large Classroom", "CLASSROOM", 50)
        );
    }
    
    private List<Participant> createExamParticipants() {
        List<Participant> participants = Arrays.asList(
            new Participant("STUDENT_001", "John Doe", "STUDENT"),
            new Participant("STUDENT_002", "Jane Smith", "STUDENT"),
            new Participant("STUDENT_003", "Mike Johnson", "STUDENT")
        );
        
        // Students enrolled in multiple courses create conflicts
        participants.get(0).addRequiredEvent("EXAM_001");
        participants.get(0).addRequiredEvent("EXAM_002");
        participants.get(0).addRequiredEvent("EXAM_004");
        
        participants.get(1).addRequiredEvent("EXAM_001");
        participants.get(1).addRequiredEvent("EXAM_003");
        participants.get(1).addRequiredEvent("EXAM_005");
        
        participants.get(2).addRequiredEvent("EXAM_002");
        participants.get(2).addRequiredEvent("EXAM_004");
        participants.get(2).addRequiredEvent("EXAM_006");
        
        return participants;
    }
    
    private List<Event> createCompetitionEvents() {
        List<Event> events = Arrays.asList(
            new Event("COMP_001", "Basketball Semi-Final 1", "COMPETITION", 120),
            new Event("COMP_002", "Basketball Semi-Final 2", "COMPETITION", 120),
            new Event("COMP_003", "Basketball Final", "COMPETITION", 120),
            new Event("COMP_004", "Swimming Heats", "COMPETITION", 180),
            new Event("COMP_005", "Swimming Finals", "COMPETITION", 120),
            new Event("COMP_006", "Track & Field", "COMPETITION", 240)
        );
        
        events.get(2).setPriority(10); // Finals highest priority
        events.get(4).setPriority(9);
        events.get(0).setPriority(8);
        events.get(1).setPriority(8);
        
        events.get(0).setRoomRequirement("GYM");
        events.get(1).setRoomRequirement("GYM");
        events.get(2).setRoomRequirement("GYM");
        events.get(2).addConflictingEvent("COMP_001"); // Final after semi-finals
        events.get(2).addConflictingEvent("COMP_002");
        
        return events;
    }
    
    private List<Room> createCompetitionVenues() {
        return Arrays.asList(
            new Room("GYM_001", "Main Gymnasium", "GYM", 500),
            new Room("POOL_001", "Swimming Pool", "POOL", 200),
            new Room("TRACK_001", "Athletic Track", "TRACK", 1000)
        );
    }
    
    private List<Participant> createCompetitionParticipants() {
        List<Participant> participants = Arrays.asList(
            new Participant("TEAM_001", "Blue Team", "TEAM"),
            new Participant("TEAM_002", "Red Team", "TEAM"),
            new Participant("ATHLETE_001", "Michael Phelps Jr.", "ATHLETE")
        );
        
        participants.get(0).addRequiredEvent("COMP_001");
        participants.get(0).addRequiredEvent("COMP_003");
        participants.get(1).addRequiredEvent("COMP_002");
        participants.get(1).addRequiredEvent("COMP_003");
        participants.get(2).addRequiredEvent("COMP_004");
        participants.get(2).addRequiredEvent("COMP_005");
        
        return participants;
    }
    
    private List<Event> createMixedEvents() {
        List<Event> mixed = new ArrayList<>();
        mixed.addAll(createConferenceEvents().subList(0, 3));
        mixed.addAll(createExamEvents().subList(0, 3));
        mixed.addAll(createCompetitionEvents().subList(0, 2));
        return mixed;
    }
    
    private List<Room> createMixedRooms() {
        List<Room> mixed = new ArrayList<>();
        mixed.addAll(createConferenceRooms().subList(0, 2));
        mixed.addAll(createExamRooms().subList(0, 2));
        mixed.addAll(createCompetitionVenues().subList(0, 1));
        return mixed;
    }
    
    private List<Participant> createMixedParticipants() {
        List<Participant> mixed = new ArrayList<>();
        mixed.addAll(createConferenceParticipants().subList(0, 2));
        mixed.addAll(createExamParticipants().subList(0, 2));
        mixed.addAll(createCompetitionParticipants().subList(0, 1));
        return mixed;
    }
    
    // Helper methods for algorithm comparison
    private List<Event> copyEvents(List<Event> original) {
        List<Event> copy = new ArrayList<>();
        for (Event event : original) {
            Event newEvent = new Event(event.getId(), event.getName(), event.getType(), event.getDuration());
            newEvent.setPriority(event.getPriority());
            newEvent.setCapacity(event.getCapacity());
            newEvent.setRoomRequirement(event.getRoomRequirement());
            newEvent.setRequiredParticipants(new HashSet<>(event.getRequiredParticipants()));
            newEvent.setConflictingEvents(new HashSet<>(event.getConflictingEvents()));
            copy.add(newEvent);
        }
        return copy;
    }
    
    private List<Room> copyRooms(List<Room> original) {
        List<Room> copy = new ArrayList<>();
        for (Room room : original) {
            Room newRoom = new Room(room.getId(), room.getName(), room.getType(), room.getCapacity());
            newRoom.setFeatures(new HashSet<>(room.getFeatures()));
            copy.add(newRoom);
        }
        return copy;
    }
}