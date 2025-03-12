package exception;

import lombok.experimental.UtilityClass;

/***
 * Utility class for error messages
 */
@UtilityClass
public class ErrorMessages {

    public static IllegalArgumentException reportNullArgument() {
        return new IllegalArgumentException("Arguments cannot be null");
    }

    public static IllegalArgumentException reportNegativeArgument() {
        return new IllegalArgumentException("Arguments must be positive");
    }

    public static IllegalStateException reportFinishedMatch() {
        return new IllegalStateException("Match is already finished");
    }

    public static IllegalArgumentException reportNonValidTeamName() {
        return new IllegalArgumentException("Team name cannot be empty or equal to each other");
    }

    public static IllegalStateException reportMatchNotInProgress() {
        return new IllegalStateException("Match is not in progress");
    }

    public static IllegalStateException reportMatchNotInScoreboard() {
        return new IllegalStateException("Match is not in scoreboard");
    }
}
