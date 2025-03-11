package service;

import exception.ErrorMessages;
import java.util.Arrays;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import model.Match;
import model.Scoreboard;
import model.Team;

@UtilityClass
public class ScoreBoardValidationService {

    void checkScoreUpdate(Match match, Integer homeTeamScore, Integer awayTeamScore) {
        if (areValuesNonNull(match, homeTeamScore, awayTeamScore)) {
            throw ErrorMessages.reportNullArgument();
        }
        if (areValuesPositive(homeTeamScore, awayTeamScore)) {
            throw ErrorMessages.reportNegativeArgument();
        }
        if (match.getEndTime() != null) {
            throw ErrorMessages.reportFinishedMatch();
        }
        if (Boolean.FALSE.equals(match.getInProgress())) {
            throw ErrorMessages.reportMatchNotInProgress();
        }
    }

    void checkMatchIsValid(Match match) {
        if (match == null) {
            throw ErrorMessages.reportNullArgument();
        }
        if (!areValuesNonNull(match.getHomeTeam().getName(), match.getAwayTeam().getName())) {
            throw ErrorMessages.reportNullArgument();
        }
        if (match.getEndTime() != null) {
            throw ErrorMessages.reportFinishedMatch();
        }
        if (areTeamsEqual(match)) {
            throw ErrorMessages.reportNonValidTeamName();
        }
    }

    void checkMatchIsInScoreboard(Match match, Scoreboard scoreboard) {
        if (isMatchNotInTheScoreboard(match, scoreboard)) {
            throw ErrorMessages.reportMatchNotInScoreboard();
        }
    }

    boolean isMatchNotInTheScoreboard(Match match, Scoreboard scoreboard) {
        return !scoreboard.getMatches().contains(match);
    }

    boolean isTeamAlreadyCreated(String teamName, Scoreboard scoreboard) {
        return scoreboard.getTeams().contains(new Team(teamName));
    }

    private boolean areValuesNonNull(Object... values) {
        return Arrays.stream(values).allMatch(Objects::nonNull);
    }

    private boolean areValuesPositive(Integer... values) {
        return Arrays.stream(values).allMatch(value -> value >= 0);
    }

    private boolean areTeamsEqual(Match match) {
        return match.getHomeTeam().equals(match.getAwayTeam());
    }
}
