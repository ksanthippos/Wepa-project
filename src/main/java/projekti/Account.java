package projekti;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {

    private String username;
    private String password;
    private String nickname;
/*    private List<Account> followedAccounts;
    private List<Account> myFollowers;*/


    public Account(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;


    }

    // OK

    @OneToMany(mappedBy = "account")
    private List<Image> picGallery = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Message> messageList = new ArrayList<>();

    @ManyToOne
    private Message likedMessage;   // user can like different messages but each only once

/*
    @ManyToMany(mappedBy = "likedAccounts")
    private List<Message> likedMessages = new ArrayList<>();
*/


    // following and blocking
    @ManyToMany
    private List<Account> followingAt = new ArrayList<>();    // all profiles this one is following

    @ManyToMany(mappedBy = "followingAt")
    private List<Account> followingMe = new ArrayList<>();   // all profiles that are following this profile


    // BLOCKING SAME STYLE AS FOLLOWING OR THIS?

/*    @OneToMany(mappedBy = "blockedThis")
    private List<Account> blockedFollowers = new ArrayList<>();     // list of all profiles this profile has blocked

    @ManyToOne
    private Account blockedThis;                            // profile that blocked this profile*/
}
