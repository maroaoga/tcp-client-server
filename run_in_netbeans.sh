#!/bin/bash

echo "=========================================="
echo "EventScheduler - NetBeans Project Setup"
echo "=========================================="
echo ""

# Check if NetBeans is installed
if command -v netbeans &> /dev/null; then
    echo "✅ NetBeans found!"
    echo "Opening project in NetBeans..."
    netbeans EventScheduler
else
    echo "❌ NetBeans not found in PATH"
    echo ""
    echo "To run this project in NetBeans:"
    echo "1. Install NetBeans IDE (version 12 or higher)"
    echo "2. Open NetBeans"
    echo "3. Go to File → Open Project"
    echo "4. Navigate to and select the 'EventScheduler' folder"
    echo "5. Right-click on the project → Run"
    echo ""
    echo "Alternatively, you can run from command line:"
    echo "  cd EventScheduler"
    echo "  mvn clean compile exec:java -Dexec.mainClass=\"com.eventscheduler.EventSchedulerApp\""
fi

echo ""
echo "Project structure:"
echo "  EventScheduler/"
echo "  ├── pom.xml                 (Maven configuration)"
echo "  ├── nbproject/              (NetBeans project files)"
echo "  ├── src/main/java/          (Java source code)"
echo "  └── README.md               (Project documentation)"
echo ""
echo "The project demonstrates:"
echo "  🎯 Conference scheduling with speaker conflicts"
echo "  📚 Exam scheduling ensuring no student conflicts"
echo "  🏆 Sports competition scheduling with venue constraints"
echo "  📊 Graph coloring algorithms (Welsh-Powell, DSATUR, Greedy)"
echo ""
echo "Happy coding! 🚀"