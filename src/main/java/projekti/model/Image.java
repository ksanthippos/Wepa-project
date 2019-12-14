package projekti.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.model.Account;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image extends AbstractPersistable<Long> {

    private String description;
    private boolean isProfilePic;

    @ManyToOne
    private Account account;    // images are linked to a certain account

    @ManyToMany(mappedBy = "likedImages")
    private List<Account> likedAccounts = new ArrayList<>();    // image can have multiple likes

    @OneToMany(mappedBy = "image")
    private List<Comment> commentList = new ArrayList<>();  // image can have multiple comments


/*    @Lob*/
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] content;     // image content (data) is converted to array form




}
