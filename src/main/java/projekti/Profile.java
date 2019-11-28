package projekti;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile extends AbstractPersistable<Long> {

    private String name;    // profile users real name
    private String alias;   // profile alias on the address field

    @OneToOne
    private Profile blockedByProfile;   // profile is blocked by this profile --> not visible both ways
    @OneToOne
    private Image profilePic;
    @OneToMany
    private List<Image> picGallery;
    @OneToMany
    private List<Profile> followingMe;  // following this profile
    @OneToMany
    private List<Profile> followingAt;  // profiles followed by this profile



}
