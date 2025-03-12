package service;

import model.Match;
import model.Scoreboard;

public interface ScoreBoardService {

    Match startNewMatch(String homeTeamName, String awayTeamName);

    void updateScore(Match match, Integer homeTeamScore, Integer awayTeamScore);

    void finishMatch(Match match);

    String getSummary();

    Scoreboard getScoreboard();

    Match getMatch(String homeTeamName, String awayTeamName);
}
