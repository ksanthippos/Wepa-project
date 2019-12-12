package projekti.model;

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

    // comments
    @OneToMany(mappedBy = "account")
    private List<Comment> commentList = new ArrayList<>();


    // BLOCKING SAME STYLE AS FOLLOWING ??

}
