package projekti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projekti.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {


}
