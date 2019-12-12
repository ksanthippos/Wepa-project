package projekti.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {

    private String name;
    private String username;
    private String password;
    private String nickname;

    private Long profilePicId;

    // ctor for creating a new account
    public Account(String name, String username, String password, String nickname) {

        this.name = name;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    // THESE OK
    // ****************

    // images
    @OneToMany(mappedBy = "account")
    private List<Image> picGallery = new ArrayList<>();

    @ManyToMany
    private List<Image> likedImages = new ArrayList<>();    // user can like different images but each only once

    // messages
    @OneToMany(mappedBy = "account")
    private List<Message> messageList = new ArrayList<>();

    @ManyToMany
    private List<Message> likedMessages = new ArrayList<>();   // user can like different messages but each only once

    // following
    @ManyToMany
    private List<Account> followingAt = new ArrayList<>();    // all profiles this one is following

    @ManyToMany(mappedBy = "followingAt")
    private List<Account> followingMe = new ArrayList<>();   // all profiles that are following this profile

    // blocking
    @ManyToMany
    private List<Account> blockedAt = new ArrayList<>();

    @ManyToMany(mappedBy = "blockedAt")
    private List<Account> blockedMe = new ArrayList<>();

    // comments
    @OneToMany(mappedBy = "account")
    private List<Comment> commentList = new ArrayList<>();


    // NOT SURE YET...
    @ManyToMany
    private Map<String, Account> followingAtDate = new HashMap<>();

    @ManyToMany
    private Map<String, Account> followingMeDate = new HashMap<>();







}
