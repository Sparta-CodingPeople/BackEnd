package com.server.delivery.model.review.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_review")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE p_review SET review_is_deleted = true WHERE review_id = ?") //is_deleted로 수정? 스네이크 케이스를 사용하기 때문
@SQLRestriction("review_is_deleted = false")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "review_id")
    private UUID reviewUuid;

    @Column(name = "review_content")
    private String content;

    @Column(name = "review_rating")
    private Double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "review")
    private List<ReviewImage> images = new ArrayList<>();

    @Builder.Default
    @Column(name = "review_is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_uuid")
    private Store store;

    @OneToOne
    @JoinColumn(name = "orders_uuid", unique = true)
    private Order order;

    // todo. 필요 시 추가
    //    @ManyToOne
    //    @JoinColumn(name = "menus_uuid")
    //    private Menu menu;

    public void updateReview(String content, Double rating) {
        this.content = content;
        this.rating = rating;
    }

    public void updateReviewImages(List<ReviewImage> images) {
        for (ReviewImage image : images) {
            image.changeReview(this);
        }
        this.images = images;
    }

    public void performSoftDelete() {
        this.isDeleted = Boolean.TRUE;
        this.softDelete();
    }
}
