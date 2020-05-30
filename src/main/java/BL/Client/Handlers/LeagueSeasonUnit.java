package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Team;
import DL.Users.Fan;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description: This class represents the leagueSeasonUnit.
 * it gets the information from the ClientSystem (from the user) and performs tasks as required.
 * responsible of U.C 9.1-9.6 - all the use cases that related to League, Season, and Policies.
 **/
public class LeagueSeasonUnit {

    private ClientServerCommunication clientServerCommunication;
    public static void main(String[] args) throws Exception {
        try {
            ClientServerCommunication c = new ClientServerCommunication();
            LeagueSeasonUnit l = new LeagueSeasonUnit(c);
            AssociationManagementUnit am = new AssociationManagementUnit(c);
            PoliciesUnit p = new PoliciesUnit(c);
            Fan f1 = new Fan("Assaf", "test@mail.com", DigestUtils.sha1Hex("abcd"));
            Fan f2 = new Fan("Amit", "test1@mail.com", DigestUtils.sha1Hex("abcd"));
            am.addTeam("h", "aa", f1);
            am.addTeam("d", "a", f2);
            l.addNewLeague("a");
            l.addNewSeason(3028);
            p.addNewGamePolicy(7, 3);
            p.addNewScorePolicy(7, 3, 5);
            League league = l.getLeagues().get(0);
            Season season = l.getSeasons().get(0);
            GamePolicy gp = p.getGamePolicies().get(0);
            ScorePolicy sp = p.getScorePolicies().get(0);
            Date d = new Date();
            l.addLeagueSeason(league, season, gp, sp, d);
            Team t = am.loadTeam("h");
            Team t1 = am.loadTeam("d");
            List<LeagueSeason> ls = l.getLeagueSeasons(new Season(3028));
            LeagueSeason ld = ls.get(0);
//            t.setLeagueSeasons();
//            t1.setLeagueSeasons();
            l.addTeamToLeagueSeason(ld, t);
            //l.addTeamToLeagueSeason(ld, t1);
            p.scheduleMatches(ld);

            //System.out.println(ld.getMatches());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Ctor with parameters
     *
     * @param clientServerCommunication
     */
    public LeagueSeasonUnit(ClientServerCommunication clientServerCommunication) {
        this.clientServerCommunication = clientServerCommunication;
    }

    /**
     * creates new league and adds it into the database. checks if the name is valid - nut null and null empty string.
     *
     * @param name of the league
     * @return true if the league created
     */
    public boolean addNewLeague(String name) throws Exception {
        if (name != null && !name.equals("")) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("name", name);
            List league = clientServerCommunication.query("GetLeague", parameters);

            //connection problem
            if (league == null)
                throw new Exception("There was a problem with the connection to the server. Please try again later");

                //league name already exists
            else if (league.size() > 0)
                throw new Exception("League with that name already exists. Please insert different name.");

            else {
                League newLeague = new League(name);
                return clientServerCommunication.insert(newLeague);
            }

        } else
            throw new Exception("League name can not be empty. Please try again.");
    }

    /**
     * creates new season and adds it into the database. the year should be greater than 1950.
     *
     * @param year of the season
     * @return true if the season created
     */
    public boolean addNewSeason(int year) throws Exception {

        if (year >= 1950) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("year", year);
            List season = clientServerCommunication.query("GetSeason", parameters);

            //connection problem
            if (season == null)
                throw new Exception("There was a problem with the connection to the server. Please try again later");

                //league name already exists
            else if (season.size() > 0)
                return true;

            else {
                Season newSeason = new Season(year);
                return clientServerCommunication.insert(newSeason);
            }

        } else
            throw new Exception("Year must be greater than 1950. Please try again.");
    }

    /**
     * Creates new leagueSeason according to the league and season parameters, and adds it into the database.
     * checks that the parameters aren't null.
     *
     * @param league          league of the leagueSeason
     * @param season          season of the league season
     * @param gamePolicy      the game policy according to it the leagueSeason schedule its matches
     * @param scorePolicy     the score policy according to it the leagueSeason calculates its table.
     * @param startLeagueDate the date of the first match.
     * @return true if the league season created
     */
    public boolean addLeagueSeason(League league, Season season, GamePolicy gamePolicy, ScorePolicy scorePolicy, Date startLeagueDate) throws Exception {
        if (league != null && season != null && scorePolicy != null && gamePolicy != null) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("league", league);
            parameters.put("season", season);
            List leagueSeason = clientServerCommunication.query("GetLeagueSeason", parameters);

            //connection problem
            if (leagueSeason == null)
                throw new Exception("There was a problem with the connection to the server. Please try again later");

                //league name already exists
            else if (leagueSeason.size() > 0)
                throw new Exception("LeagueSeason already exists. Please try with different year or name.");

            else {
                LeagueSeason newLeagueSeason = new LeagueSeason(league, season, gamePolicy, scorePolicy, startLeagueDate);
                return clientServerCommunication.insert(newLeagueSeason);
            }

        } else
            throw new Exception("Parameters should not be null. Please try again");
    }

    /**
     * Changes the score policy of a given league season.
     * checks if the parameters aren't null and that the leagueSeason didn't start yet.
     *
     * @param leagueSeason
     * @param scorePolicy
     * @return true if the scorePolicy changed.
     */
    public boolean changeScorePolicy(LeagueSeason leagueSeason, ScorePolicy scorePolicy) throws Exception {
        if (leagueSeason != null && scorePolicy != null) {

            if (leagueSeason.setScorePolicy(scorePolicy)) {
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("newScorePolicy", scorePolicy);
                parameters.put("league", leagueSeason.getLeague());
                parameters.put("season", leagueSeason.getSeason());
                return clientServerCommunication.update("UpdateScorePolicy", parameters);

            } else {
                throw new Exception("Sorry, you can not change the score policy because the league is already running.");
            }

        } else
            throw new Exception("Parameters should not be null. Please try again");
    }

    /**
     * Sets referee In leagueSeason. checks that the referee and leagueSeason aren't null.
     *
     * @param leagueSeason
     * @param referee
     * @return true if the referee added.
     */
    public boolean setRefereeInLeagueSeason(LeagueSeason leagueSeason, Referee referee) throws Exception {
        if (leagueSeason != null && referee != null) {

            if (leagueSeason.addReferee(referee)) {
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("newReferees", leagueSeason.getReferees());
                parameters.put("league", leagueSeason.getLeague());
                parameters.put("season", leagueSeason.getSeason());
                return clientServerCommunication.update("UpdateLeagueSeasonRefereeList", parameters);

                //referee already exists
            } else {
                return true;
            }
        }
        throw new Exception("Parameters should not be null. Please try again");
    }

    /**
     * Adds Team to leagueSeason. checks that the Team and leagueSeason aren't null,
     * and that the team isn't already exists.
     *
     * @param leagueSeason
     * @param team
     * @return
     */
    public boolean addTeamToLeagueSeason(LeagueSeason leagueSeason, Team team) throws Exception {
        if (leagueSeason != null && team != null) {

            if (leagueSeason.addTeam(team)) {
                HashMap<String, Object> parameters = new HashMap<>();
//                parameters.put("newTeamList", leagueSeason.getTeamsParticipate());
//                parameters.put("league", leagueSeason.getLeague());
//                parameters.put("season", leagueSeason.getSeason());
                return clientServerCommunication.insert(leagueSeason);

            } else {
                throw new Exception("Sorry, the team is not active.");
            }

        } else
            throw new Exception("Parameters should not be null. Please try again");
    }

    /***************Getters*************/

    /**
     * @return the leagues in the system.
     */
    public List<League> getLeagues() {
        List<League> leagues = clientServerCommunication.query("GetAllLeagues", null);
        return leagues;
    }

    /**
     * @return the Seasons in the system.
     */
    public List<Season> getSeasons() {
        List<Season> seasons = clientServerCommunication.query("GetAllSeasons", null);
        return seasons;
    }

    /**
     * @return the LeagueSeasons in the system.
     */
    public List<LeagueSeason> getLeagueSeasons(Season season) throws Exception {
        if (season != null) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("season", season);
            List<LeagueSeason> leagueSeasons = clientServerCommunication.query("GetAllLeagueSeasons", parameters);
            return leagueSeasons;
        }
        throw new Exception("Parameters should not be null. Please try again");
    }


    /**
     * TODO: ADD TESTS FOR THOSE FUNCTION
     * /**
     *
     * @return the specific season
     */
    public Season getSeason(int year) throws Exception {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("year", year);
        List<Season> requiredSeason = clientServerCommunication.query("GetSeason", parameters);

        if (requiredSeason == null) {
            throw new Exception("There was a problem with the connection to the server. Please try again later");

        } else if (requiredSeason.size() <= 0) {
            throw new Exception("The season does not exist.");

        } else {
            return requiredSeason.get(0);
        }
    }

    /**
     * @return the specific LeagueSeason
     */
    public LeagueSeason getLeagueSeason(Season season, League league) throws Exception {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("season", season);
        parameters.put("league", league);
        List<LeagueSeason> requiredLeagueSeason = clientServerCommunication.query("GetLeagueSeason", parameters);
        if (requiredLeagueSeason.size() > 0) {
            return requiredLeagueSeason.get(0);
        } else {
            throw new Exception("The required League Season does not exist.");
        }
    }

    /**
     * @return the teams in the system.
     */
    public List<Team> getTeams() {
        List<Team> teams = clientServerCommunication.query("Team", null);
        return teams;
    }

    /**
     * @return the Referees in the system.
     */
    public List<Referee> getReferees() {
        List<Referee> referees = clientServerCommunication.query("AllReferees", null);
        return referees;
    }

}
