package service;

import exception.ErrorMessages;
import java.util.Arrays;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import model.Match;

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
    }

    void checkMatchIsValid(Match match) {
        if (match == null) {
            throw ErrorMessages.reportNullArgument();
        }
        if (match.getEndTime() != null) {
            throw new IllegalStateException("Match is already finished");
        }
        if (areTeamsEqual(match)) {
            throw ErrorMessages.reportNonValidTeamName();
        }
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
