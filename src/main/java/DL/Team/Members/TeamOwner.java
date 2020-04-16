package DL.Team.Members;
import DL.Administration.Financial.FinancialEntry;
import DL.Administration.Financial.FinancialUser;
import DL.Team.Team;
import DL.Users.Fan;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:  Defines a TeamOwner object - Owner of a specific team  X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "TeamOwner", query = "SELECT to FROM TeamOwner to"),
        @NamedQuery(name = "TeamOwnerByTeam", query = "SELECT to FROM TeamOwner to WHERE to.team = :team"),
        @NamedQuery(name = "TeamOwnerByTeamUser", query = "SELECT to FROM TeamOwner to WHERE to.teamUser = :teamUser"),
        @NamedQuery(name = "TeamOwnerByUser", query = "SELECT to FROM TeamOwner to WHERE to.active = true and to.teamUser.myUser = :user"),
        @NamedQuery(name = "TeamOwnerByNominee", query = "SELECT to FROM TeamOwner to WHERE to.nominees = :nominee"),
        @NamedQuery(name = "TeamOwnerAddOwnerNominee", query = "UPDATE TeamOwner to SET to.ownerNominees = :newNomineesList WHERE to =:teamOwner and to.active = true"),
        @NamedQuery(name = "TeamOwnerAddManageNominee", query = "UPDATE TeamOwner to SET to.manageNominees = :newNomineesList WHERE to =:teamOwner and to.active = true"),
        @NamedQuery(name = "setActiveTeamOwner", query = "UPDATE TeamOwner to SET to.active = : active where to =: teamOwner")
})
public class TeamOwner implements FinancialUser
{
    @Id
    @Column
    @OneToOne (cascade = CascadeType.MERGE)
    private TeamUser teamUser;

    @Column
    @OneToOne(cascade = CascadeType.MERGE)
    private Team team;

    @Column
    @OneToMany (cascade = CascadeType.MERGE)
    private List<TeamOwner> ownerNominees;

    @Column
    @OneToMany (cascade = CascadeType.MERGE)
    private List<TeamManager> manageNominees;

    @Column
    boolean active;

    public TeamOwner(Team team, TeamUser user, boolean active) {
        this.teamUser = user;
        this.team = team;
        this.active = active;
        this.ownerNominees = new ArrayList<>();
        this.manageNominees = new ArrayList<>();
    }

    public TeamOwner() {}

    @Override
    public List<FinancialEntry> getFinancialEntries() {
        return null;
    }

    public List<TeamOwner> getOwnerNominees() {
        return ownerNominees;
    }

    public List<TeamManager> getManageNominees() {
        return manageNominees;
    }

    public Team getTeam() {
        return team;
    }

    public TeamUser getTeamUser() {
        return teamUser;
    }

    public boolean isActive() {
        return active;
    }

    public TeamOwner addTeamOwnerNominee(TeamUser nominee)
    {
        if(nominee == null) return null;

        TeamOwner owner = new TeamOwner(team,nominee,true);
        ownerNominees.add(owner);
        team.teamOwners.add(owner);
        return owner;
    }

    public TeamManager addTeamManagerNominee(Fan nominee, String name)
    {
        if(nominee == null || name == null || name.isEmpty()) return null;

        TeamManager teamManager = new TeamManager(name,true,nominee,team,this);
        manageNominees.add(teamManager);
        team.teamManagers.add(teamManager);
        return teamManager;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}