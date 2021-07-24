package com.cds.academy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	String _id;
	String username;
	String fullname;
	String password;
	String email;
	List<String> messagesId;
	String description;
	String avatar;
	HashSet<String> likes;
	HashSet<String> follows;

	public User() {
		this.messagesId = new ArrayList<String>();
		this.likes = new HashSet<String>();
	}

	public User(String username, String password, String email, String fullname, String avatar) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.messagesId = new ArrayList<String>();
		this.follows = new HashSet<String>();
		this.likes = new HashSet<String>();
		this.avatar = avatar;
		this.fullname = fullname;
	}

	public HashSet<String> getFollows() {
		return follows;
	}

	public void setFollows(HashSet<String> follows) {
		this.follows = follows;
	}

	public HashSet<String> getLikes() {
		return likes;
	}

	public void setLikes(HashSet<String> likes) {
		this.likes = likes;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getMessagesId() {
		return messagesId;
	}

	public void setMessagesId(List<String> messagesId) {
		this.messagesId = messagesId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
