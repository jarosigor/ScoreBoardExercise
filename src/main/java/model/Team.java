package model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Team {

    private final UUID uuid;
    private final String name;
    private Integer wins;
    private Integer draws;
    private Integer losses;

    public Team(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
    }
}
