package DL.Team.Page;

import DL.Team.Members.PageUser;
import DL.Team.Team;

import javax.persistence.*;

/**
 * Description: Defines a Page object - a personal page of coach/player, a fan can follow    X
 * ID:              X
 **/


@NamedQueries(value = {
        @NamedQuery(name = "UserPage", query = "SELECT up from UserPage up"),
        @NamedQuery(name = "UserPageSetContent", query = "UPDATE UserPage us SET us.content = :content WHERE us.pageUser = :pageUser "),

})

@Entity
public class UserPage extends Page {

    @Id
    @Column
    @OneToOne(cascade = CascadeType.MERGE)
    private PageUser pageUser;

    public UserPage(String content, PageUser pageUser) {

        if (pageUser == null || content == null) throw new IllegalArgumentException();

        this.pageUser = pageUser;
        super.content = content;
    }

    public UserPage() {

    }

}
