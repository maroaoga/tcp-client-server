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
- **Vertex Degree**: Number of conflicts an event has with other events

### 2. Algorithms Implemented
- **Welsh-Powell Algorithm**: Graph coloring by sorting vertices by degree
- **Greedy Priority Algorithm**: Prioritize high-importance events first
- **DSATUR Algorithm**: Dynamic saturation graph coloring

### 3. Constraint Satisfaction
- **Participant Conflicts**: Two events conflict if they share participants
- **Room Constraints**: Events must fit in appropriate room types with sufficient capacity
- **Time Windows**: Events must be scheduled within available time periods

## How to Run in NetBeans

### Method 1: Open as Maven Project
1. Open NetBeans IDE
2. Go to `File` → `Open Project`
3. Navigate to the `EventScheduler` folder
4. Select the project (NetBeans will recognize it as a Maven project)
5. Right-click on the project → `Run`

### Method 2: Import Existing Project
1. In NetBeans, go to `File` → `Import Project` → `From Folder`
2. Browse to the EventScheduler directory
3. NetBeans will automatically detect the Maven structure
4. Build and run the project

### Method 3: Command Line (if NetBeans is not available)
```bash
cd EventScheduler
mvn clean compile exec:java -Dexec.mainClass="com.eventscheduler.EventSchedulerApp"
```

## Project Structure

```
EventScheduler/
├── pom.xml                     # Maven configuration
├── nbproject/                  # NetBeans project files
├── src/main/java/
│   └── com/eventscheduler/
│       ├── EventSchedulerApp.java           # Main application
│       ├── model/
│       │   ├── Event.java                   # Event data model
│       │   ├── Room.java                    # Room data model
│       │   ├── Participant.java             # Participant data model
│       │   └── TimeSlot.java                # Time slot representation
│       └── algorithm/
│           ├── SchedulingAlgorithm.java     # Algorithm interface
│           ├── GraphColoringScheduler.java  # Graph coloring implementation
│           ├── ConflictGraph.java           # Conflict graph data structure
│           └── SchedulingResult.java        # Result wrapper
└── README.md
```

## How the Scheduling Algorithm Works

### Step 1: Build Conflict Graph
- Create a vertex for each event
- Add an edge between two events if they conflict (share participants, rooms, or time requirements)

### Step 2: Graph Coloring
- Apply a graph coloring algorithm to assign time slots (colors) to events
- No two adjacent vertices (conflicting events) can have the same color (time slot)

### Step 3: Room Assignment
- For each time slot, assign rooms to events based on:
  - Room capacity requirements
  - Room type requirements (lecture hall, lab, gym, etc.)
  - Room availability

### Step 4: Validation
- Verify all constraints are satisfied
- Report scheduling efficiency and unresolved conflicts

## Example Output

When you run the application, you'll see three scheduling scenarios:

1. **Conference Scheduling**: Demonstrates scheduling academic conference talks with speaker conflicts
2. **Exam Scheduling**: Shows how to schedule final exams ensuring no student has conflicting exams
3. **Sports Competition Scheduling**: Illustrates venue-constrained sports event scheduling

Each scenario shows:
- Number of events successfully scheduled
- Time slots used
- Room assignments
- Unresolved conflicts and their reasons
- Algorithm performance metrics

## Educational Value

This project demonstrates several key discrete structures concepts:

1. **Graph Theory**: Practical application of graphs to model real-world constraints
2. **Graph Coloring**: A classic NP-hard problem with practical solutions
3. **Constraint Satisfaction**: How to model and solve complex constraint problems
4. **Algorithm Analysis**: Comparing different algorithmic approaches
5. **Data Structures**: Using sets, maps, and graphs effectively

## Customization

You can easily modify the project to test different scenarios:

- Add more events in `EventSchedulerApp.java`
- Modify conflict detection logic in `ConflictGraph.java`
- Implement new scheduling algorithms by extending `SchedulingAlgorithm`
- Add new constraint types in the model classes

## Requirements

- Java 11 or higher
- Maven 3.6 or higher
- NetBeans IDE 12 or higher (recommended)

## Building and Testing

```bash
# Compile the project
mvn compile

# Run tests (if any)
mvn test

# Run the application
mvn exec:java -Dexec.mainClass="com.eventscheduler.EventSchedulerApp"

# Create JAR file
mvn package
```

This project provides a comprehensive example of how discrete structures and graph theory can solve complex real-world scheduling problems efficiently.
