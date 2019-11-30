package projekti;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image extends AbstractPersistable<Long> {

    private String description;

    @ManyToOne
    private Profile profile;

    @Lob
    private byte[] content;

}
