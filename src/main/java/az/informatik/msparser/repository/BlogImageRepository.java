package az.informatik.msparser.repository;

import az.informatik.msparser.model.BlogImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogImageRepository extends JpaRepository<BlogImageEntity, String> {
}
