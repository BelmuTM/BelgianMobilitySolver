# Belgian Mobility Solver

## About

**Belgian Mobility Solver** is a Java implementation of the Dijkstra pathfinding algorithm adapted to GTFS data from Belgian transport companies.
It enables the user to find the fastest possible public transport trip between two stops, even across different companies, at a specified departure time.

The program outputs all the trips necessary to travel to the destination.

## Prerequisites

- A system with a recent Java Development Kit (JDK) installed - **JDK 21 or newer** is recommended.
You can download it from [Oracle](https://www.oracle.com/be/java/technologies/downloads/) or use a distribution like [OpenJDK](https://openjdk.org/).

- An internet connection to download Gradle dependencies (the first time you build).

## Compiling

Navigate to the project's directory and run this command from your terminal:
- `gradlew build`

## Executing

Run the executable using the following command in the project's directory:
- `java -jar build/libs/BelgianMobilitySolver.jar "Departure Stop" "Destination Stop" "HH:mm:ss"`

Example:
- `java -jar build/libs/BelgianMobilitySolver.jar "Alveringem Nieuwe Herberg" "Aubange" "10:30:00"`
