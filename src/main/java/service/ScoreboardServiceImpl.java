package service;

import java.util.Collections;
import java.util.Date;
import lombok.Getter;
import model.Match;
import model.Scoreboard;
import model.Team;

/***
 * Main class that implements the ScoreBoardService interface and provides the main functionality of the scoreboard.
 */
@Getter
public class ScoreboardServiceImpl implements ScoreBoardService {

    private final Scoreboard scoreboard;

    public ScoreboardServiceImpl() {
        scoreboard = new Scoreboard();
    }

    public Match startNewMatch(String homeTeamName, String awayTeamName) {
        var awayTeam = new Team(awayTeamName);
        var homeTeam = new Team(homeTeamName);
        var match = new Match(homeTeam, awayTeam);
        match.setStartTime(new Date());
        match.setInProgress(true);
        ScoreBoardValidation.checkMatchIsValid(match);
        addNewTeams(homeTeam, awayTeam);
        if (ScoreBoardValidation.isMatchNotInTheScoreboard(match, scoreboard)) {
            scoreboard.getMatches().add(match);
        }
        return match;
    }

    public void updateScore(Match match, Integer homeTeamScore, Integer awayTeamScore) {
        ScoreBoardValidation.checkMatchIsValid(match);
        ScoreBoardValidation.checkMatchIsInScoreboard(match, scoreboard);
        ScoreBoardValidation.checkScoreUpdate(match, homeTeamScore, awayTeamScore);
        match.getScore().setHomeTeamScore(homeTeamScore);
        match.getScore().setAwayTeamScore(awayTeamScore);
    }

    public void finishMatch(Match match) {
        ScoreBoardValidation.checkMatchIsValid(match);
        ScoreBoardValidation.checkMatchIsInScoreboard(match, scoreboard);
        match.setEndTime(new Date());
        match.setInProgress(false);
        scoreboard.getMatches().remove(match);
        updateTeamsScore(match);
    }

    public String getSummary() {
        Collections.sort(scoreboard.getMatches());
        return scoreboard.toString();
    }

    public Match getMatch(String homeTeamName, String awayTeamName) {
        return scoreboard.getMatches().stream()
                .filter(match -> match.equals(new Match(new Team(homeTeamName), new Team(awayTeamName))))
                .findFirst()
                .orElse(null);
    }

    private void addNewTeams(Team homeTeam, Team awayTeam) {
        addNewTeam(homeTeam);
        addNewTeam(awayTeam);
    }

    private void addNewTeam(Team team) {
        if (!ScoreBoardValidation.isTeamAlreadyCreated(team.getName(), scoreboard)) {
            scoreboard.getTeams().add(team);
        }
    }

    private void updateTeamsScore(Match match) {
        var homeTeamScore = match.getScore().getHomeTeamScore();
        var awayTeamScore = match.getScore().getAwayTeamScore();
        if (homeTeamScore > awayTeamScore) {
            match.getHomeTeam().setWins(match.getHomeTeam().getWins() + 1);
            match.getAwayTeam().setLosses(match.getAwayTeam().getLosses() + 1);
        } else if (homeTeamScore < awayTeamScore) {
            match.getAwayTeam().setWins(match.getAwayTeam().getWins() + 1);
            match.getHomeTeam().setLosses(match.getHomeTeam().getLosses() + 1);
        } else {
            match.getHomeTeam().setDraws(match.getHomeTeam().getDraws() + 1);
            match.getAwayTeam().setDraws(match.getAwayTeam().getDraws() + 1);
        }
    }
}
