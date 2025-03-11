package model;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Team {

    private final String name;
    private Integer wins;
    private Integer draws;
    private Integer losses;

    public Team(String name) {
        this.name = name;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return getName().equalsIgnoreCase(team.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
