package model;

import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Match implements Comparable<Match> {

    private final Team homeTeam;
    private final Team awayTeam;
    private Date startTime;
    private Date endTime;
    private Score score;
    private Boolean inProgress;

    public Match(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.score = new Score(0, 0);
        this.inProgress = false;
    }

    @Override
    public String toString() {
        return homeTeam.getName() + " " + score.getHomeTeamScore() + " - " + awayTeam.getName() + " " + score.getAwayTeamScore();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return getHomeTeam().equals(match.getHomeTeam()) && getAwayTeam().equals(match.getAwayTeam());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHomeTeam(), getAwayTeam());
    }

    @Override
    public int compareTo(Match o) {
        var totalScore1 = this.score.getHomeTeamScore() + this.score.getAwayTeamScore();
        var totalScore2 = o.getScore().getHomeTeamScore() + o.getScore().getAwayTeamScore();
        if (totalScore1 < totalScore2) {
            return 1;
        } else if (totalScore1 > totalScore2) {
            return -1;
        } else {
            return this.startTime.compareTo(o.startTime) * -1;
        }
    }
}
