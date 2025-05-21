package az.informatik.msparser.repository;

import az.informatik.msparser.model.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

}
