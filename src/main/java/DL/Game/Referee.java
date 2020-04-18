package DL.Game;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Users.Fan;
import DL.Users.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:     Represents a referee user
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "AllReferees", query = "SELECT r From Referee r"),
        @NamedQuery(name = "UpdateRefereeLeagueSeasonList", query = "UPDATE Referee r SET r.leagueSeasons = :newLeagueSeasonList WHERE r.username = : username"),
        @NamedQuery(name = "RefereeeByFan", query = "SELECT r FROM Referee r WHERE fan = :fan"),
})
public class Referee {

    @Id
    @Column
    String name;

    @Id
    @Column
    Fan fan;

    @Column
    boolean active;

    @Column
    private String qualification;
    @OneToMany
    private List<Match> mainMatches;
    @ManyToMany
    private List<Match> linesManMatches;
    @ManyToMany
    private List<LeagueSeason> leagueSeasons;



    public Referee (String qualification, String name, Fan fan, boolean active)
    {
        this.qualification = qualification;
        this.name = name;
        this.fan = fan;
        this.active = active;

        this.mainMatches = new ArrayList<Match>();
        this.linesManMatches = new ArrayList<Match>();
        this.leagueSeasons = new ArrayList<LeagueSeason>();
    }

    public Referee ()
    {
        this("", "", null, true);
    }

    public void addLinesManMatch(Match match) {
        this.linesManMatches.add(match);
    }

    public void addMainMatch(Match match) {
        this.mainMatches.add(match);
    }

    public void addLeagueSeason(LeagueSeason leagueSeason) {
        if (leagueSeason != null)
            this.leagueSeasons.add(leagueSeason);
    }

    public boolean createMatchEvent() {
        return false;
    }

    public String getName()
    {
        return name;
    }

    public Fan getFan()
    {
        return fan;
    }
    public List<LeagueSeason> getLeagueSeasons() {
        return leagueSeasons;
    }

}

