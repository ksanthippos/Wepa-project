package projekti.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @OneToMany(mappedBy = "image")
    private List<Comment> commentList = new ArrayList<>();  // image can have multiple comments

    @Lob
    private byte[] content;     // image content (data) is converted to array form




}
