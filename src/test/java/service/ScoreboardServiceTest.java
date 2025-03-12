package service;

import model.Match;
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

    private ScoreboardServiceImpl scoreboardServiceImpl;

    @Nested
    @DisplayName("Test start match functionality")
    class StartNewMatch {

        @BeforeEach
        void setUp() {
            scoreboardServiceImpl = new ScoreboardServiceImpl();
        }

        @Test
        @DisplayName("Start match with registered teams")
        void validStartNewMatch() {
            scoreboardServiceImpl.startNewMatch(TEAM_A, TEAM_B);
            assertTrue(scoreboardServiceImpl.getScoreboard().getMatches().getFirst().getInProgress());
            assertEquals(1, scoreboardServiceImpl.getScoreboard().getMatches().size());
        }

        @Test
        @DisplayName("Start match with same teams")
        void sameTeams() {
            assertThrows(IllegalStateException.class, () -> {
                scoreboardServiceImpl.startNewMatch(TEAM_A, TEAM_A);
            });
        }

        @Test
        @DisplayName("Start match with null team names")
        void nullTeamNames() {
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardServiceImpl.startNewMatch(null, TEAM_B);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardServiceImpl.startNewMatch(TEAM_A, null);
            });
        }

        @Test
        @DisplayName("Start match with empty team names")
        void emptyTeamNames() {
            assertThrows(IllegalStateException.class, () -> {
                scoreboardServiceImpl.startNewMatch("", TEAM_B);
            });
            assertThrows(IllegalStateException.class, () -> {
                scoreboardServiceImpl.startNewMatch(TEAM_A, "");
            });
        }

        @Test
        @DisplayName("Start match when match is already in progress")
        void matchAlreadyInProgress() {
            scoreboardServiceImpl.startNewMatch(TEAM_A, TEAM_B);
            scoreboardServiceImpl.startNewMatch(TEAM_A, TEAM_B);
            assertEquals(1, scoreboardServiceImpl.getScoreboard().getMatches().size());
        }
    }

    @Nested
    @DisplayName("Test update score functionality")
    class UpdateScore {

        @BeforeEach
        void setUp() {
            scoreboardServiceImpl = new ScoreboardServiceImpl();
            scoreboardServiceImpl.startNewMatch(TEAM_A, TEAM_B);
        }

        @Test
        @DisplayName("Update score with valid match and scores")
        void validUpdateScore() {
            Match match = scoreboardServiceImpl.getScoreboard().getMatches().getFirst();
            scoreboardServiceImpl.updateScore(match, 1, 0);
            assertEquals(1, match.getScore().getHomeTeamScore());
            assertEquals(0, match.getScore().getAwayTeamScore());
        }

        @Test
        @DisplayName("Update score with null match")
        void nullMatch() {
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardServiceImpl.updateScore(null, 1, 0);
            });
        }

        @Test
        @DisplayName("Update score for match not in progress")
        void matchNotInProgress() {
            Match match = scoreboardServiceImpl.getScoreboard().getMatches().getFirst();
            scoreboardServiceImpl.finishMatch(match);
            assertThrows(IllegalStateException.class, () -> {
                scoreboardServiceImpl.updateScore(match, 1, 0);
            });
        }

        @Test
        @DisplayName("Update score with negative values")
        void negativeScores() {
            Match match = scoreboardServiceImpl.getScoreboard().getMatches().getFirst();
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardServiceImpl.updateScore(match, -1, 0);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardServiceImpl.updateScore(match, 1, -1);
            });
        }

        @Test
        @DisplayName("Update score with null values")
        void nullScores() {
            Match match = scoreboardServiceImpl.getScoreboard().getMatches().getFirst();
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardServiceImpl.updateScore(match, null, 0);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardServiceImpl.updateScore(match, 1, null);
            });
        }

        @Test
        @DisplayName("Update score when match not in the scoreboard")
        void matchNotInScoreboard() {
            Match match = new Match(new Team("Not in sb"), new Team("Not in scoreboard"));
            assertThrows(IllegalStateException.class, () -> {
                scoreboardServiceImpl.updateScore(match, 1, 0);
            });
        }
    }

    @Nested
    @DisplayName("Test finish match functionality")
    class FinishMatch {

        @BeforeEach
        void setUp() {
            scoreboardServiceImpl = new ScoreboardServiceImpl();
            scoreboardServiceImpl.startNewMatch(TEAM_A, TEAM_B);
        }

        @Test
        @DisplayName("Finish match with valid match")
        void validFinishMatch() {
            Match match = scoreboardServiceImpl.getScoreboard().getMatches().getFirst();
            scoreboardServiceImpl.finishMatch(match);
            assertNotNull(match.getEndTime());
            assertFalse(match.getInProgress());
            assertTrue(scoreboardServiceImpl.getScoreboard().getMatches().isEmpty());
        }

        @Test
        @DisplayName("Finish match with null match")
        void nullMatch() {
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardServiceImpl.finishMatch(null);
            });
        }

        @Test
        @DisplayName("Finish match already finished")
        void matchAlreadyFinished() {
            Match match = scoreboardServiceImpl.getScoreboard().getMatches().getFirst();
            scoreboardServiceImpl.finishMatch(match);

            assertThrows(IllegalStateException.class, () -> {
                scoreboardServiceImpl.finishMatch(match);
            });
        }

        @Test
        @DisplayName("Finish match when match not in the scoreboard")
        void matchNotInScoreboard() {
            Match match = new Match(new Team("Not in scoreboard"), new Team("Not in sb"));
            assertThrows(IllegalStateException.class, () -> {
                scoreboardServiceImpl.finishMatch(match);
            });
        }
    }

    @Nested
    @DisplayName("Test get summary functionality")
    class GetSummary {

        @BeforeEach
        void setUp() {
            scoreboardServiceImpl = new ScoreboardServiceImpl();
        }

        @Test
        @DisplayName("Get summary with no matches in progress")
        void noMatchesInProgress() {
            String summary = scoreboardServiceImpl.getSummary();
            assertEquals("", summary);
        }

        @Test
        @DisplayName("Get summary with a single match in progress")
        void singleMatchInProgress() {
            scoreboardServiceImpl.startNewMatch(TEAM_A, TEAM_B);
            String summary = scoreboardServiceImpl.getSummary();
            assertTrue(summary.contains(TEAM_A));
            assertTrue(summary.contains(TEAM_B));
        }

        @Test
        @DisplayName("Get summary with multiple matches with different scores")
        void multipleMatchesDifferentScores() {
            var match1 = scoreboardServiceImpl.startNewMatch(TEAM_A, TEAM_B);
            var match2 = scoreboardServiceImpl.startNewMatch(TEAM_C, TEAM_D);
            var match3 = scoreboardServiceImpl.startNewMatch(TEAM_E, TEAM_F);

            scoreboardServiceImpl.updateScore(match1, 1, 0);
            scoreboardServiceImpl.updateScore(match2, 7, 4);
            scoreboardServiceImpl.updateScore(match3, 1, 2);

            String summary = scoreboardServiceImpl.getSummary();
            assertTrue(summary.indexOf(TEAM_C) < summary.indexOf(TEAM_E));
            assertTrue(summary.indexOf(TEAM_E) < summary.indexOf(TEAM_A));
        }

        @Test
        @DisplayName("Get summary with multiple matches where some have the same score")
        void multipleMatchesSomeWithSameScore() throws InterruptedException {
            var match1 = scoreboardServiceImpl.startNewMatch(TEAM_A, TEAM_B);
            var match2 = scoreboardServiceImpl.startNewMatch(TEAM_C, TEAM_D);
            var match3 = scoreboardServiceImpl.startNewMatch(TEAM_E, TEAM_F);

            scoreboardServiceImpl.updateScore(match1, 1, 1);
            Thread.sleep(100);
            scoreboardServiceImpl.updateScore(match2, 1, 1);
            scoreboardServiceImpl.updateScore(match3, 4, 0);

            String summary = scoreboardServiceImpl.getSummary();
            assertTrue(summary.indexOf(TEAM_E) < summary.indexOf(TEAM_A));
            assertTrue(summary.indexOf(TEAM_A) < summary.indexOf(TEAM_C));
        }

        @Test
        @DisplayName("Get summary for example provided in task")
        void exampleFromTask() throws InterruptedException {
            scoreboardServiceImpl = new ScoreboardServiceImpl();
            setUpExampleTaskData();

            String summary = scoreboardServiceImpl.getSummary();
            assertTrue(summary.indexOf("Argentina") < summary.indexOf("Germany"));
            assertTrue(summary.indexOf("Mexico") < summary.indexOf("Argentina"));
            assertTrue(summary.indexOf("Spain") < summary.indexOf("Mexico"));
            assertTrue(summary.indexOf("Uruguay") < summary.indexOf("Spain"));

        }

        void setUpExampleTaskData() throws InterruptedException {
            var match1 = scoreboardServiceImpl.startNewMatch("Mexico", "Canada");
            var match2 = scoreboardServiceImpl.startNewMatch("Spain", "Brazil");
            Thread.sleep(100);
            var match3 = scoreboardServiceImpl.startNewMatch("Germany", "France");
            var match4 = scoreboardServiceImpl.startNewMatch("Uruguay", "Italy");
            Thread.sleep(100);
            var match5 = scoreboardServiceImpl.startNewMatch("Argentina", "Australia");
            scoreboardServiceImpl.updateScore(match1, 0, 5);
            scoreboardServiceImpl.updateScore(match2, 10, 2);
            scoreboardServiceImpl.updateScore(match3, 2, 2);
            scoreboardServiceImpl.updateScore(match4, 6, 6);
            scoreboardServiceImpl.updateScore(match5, 3, 1);
        }
    }

}
