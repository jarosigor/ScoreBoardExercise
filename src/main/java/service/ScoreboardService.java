package service;

import java.util.Date;
import lombok.Getter;
import model.Match;
import model.Scoreboard;
import model.Team;

@Getter
public class ScoreboardService {

    private final Scoreboard scoreboard;

    public ScoreboardService() {
        scoreboard = new Scoreboard();
    }

    public void startNewMatch(String homeTeamName, String awayTeamName) {
        var awayTeam = new Team(awayTeamName);
        var homeTeam = new Team(homeTeamName);
        var match = new Match(homeTeam, awayTeam);
        match.setInProgress(true);
        ScoreBoardValidationService.checkMatchIsValid(match);
        addNewTeams(homeTeam, awayTeam);
        if (ScoreBoardValidationService.isMatchNotInTheScoreboard(match, scoreboard)) {
            scoreboard.getMatches().add(match);
        }
    }

    public void updateScore(Match match, Integer homeTeamScore, Integer awayTeamScore) {
        ScoreBoardValidationService.checkMatchIsValid(match);
        ScoreBoardValidationService.checkMatchIsInScoreboard(match, scoreboard);
        ScoreBoardValidationService.checkScoreUpdate(match, homeTeamScore, awayTeamScore);
        match.getScore().setHomeTeamScore(homeTeamScore);
        match.getScore().setAwayTeamScore(awayTeamScore);
    }

    public void finishMatch(Match match) {
        ScoreBoardValidationService.checkMatchIsValid(match);
        ScoreBoardValidationService.checkMatchIsInScoreboard(match, scoreboard);
        match.setEndTime(new Date());
        match.setInProgress(false);
        scoreboard.getMatches().remove(match);
    }

    public String getSummary() {
        return null;
    }

    private void addNewTeams(Team homeTeam, Team awayTeam) {
        addNewTeam(homeTeam);
        addNewTeam(awayTeam);
    }

    private void addNewTeam(Team team) {
        if (!ScoreBoardValidationService.isTeamAlreadyCreated(team.getName(), scoreboard)) {
            scoreboard.getTeams().add(team);
        }
    }
}
