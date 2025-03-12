package model;

import lombok.AllArgsConstructor;
import lombok.Data;

/***
 * Model class for the Score entity
 */
@Data
@AllArgsConstructor
public class Score {

    private Integer homeTeamScore;
    private Integer awayTeamScore;
}
