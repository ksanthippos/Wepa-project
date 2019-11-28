package projekti;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Lob;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image extends AbstractPersistable<Long> {

    private String description;

   /* @Lob
    private byte[] content;*/

}
