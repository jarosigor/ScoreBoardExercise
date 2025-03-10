package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Score {

    private Integer homeTeamScore;
    private Integer awayTeamScore;
}
