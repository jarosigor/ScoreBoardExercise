package service;

import model.Match;
import model.Scoreboard;

/***
 * Service to manage the scoreboard
 */
public interface ScoreBoardService {
    /***
     * Start a new match between two teams
     * @param homeTeamName
     * @param awayTeamName
     * @return recently created and inProgress match object
     * @throws IllegalArgumentException when passed arguments are invalid (null / empty / the same)
     */
    Match startNewMatch(String homeTeamName, String awayTeamName);

    /***
     * Update the score of the match
     * @param match to update
     * @param homeTeamScore home team score represented as a positive integer
     * @param awayTeamScore away team score represented as a positive integer
     * @throws IllegalArgumentException when passed arguments are invalid (null / negative,  team names are null / the same / empty)
     * @throws IllegalStateException when match is finished / not in progress / not in the scoreboard
     */
    void updateScore(Match match, Integer homeTeamScore, Integer awayTeamScore);

    /***
     * Finish the match
     * @param match to finish
     * @throws IllegalArgumentException when passed arguments are invalid (null, team names are null / the same / empty)
     * @throws IllegalStateException when match is finished / not in progress / not in the scoreboard
     */
    void finishMatch(Match match);

    /***
     * Get the summary of the matches
     * @return summary of the matches as a String
     */
    String getSummary();

    /***
     * Get the scoreboard
     * @return the scoreboard object with all the matches and teams in it
     */
    Scoreboard getScoreboard();

    /***
     * Get the match object by the team names
     * @param homeTeamName
     * @param awayTeamName
     * @return the match object when the match is found in the scoreboard, otherwise null
     */
    Match getMatch(String homeTeamName, String awayTeamName);
}
