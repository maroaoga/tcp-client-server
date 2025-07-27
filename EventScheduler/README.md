# Event Scheduler - Discrete Structures Implementation

A Java NetBeans project that demonstrates the use of discrete structures and graph theory to solve real-world scheduling problems including conference talks, final exams, and sports competitions.

## Project Overview

This application addresses the fundamental scheduling problems you encounter in organizing events:

- **Conference Scheduling**: How to schedule talks with speaker and room constraints
- **Exam Scheduling**: How to schedule final exams ensuring no student has conflicts
- **Competition Scheduling**: How to schedule sports events with venue and participant constraints

## Discrete Structures Concepts Used

### 1. Graph Theory
- **Conflict Graph**: Events are represented as vertices, conflicts as edges
- **Graph Coloring**: Different colors represent different time slots
- **Chromatic Number**: Minimum number of time slots needed

### 2. Graph Coloring Algorithms
- **Welsh-Powell Algorithm**: Greedy coloring based on vertex degree
- **DSATUR Algorithm**: Dynamic saturation degree for better coloring
- **Priority-Based Greedy**: Considers event priorities in scheduling

### 3. Constraint Satisfaction
- **Participant Conflicts**: No person can attend two events simultaneously
- **Room Capacity**: Events must fit in assigned rooms
- **Time Windows**: All events must fit within available time periods

### 4. Optimization Techniques
- **Best-Fit Room Assignment**: Optimal room allocation strategy
- **Clique Detection**: Finding groups of mutually conflicting events
- **Efficiency Metrics**: Measuring scheduling success rates

## Project Structure

```
EventScheduler/
├── src/main/java/com/eventscheduler/
│   ├── EventSchedulerApp.java          # Main application
│   ├── model/
│   │   ├── Event.java                  # Event entity
│   │   ├── Room.java                   # Room entity
│   │   └── Participant.java            # Participant entity
│   └── algorithm/
│       ├── ConflictGraph.java          # Graph representation
│       ├── SchedulingAlgorithm.java    # Algorithm interface
│       └── GraphColoringScheduler.java # Graph coloring implementation
├── pom.xml                             # Maven configuration
└── README.md                           # This file
```

## How to Run in NetBeans

### Prerequisites
- NetBeans IDE 12.0 or later
- Java 11 or later
- Maven (usually included with NetBeans)

### Steps
1. **Open Project in NetBeans**:
   - File → Open Project
   - Navigate to the `EventScheduler` folder
   - Click "Open Project"

2. **Build the Project**:
   - Right-click on the project in the Projects window
   - Select "Build" or press F11

3. **Run the Application**:
   - Right-click on `EventSchedulerApp.java`
   - Select "Run File" or press Shift+F6
   - Or set it as the main class and press F6

### Expected Output
The application will run three different scheduling scenarios and compare algorithms:

1. **Conference Scheduling Demo**
2. **Exam Scheduling Demo** 
3. **Sports Competition Scheduling Demo**
4. **Algorithm Comparison**

## Key Features Demonstrated

### Real-World Scheduling Problems

1. **Conference Organization**:
   - Schedule keynote speakers, regular talks, workshops
   - Handle speaker conflicts and room requirements
   - Prioritize important sessions

2. **Academic Exam Scheduling**:
   - Ensure no student has conflicting exams
   - Respect room capacity constraints
   - Minimize scheduling conflicts

3. **Sports Competition Management**:
   - Schedule tournaments with venue constraints
   - Handle team participation requirements
   - Manage sequential events (semi-finals before finals)

### Discrete Mathematics Applications

- **Graph Coloring Theory**: Converts scheduling to a well-known CS problem
- **Clique Detection**: Identifies groups of mutually conflicting events
- **Greedy Algorithms**: Provides efficient approximate solutions
- **Constraint Satisfaction**: Models real-world restrictions mathematically

## Algorithm Details

### Welsh-Powell Algorithm
```
1. Sort vertices by degree (descending)
2. For each vertex:
   - Find colors used by neighbors
   - Assign lowest available color
```

### DSATUR Algorithm
```
1. While uncolored vertices exist:
   - Select vertex with highest saturation degree
   - Break ties by selecting highest degree vertex
   - Assign lowest available color
```

### Room Assignment Strategy
```
1. Filter rooms that can accommodate the event
2. Score rooms based on:
   - Capacity utilization efficiency
   - Room type matching
3. Select highest scoring room
```

## Sample Output

```
====================================================
EVENT SCHEDULER - Discrete Structures Implementation
====================================================

CONFERENCE SCHEDULING DEMO
Using Graph Coloring to schedule conference talks

SchedulingResult{
  Algorithm: Graph Coloring (WELSH_POWELL)
  Scheduled: 7/7 events (100.00%)
  Time slots used: 4
  Scheduling efficiency: 100.00%
  Execution time: 15 ms
}

SCHEDULED EVENTS:

Jun 15, 09:00:
  CONFERENCE       Future of Technology      (Room: ROOM_A, 60 min)

Jun 15, 10:15:
  CONFERENCE       AI in Healthcare          (Room: ROOM_B, 45 min)
  CONFERENCE       Blockchain Technologies   (Room: ROOM_C, 45 min)

...
```

## Educational Value

This project demonstrates:

1. **Practical Application**: Shows how abstract math concepts solve real problems
2. **Algorithm Comparison**: Different approaches yield different results
3. **Complexity Analysis**: Trade-offs between solution quality and computation time
4. **Constraint Modeling**: How to translate real-world rules into mathematical constraints

## Extensions and Improvements

Possible enhancements for learning:

1. **Additional Algorithms**: Implement backtracking or genetic algorithms
2. **GUI Interface**: Create a visual scheduling interface
3. **Database Integration**: Store and retrieve scheduling data
4. **Optimization**: Add multi-objective optimization (cost, preferences, etc.)
5. **Real-time Updates**: Handle dynamic scheduling changes

## Academic Context

This project illustrates key concepts from:

- **Discrete Mathematics**: Graph theory, combinatorics
- **Algorithms**: Greedy algorithms, approximation algorithms
- **Data Structures**: Graphs, sets, hash maps
- **Constraint Programming**: Modeling and solving CSPs
- **Operations Research**: Scheduling optimization

Perfect for demonstrating how theoretical computer science concepts apply to practical, everyday problems!