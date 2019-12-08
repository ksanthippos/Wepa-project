package projekti;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message extends AbstractPersistable<Long> {

    private String content;
    private LocalDateTime dateTime;


    @ManyToOne
    private Account account;    // every message comes from a certain account

    @ManyToMany(mappedBy = "likedMessages")
    private List<Account> likedAccounts = new ArrayList<>();    // a message can have multiple likes (= number of accounts)


    // COMMENTS ALSO !!!


}
