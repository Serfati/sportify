package DL.Game.MatchEvents;


import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Description:     this class represents a penaltyKick event
 **/
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "AllPenaltyKickEvents", query = "Select e From PenaltyKick e")
})
public class PenaltyKick extends Event
{
    public PenaltyKick(EventUser createdByUser, EventLog eventLog, int gameTime) {
        super(createdByUser, eventLog, gameTime);
    }

    public PenaltyKick() {
        super(null,null,0);
    }
}
