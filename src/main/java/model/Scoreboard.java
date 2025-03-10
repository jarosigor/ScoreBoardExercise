package model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Scoreboard {

    private List<Team> teams;
    private List<Match> matches;

    public Scoreboard() {
        this.teams = new ArrayList<>();
        this.matches = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        matches.forEach(match -> sb.append(match.toString()).append("\n"));
        return sb.toString();
    }
}
