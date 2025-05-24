package az.informatik.msparser.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "blog_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogImageEntity {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private BlogPostEntity post;

    @Column(length = 500)
    private String url;
    @Column(length = 500)
    private String title;
    @Column(length = 500)
    private String description;

    @Column(name = "\"order\"")
    private Integer order;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
