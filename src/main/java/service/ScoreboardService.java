package service;

import lombok.Getter;
import model.Match;
import model.Scoreboard;

@Getter
public class ScoreboardService {

    private final Scoreboard scoreboard;

    public ScoreboardService() {
        scoreboard = new Scoreboard();
    }

    public void startNewMatch(String homeTeamName, String awayTeamName) {
    }

    public void updateScore(Match match, Integer homeTeamScore, Integer awayTeamScore) {
    }

    public void finishMatch(Match match) {
    }

    public String getSummary() {
        return null;
    }

}
