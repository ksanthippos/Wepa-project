package projekti;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
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



}
