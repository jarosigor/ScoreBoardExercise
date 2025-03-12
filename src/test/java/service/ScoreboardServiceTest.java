package service;

import java.util.List;
import model.Match;
import model.Scoreboard;
import model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("Scoreboard Service test ::")
class ScoreboardServiceTest {

    private final String TEAM_A = "Team A";
    private final String TEAM_B = "Team B";
    private final String TEAM_C = "Team C";
    private final String TEAM_D = "Team D";
    private final String TEAM_E = "Team E";
    private final String TEAM_F = "Team F";

    private ScoreboardService scoreboardService;

    @Nested
    @DisplayName("Test start match functionality")
    class StartNewMatch {

        @BeforeEach
        void setUp() {
            scoreboardService = new ScoreboardService();
            var teams = List.of(new Team(TEAM_A), new Team(TEAM_B), new Team(TEAM_C));
            scoreboardService.getScoreboard().getTeams().addAll(teams);
        }

        @Test
        @DisplayName("Start match with registered teams")
        void validStartNewMatch() {
            scoreboardService.startNewMatch(TEAM_A, TEAM_B);
            assertTrue(scoreboardService.getScoreboard().getMatches().getFirst().getInProgress());
            assertEquals(1, scoreboardService.getScoreboard().getMatches().size());
        }

        @Test
        @DisplayName("Start match with same teams")
        void sameTeams() {
            assertThrows(IllegalStateException.class, () -> {
                scoreboardService.startNewMatch(TEAM_A, TEAM_A);
            });
        }

        @Test
        @DisplayName("Start match with null team names")
        void nullTeamNames() {
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardService.startNewMatch(null, TEAM_B);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardService.startNewMatch(TEAM_A, null);
            });
        }

        @Test
        @DisplayName("Start match with empty team names")
        void emptyTeamNames() {
            assertThrows(IllegalStateException.class, () -> {
                scoreboardService.startNewMatch("", TEAM_B);
            });
            assertThrows(IllegalStateException.class, () -> {
                scoreboardService.startNewMatch(TEAM_A, "");
            });
        }

        @Test
        @DisplayName("Start match when match is already in progress")
        void matchAlreadyInProgress() {
            scoreboardService.startNewMatch(TEAM_A, TEAM_B);
            scoreboardService.startNewMatch(TEAM_A, TEAM_B);
            assertEquals(1, scoreboardService.getScoreboard().getMatches().size());
        }
    }

    @Nested
    @DisplayName("Test update score functionality")
    class UpdateScore {

        @BeforeEach
        void setUp() {
            scoreboardService = new ScoreboardService();
            var teams = List.of(new Team(TEAM_A), new Team(TEAM_B));
            scoreboardService.getScoreboard().getTeams().addAll(teams);
            scoreboardService.startNewMatch(TEAM_A, TEAM_B);
        }

        @Test
        @DisplayName("Update score with valid match and scores")
        void validUpdateScore() {
            Match match = scoreboardService.getScoreboard().getMatches().getFirst();
            scoreboardService.updateScore(match, 1, 0);
            assertEquals(1, match.getScore().getHomeTeamScore());
            assertEquals(0, match.getScore().getAwayTeamScore());
        }

        @Test
        @DisplayName("Update score with null match")
        void nullMatch() {
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardService.updateScore(null, 1, 0);
            });
        }

        @Test
        @DisplayName("Update score for match not in progress")
        void matchNotInProgress() {
            Match match = scoreboardService.getScoreboard().getMatches().getFirst();
            scoreboardService.finishMatch(match);
            assertThrows(IllegalStateException.class, () -> {
                scoreboardService.updateScore(match, 1, 0);
            });
        }

        @Test
        @DisplayName("Update score with negative values")
        void negativeScores() {
            Match match = scoreboardService.getScoreboard().getMatches().getFirst();
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardService.updateScore(match, -1, 0);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardService.updateScore(match, 1, -1);
            });
        }

        @Test
        @DisplayName("Update score with null values")
        void nullScores() {
            Match match = scoreboardService.getScoreboard().getMatches().getFirst();
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardService.updateScore(match, null, 0);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardService.updateScore(match, 1, null);
            });
        }

        @Test
        @DisplayName("Update score when match not in the scoreboard")
        void matchNotInScoreboard() {
            Match match = new Match(new Team("Not in sb"), new Team("Not in scoreboard"));
            assertThrows(IllegalStateException.class, () -> {
                scoreboardService.updateScore(match, 1, 0);
            });
        }
    }

    @Nested
    @DisplayName("Test finish match functionality")
    class FinishMatch {

        @BeforeEach
        void setUp() {
            scoreboardService = new ScoreboardService();
            var teams = List.of(new Team(TEAM_A), new Team(TEAM_B));
            scoreboardService.getScoreboard().getTeams().addAll(teams);
            scoreboardService.startNewMatch(TEAM_A, TEAM_B);
        }

        @Test
        @DisplayName("Finish match with valid match")
        void validFinishMatch() {
            Match match = scoreboardService.getScoreboard().getMatches().getFirst();
            scoreboardService.finishMatch(match);
            assertNotNull(match.getEndTime());
            assertFalse(match.getInProgress());
            assertTrue(scoreboardService.getScoreboard().getMatches().isEmpty());
        }

        @Test
        @DisplayName("Finish match with null match")
        void nullMatch() {
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardService.finishMatch(null);
            });
        }

        @Test
        @DisplayName("Finish match already finished")
        void matchAlreadyFinished() {
            Match match = scoreboardService.getScoreboard().getMatches().getFirst();
            scoreboardService.finishMatch(match);

            assertThrows(IllegalStateException.class, () -> {
                scoreboardService.finishMatch(match);
            });
        }

        @Test
        @DisplayName("Finish match when match not in the scoreboard")
        void matchNotInScoreboard() {
            Match match = new Match(new Team("Not in scoreboard"), new Team("Not in sb"));
            assertThrows(IllegalStateException.class, () -> {
                scoreboardService.finishMatch(match);
            });
        }
    }

    @Nested
    @DisplayName("Test get summary functionality")
    class GetSummary {

        @BeforeEach
        void setUp() {
            scoreboardService = new ScoreboardService();
            var teams = List.of(new Team(TEAM_A), new Team(TEAM_B),
                    new Team(TEAM_C), new Team(TEAM_D), new Team(TEAM_E),
                    new Team(TEAM_F));
            scoreboardService.getScoreboard().getTeams().addAll(teams);
        }

        @Test
        @DisplayName("Get summary with no matches in progress")
        void noMatchesInProgress() {
            String summary = scoreboardService.getSummary();
            assertEquals("", summary);
        }

        @Test
        @DisplayName("Get summary with a single match in progress")
        void singleMatchInProgress() {
            scoreboardService.startNewMatch(TEAM_A, TEAM_B);
            String summary = scoreboardService.getSummary();
            assertTrue(summary.contains(TEAM_A));
            assertTrue(summary.contains(TEAM_B));
        }

        @Test
        @DisplayName("Get summary with multiple matches with different scores")
        void multipleMatchesDifferentScores() {
            scoreboardService.startNewMatch(TEAM_A, TEAM_B);
            scoreboardService.startNewMatch(TEAM_C, TEAM_D);
            scoreboardService.startNewMatch(TEAM_E, TEAM_F);
            Match match1 = scoreboardService.getScoreboard().getMatches().get(0);
            Match match2 = scoreboardService.getScoreboard().getMatches().get(1);
            Match match3 = scoreboardService.getScoreboard().getMatches().get(2);
            scoreboardService.updateScore(match1, 1, 0);
            scoreboardService.updateScore(match2, 7, 4);
            scoreboardService.updateScore(match3, 1, 2);

            String summary = scoreboardService.getSummary();
            assertTrue(summary.indexOf(TEAM_C) < summary.indexOf(TEAM_E));
            assertTrue(summary.indexOf(TEAM_E) < summary.indexOf(TEAM_A));
        }

        @Test
        @DisplayName("Get summary with multiple matches where some have the same score")
        void multipleMatchesSomeWithSameScore() {
            scoreboardService.startNewMatch(TEAM_A, TEAM_B);
            scoreboardService.startNewMatch(TEAM_C, TEAM_D);
            scoreboardService.startNewMatch(TEAM_E, TEAM_F);
            Match match1 = scoreboardService.getScoreboard().getMatches().get(0);
            Match match2 = scoreboardService.getScoreboard().getMatches().get(1);
            Match match3 = scoreboardService.getScoreboard().getMatches().get(2);
            scoreboardService.updateScore(match1, 1, 1);
            scoreboardService.updateScore(match2, 1, 1);
            scoreboardService.updateScore(match3, 4, 0);

            String summary = scoreboardService.getSummary();
            assertTrue(summary.indexOf(TEAM_E) < summary.indexOf(TEAM_A));
            assertTrue(summary.indexOf(TEAM_A) < summary.indexOf(TEAM_C));
        }

        @Test
        @DisplayName("Get summary for example provided in task")
        void exampleFromTask() throws InterruptedException {
            scoreboardService = new ScoreboardService();
            setUpExampleTaskData(scoreboardService.getScoreboard());

            String summary = scoreboardService.getSummary();
            assertTrue(summary.indexOf("Argentina") < summary.indexOf("Germany"));
            assertTrue(summary.indexOf("Mexico") < summary.indexOf("Argentina"));
            assertTrue(summary.indexOf("Spain") < summary.indexOf("Mexico"));
            assertTrue(summary.indexOf("Uruguay") < summary.indexOf("Spain"));

        }

        void setUpExampleTaskData(Scoreboard scoreboard) throws InterruptedException {
            var teams = List.of(new Team("Mexico"), new Team("Canada"),
                    new Team("Spain"), new Team("Brazil"), new Team("Germany"),
                    new Team("France"), new Team("Uruguay"), new Team("Italy"),
                    new Team("Argentina"), new Team("Australia"));
            scoreboard.getTeams().addAll(teams);
            scoreboardService.startNewMatch("Mexico", "Canada");
            scoreboardService.startNewMatch("Spain", "Brazil");
            Thread.sleep(100);
            scoreboardService.startNewMatch("Germany", "France");
            scoreboardService.startNewMatch("Uruguay", "Italy");
            Thread.sleep(100);
            scoreboardService.startNewMatch("Argentina", "Australia");
            Match match1 = scoreboardService.getScoreboard().getMatches().get(0);
            Match match2 = scoreboardService.getScoreboard().getMatches().get(1);
            Match match3 = scoreboardService.getScoreboard().getMatches().get(2);
            Match match4 = scoreboardService.getScoreboard().getMatches().get(3);
            Match match5 = scoreboardService.getScoreboard().getMatches().get(4);
            scoreboardService.updateScore(match1, 0, 5);
            scoreboardService.updateScore(match2, 10, 2);
            scoreboardService.updateScore(match3, 2, 2);
            scoreboardService.updateScore(match4, 6, 6);
            scoreboardService.updateScore(match5, 3, 1);
        }
    }

}
