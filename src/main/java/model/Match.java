package model;

import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Match {

    private final UUID uuid;
    private final Team homeTeam;
    private final Team awayTeam;
    private Date startTime;
    private Date endTime;
    private Score score;
    private Boolean inProgress;

    public Match(Team homeTeam, Team awayTeam) {
        this.uuid = UUID.randomUUID();
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    @Override
    public String toString() {
        return homeTeam.getName() + " " + score.getHomeTeamScore() + " - " + awayTeam.getName() + " " + score.getAwayTeamScore();
    }
}
