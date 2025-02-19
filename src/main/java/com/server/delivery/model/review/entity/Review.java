package com.server.delivery.model.review.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.menu.entity.Menu;
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

@Entity
@Table(name = "p_review")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE p_review SET review_is_Deleted = true WHERE review_id = ?")
@SQLRestriction("review_is_Deleted = false")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "review_id")
    private String id;

    private String content;

    private Double rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> images = new ArrayList<>();

    @Builder.Default
    @Column(name = "review_is_Deleted")
    private Boolean isDeleted = Boolean.FALSE;

    // todo. 가게 id, 주문 id FK <- 주문과 리뷰는 다대다?
    @ManyToOne
    @JoinColumn(name = "store_uuid")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "menu_uuid")
    private Menu menu;

}
