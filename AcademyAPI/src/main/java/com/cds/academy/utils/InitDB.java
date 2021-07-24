package com.cds.academy.utils;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.cds.academy.model.Hastag;
import com.cds.academy.model.Message;
import com.cds.academy.repository.Hastags;
import com.cds.academy.repository.Messages;
import com.cds.academy.repository.Users;

@Configuration
public class InitDB {


	@Autowired
	Users userRepository;

	@Autowired
	Messages messagesRepository;
	@Autowired
	Hastags hastahsRepository;
	
	@PostConstruct
	public void pupulateData() {
		//messages();
		//hastag();
	}
//	
//	private void messages() {
//		for (int i = 0; i < 10; i++) {
//			Message m = new Message();
//			m.setUser_id("username"+i);
//			m.setContent("Hola soy un mensaje de test " + i);
//			m.setLike(i);
//			Date d = new Date();
//			m.setDate(d.toLocaleString());
//			messagesRepository.save(m);
//		}
//		
//	}


	private void hastag() {
		Hastag h = new Hastag();
		h.setList(new HashMap<String, Integer>());
		hastahsRepository.save(h);
		
	}
	
}
