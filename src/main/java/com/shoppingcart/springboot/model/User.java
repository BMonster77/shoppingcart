package com.shoppingcart.springboot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(unique = true)
	private String email;

	private String password;
	private String userName;
	private String firstName;
	private String lastName;
	private boolean isStaff;
	private boolean isActive;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	@OneToOne(mappedBy = "user")
	private Customer customer;

	public User() {
	}


}
