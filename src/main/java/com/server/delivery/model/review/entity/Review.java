package com.server.delivery.model.review.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	private UUID id;

	private String content;

	private Double rating;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Builder.Default
	@OneToMany(mappedBy = "review")
	private List<ReviewImage> images = new ArrayList<>();

	@Builder.Default
	@Column(name = "review_is_Deleted")
	private Boolean isDeleted = Boolean.FALSE;

	@ManyToOne
	@JoinColumn(name = "store_uuid")
	private Store store;

	@OneToOne
	@JoinColumn(name = "order_uuid", unique = true)
	private Order order;

	// todo. 필요 시 추가
	//    @ManyToOne
	//    @JoinColumn(name = "menu_uuid")
	//    private Menu menu;

	public void updateReview(String content, Double rating) {
		this.content = content;
		this.rating = rating;
	}

	public void updateReviewImages(List<ReviewImage> images) {
		this.images.clear();
		this.images.addAll(images);
	}
}
