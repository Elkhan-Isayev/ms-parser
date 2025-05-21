package az.informatik.msparser.repository;

import az.informatik.msparser.model.BlogPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPostEntity, Long> {
}
