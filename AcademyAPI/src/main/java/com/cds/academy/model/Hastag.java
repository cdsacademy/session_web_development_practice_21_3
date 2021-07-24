package com.cds.academy.model;

import java.util.HashMap;

import org.springframework.data.annotation.Id;

public class Hastag {

	public Hastag() {

	}

	@Id
	String _id;
	HashMap<String, Integer> list;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public HashMap<String, Integer> getList() {
		if(list.isEmpty()) {
			return new HashMap<String, Integer>();
		}
		return list;
	}

	public void setList(HashMap<String, Integer> list) {
		this.list = list;
	}

}
