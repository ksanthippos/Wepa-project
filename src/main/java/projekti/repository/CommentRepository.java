package projekti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projekti.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
