# Live Football World Cup Scoreboard Library

## Overview
This project is a Java-based library that provides real-time tracking of live football matches during the World Cup. It allows users to start matches, update scores, finish matches, and retrieve a summary of ongoing matches ordered by total score and recency.

## Features
- Start a new match with an initial score of 0-0.
- Update the score of an ongoing match with absolute values.
- Finish a match and remove it from the scoreboard.
- Retrieve a summary of ongoing matches ordered by total score, with ties resolved by most recent start time.

## Technologies Used
- **Java 21**
- **Maven** (for dependency management and build automation)
- **JUnit 5** (for unit testing)
- **Lombok** (to reduce boilerplate code)

## Installation
### Prerequisites
Ensure you have the following installed:
- [Java 21](https://jdk.java.net/21/)
- [Apache Maven](https://maven.apache.org/)

### Build & Run
1. Clone the repository:
   ```sh
   git clone https://github.com/jarosigor/ScoreBoardExercise.git
   cd <repository-folder>
   ```
2. Build the project using Maven:
   ```sh
   mvn clean install
   ```
3. Run tests to verify the implementation:
   ```sh
   mvn test
   ```

## Usage

### Create ScoreBoardService
```java
ScoreBoardService scoreboardService = new ScoreboardServiceImpl();
```
### Get ScoreBoard
```java
Scoreboard scoreboard = scoreboardService.getScoreboard();
```

### Start New Match
```java
scoreboardService.startNewMatch("Team A","Team B");
```

### Get Ongoing Match
```java
scoreboardService.getMatch("Team A", "Team B");
```
### Update Score
```java
Match matchToUpdate = scoreboardService.getMatch("Team A", "Team B");
scoreboardService.updateScore(matchToUpdate, 1, 0);
```

### Finish Match
```java
Match matchToFinish = scoreboardService.getMatch("Team A", "Team B");
scoreboardService.finishMatch(matchToFinish);
```

### Get Summary as a String
```java
String summary = scoreboard.getSummary();
System.out.println(summary);
```

## Testing
This project uses JUnit 5 for unit testing. To run the tests:
```sh
mvn test
```

## License
This project is licensed under the MIT License.

## Contact
For any inquiries, please contact jaroszigor01@gmail.com.
