package projekti.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;


// Comment and Message classes are very similar

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends AbstractPersistable<Long> {

    private String content;
    private LocalDateTime dateTime;

    // comments are made by (users) account, comments can be made to messages and images
    @ManyToOne
    private Account account;

    @ManyToOne
    private Message message;

    @ManyToOne
    private Image image;

}
