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


    public Account(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;

    }

    @OneToMany(mappedBy = "account")
    private List<Image> picGallery = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Message> messageList = new ArrayList<>();

    @ManyToMany(mappedBy = "likedAccounts")
    private List<Message> likedMessages = new ArrayList<>();


    // following and blocking

    @ManyToMany
    private List<Account> following = new ArrayList<>();    // all profiles this one is following

    @ManyToMany(mappedBy = "following")
    private List<Account> followedBy = new ArrayList<>();   // all profiles that are following this profile

    @ManyToOne
    private Account blockedThis;                            // profile that blocked this profile

    @OneToMany(mappedBy = "blockedThis")
    private List<Account> blockedFollowers = new ArrayList<>();     // list of all profiles this profile has blocked

}
