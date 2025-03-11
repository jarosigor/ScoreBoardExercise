package service;

import java.util.List;
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

    private ScoreboardService scoreboardService;

    @Nested
    @DisplayName("Test start match functionality")
    class StartNewMatch {

        @BeforeEach
        public void setUp() {
            scoreboardService = new ScoreboardService();
            var teams = List.of(new Team("Team A"), new Team("Team B"), new Team("Team C"));
            scoreboardService.getScoreboard().getTeams().addAll(teams);
        }

        @Test
        @DisplayName("Start match with registered teams")
        void validStartNewMatch() {
            scoreboardService.startNewMatch("Team A", "Team B");
            assertEquals(1, scoreboardService.getScoreboard().getMatches().size());
        }

        @Test
        @DisplayName("Start match with non-registered teams")
        void notRegisteredTeams() {
            scoreboardService.startNewMatch("Team D", "Team E");
            assertEquals(0, scoreboardService.getScoreboard().getMatches().size());
        }

        @Test
        @DisplayName("Start match with same teams")
        void sameTeams() {
            scoreboardService.startNewMatch("Team A", "Team A");
            assertEquals(0, scoreboardService.getScoreboard().getMatches().size());
        }

        @Test
        @DisplayName("Start match with null team names")
        void nullTeamNames() {
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardService.startNewMatch(null, "Team B");
            });
            assertThrows(IllegalArgumentException.class, () -> {
                scoreboardService.startNewMatch("Team A", null);
            });
        }

        @Test
        @DisplayName("Start match with empty team names")
        void emptyTeamNames() {
            scoreboardService.startNewMatch("", "Team B");
            assertEquals(0, scoreboardService.getScoreboard().getMatches().size());
            scoreboardService.startNewMatch("Team A", "");
            assertEquals(0, scoreboardService.getScoreboard().getMatches().size());
        }

        @Test
        @DisplayName("Start match when match is already in progress")
        void matchAlreadyInProgress() {
            scoreboardService.startNewMatch("Team A", "Team B");
            scoreboardService.startNewMatch("Team A", "Team B");
            assertEquals(1, scoreboardService.getScoreboard().getMatches().size());
        }
    }

    @Nested
    @DisplayName("Test update score functionality")
    class UpdateScore {

        @BeforeEach
        public void setUp() {
            scoreboardService = new ScoreboardService();
            var teams = List.of(new Team("Team A"), new Team("Team B"));
            scoreboardService.getScoreboard().getTeams().addAll(teams);
            scoreboardService.startNewMatch("Team A", "Team B");
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
    }

    @Nested
    @DisplayName("Test finish match functionality")
    class FinishMatch {

        @BeforeEach
        public void setUp() {
            scoreboardService = new ScoreboardService();
            var teams = List.of(new Team("Team A"), new Team("Team B"));
            scoreboardService.getScoreboard().getTeams().addAll(teams);
            scoreboardService.startNewMatch("Team A", "Team B");
        }

        @Test
        @DisplayName("Finish match with valid match")
        void validFinishMatch() {
            Match match = scoreboardService.getScoreboard().getMatches().getFirst();
            scoreboardService.finishMatch(match);
            assertNotNull(match.getEndTime());
            assertFalse(match.getInProgress());
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
    }

    @Nested
    @DisplayName("Test get summary functionality")
    class GetSummary {

        @BeforeEach
        public void setUp() {
            scoreboardService = new ScoreboardService();
            var teams = List.of(new Team("Team A"), new Team("Team B"),
                    new Team("Team C"), new Team("Team D"), new Team("Team E"),
                    new Team("Team F"));
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
            scoreboardService.startNewMatch("Team A", "Team B");
            String summary = scoreboardService.getSummary();
            assertTrue(summary.contains("Team A"));
            assertTrue(summary.contains("Team B"));
        }

        @Test
        @DisplayName("Get summary with multiple matches with different scores")
        void multipleMatchesDifferentScores() {
            scoreboardService.startNewMatch("Team A", "Team B");
            scoreboardService.startNewMatch("Team C", "Team D");
            scoreboardService.startNewMatch("Team E", "Team F");
            Match match1 = scoreboardService.getScoreboard().getMatches().get(0);
            Match match2 = scoreboardService.getScoreboard().getMatches().get(1);
            Match match3 = scoreboardService.getScoreboard().getMatches().get(2);
            scoreboardService.updateScore(match1, 1, 0);
            scoreboardService.updateScore(match2, 7, 4);
            scoreboardService.updateScore(match3, 1, 2);

            String summary = scoreboardService.getSummary();
            assertTrue(summary.indexOf("Team C") < summary.indexOf("Team E"));
            assertTrue(summary.indexOf("Team E") < summary.indexOf("Team A"));
        }

        @Test
        @DisplayName("Get summary with multiple matches where some have the same score")
        void multipleMatchesSomeWithSameScore() {
            scoreboardService.startNewMatch("Team A", "Team B");
            scoreboardService.startNewMatch("Team C", "Team D");
            scoreboardService.startNewMatch("Team E", "Team F");
            Match match1 = scoreboardService.getScoreboard().getMatches().get(0);
            Match match2 = scoreboardService.getScoreboard().getMatches().get(1);
            Match match3 = scoreboardService.getScoreboard().getMatches().get(2);
            scoreboardService.updateScore(match1, 1, 1);
            scoreboardService.updateScore(match2, 1, 1);
            scoreboardService.updateScore(match3, 4, 0);

            String summary = scoreboardService.getSummary();
            assertTrue(summary.indexOf("Team E") < summary.indexOf("Team A"));
            assertTrue(summary.indexOf("Team A") < summary.indexOf("Team C"));
        }
    }

}
