package com.server.delivery.model.review.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_review_image")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE p_review_image SET review_image_is_Deleted = true WHERE review_image_id = ?")
@SQLRestriction("review_image_is_Deleted = false")
public class ReviewImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "review_image_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    private String imageUrl;

    private String imageName;

    private Long imageSize;

    @Builder.Default
    @Column(name = "review_image_is_Deleted")
    private Boolean isDeleted = Boolean.FALSE;

    public void changeReview(Review review) {
        this.review = review;
    }

    public void changeDeleteStatus(User user) {
        this.isDeleted = Boolean.TRUE;
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(user.getUsername());
    }

    public void performSoftDelete() {
        this.isDeleted = Boolean.TRUE;
        this.softDelete();
    }
}
