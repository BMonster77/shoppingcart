package com.shoppingcart.springboot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

  @NotNull(message = "Product name is required.")
  private String name;

  @NotNull(message = "Category is required.")
  private String category;

	private String description;

  @NotNull(message = "Price is required.")
  @PositiveOrZero(message = "Price cannot be negative.")
  private double price;

  @PositiveOrZero(message = "Discount cannot be negative.")
  private double discount = 0.0;

  @PositiveOrZero(message = "Quantity cannot be negative.")
	private int storeQuantity;

	private String imageUrl;
	private String tags;
	private int viewCount;

  @NotNull(message = "Visibility status is required.")
	private boolean isVisible;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderDetail> orderDetails;

	@OneToMany(mappedBy = "product")
	private List<ShoppingCartProduct> shoppingCartProduct;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews;

	public Product() {};

	public Product(Long productId, String name, String category, String description, double price, double discount,
				   String imageUrl, String tags, int storeQuantity, int viewCount, boolean isVisible,
				   List<OrderDetail> orderDetails, List<ShoppingCartProduct> shoppingCartProduct, List<Review> reviews) {
		this.productId = productId;
		this.name = name;
		this.category = category;
		this.description = description;
		this.price = price;
		this.discount = discount;
		this.imageUrl = imageUrl;
		this.tags = tags;
		this.storeQuantity = storeQuantity;
		this.viewCount = viewCount;
		this.isVisible = isVisible;
		this.orderDetails = orderDetails;
		this.shoppingCartProduct = shoppingCartProduct;
		this.reviews = reviews;
	}

	// Getters and Setters

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getStoreQuantity() {
		return storeQuantity;
	}

	public void setStoreQuantity(int storeQuantity) {
		this.storeQuantity = storeQuantity;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean visible) {
		isVisible = visible;
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public List<ShoppingCartProduct> getShoppingCartProduct() {
		return shoppingCartProduct;
	}

	public void setShoppingCartProduct(List<ShoppingCartProduct> shoppingCartProduct) {
		this.shoppingCartProduct = shoppingCartProduct;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
}
