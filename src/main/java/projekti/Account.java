package projekti;

import ignored.Image;
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

    //private String name;    // users real name for the account ("John Doe")
    private String username;   // username for login ("johndoe")
    private String password;

    // constructor for creating account
/*    public Account(String username, String password) {
        //this.name = name;
        this.username = username;
        this.password = password;
    }*/

/*    @OneToMany(mappedBy = "account")
    private List<Image> picGallery = new ArrayList<>();*/




}
