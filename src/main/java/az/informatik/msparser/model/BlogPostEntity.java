package az.informatik.msparser.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "blog_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5000, nullable = false)
    private String title;

    @Column(length = 5000, columnDefinition = "TEXT")
    private String content;

    @Column(length = 500)
    private String excerpt;

    @Column(length = 500)
    private String image;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "author_id")
    private Integer authorId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BlogImageEntity> images;
}
