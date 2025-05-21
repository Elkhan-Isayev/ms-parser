package az.informatik.msparser.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files", schema = "msparser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName; // stored as UUID.extension

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private NewsEntity news;
}
