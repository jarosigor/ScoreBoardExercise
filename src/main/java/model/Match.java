package model;

import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Match {

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
}
