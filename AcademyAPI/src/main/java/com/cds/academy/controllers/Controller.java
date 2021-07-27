package com.cds.academy.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cds.academy.model.ErrorResponse;
import com.cds.academy.model.Hastag;
import com.cds.academy.model.Message;
import com.cds.academy.model.User;
import com.cds.academy.repository.Hastags;
import com.cds.academy.repository.Messages;
import com.cds.academy.repository.Users;

@RefreshScope
@RestController
@RequestMapping("/api/v1/")
public class Controller {

	@Autowired
	Users userRepository;

	@Autowired
	Messages messagesRepository;

	@Autowired
	Hastags hastagsRepository;

	/**
	 * USERS
	 */
	@PostMapping("user/login")
	public ResponseEntity<?> log(@RequestBody User user) {
		String username = user.getUsername();
		String password = user.getPassword();
		User userDB = null;

		try {
			userDB = userRepository.findByUsername(username);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (userDB == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (!password.equals(userDB.getPassword())) {
			return new ResponseEntity<Object>("Wrong password.", HttpStatus.OK);
		}

		return new ResponseEntity<Object>(userDB, HttpStatus.OK);

	}

	@PostMapping("user/register")
	public ResponseEntity<?> register(@RequestBody User user) {

		User userDB = null;

		try {
			userDB = userRepository.findByUsername(user.getUsername());
		} catch (Exception e) {

		}

		if (userDB != null) {
			return new ResponseEntity<Object>("Username " + user.getUsername() + " is in use.", HttpStatus.BAD_REQUEST);
		}
		User u = new User(user.getUsername(), user.getPassword(), user.getEmail(), user.getFullname(),
				user.getAvatar());
		u = userRepository.save(u);
		return new ResponseEntity<Object>(u, HttpStatus.OK);

	}

	@GetMapping("user/{userid}")
	public ResponseEntity<?> getUser(@PathVariable String userid) {
		User u = userRepository.findByUsername(userid);
		u.setPassword("");
		return new ResponseEntity<Object>(u, HttpStatus.OK);
	}

	@PostMapping("user/{userid}")
	public ResponseEntity<?> saveUserInfo(@PathVariable String userid, @RequestBody User body) {

		User u = userRepository.findById(userid).get();

		if (body.getUsername() != null) {
			String newUserName = body.getUsername();
			if (!newUserName.equals(u.getUsername())) {
				User userAux = userRepository.findByUsername(newUserName);
				if (userAux == null) {
					u.setUsername(newUserName);
				}else {

					ErrorResponse error = new ErrorResponse(406, "Username in use.");
					return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_ACCEPTABLE);
				}


			}

		}

		if (body.getAvatar() != null) {
			u.setAvatar(body.getAvatar());
		}

		if (body.getFullname() != null) {
			u.setFullname(body.getFullname());
		}

		if (body.getDescription() != null) {
			u.setDescription(body.getDescription());
		}
		
		List<String> messages =  u.getMessagesId();
		for (String messageuuid : messages) {
			Message message = messagesRepository.findById(messageuuid).get();
			message.setUserFullname(u.getFullname());
			message.setUserUsername(u.getUsername());
			messagesRepository.save(message);
		}

		userRepository.save(u);
		return new ResponseEntity<Object>(u, HttpStatus.OK);
	}

	@GetMapping("user/getall")
	public ResponseEntity<?> getAllUsers() {
		List<User> ms = userRepository.findAll();
		return new ResponseEntity<Object>(ms, HttpStatus.OK);
	}

	@PostMapping("user/follow")
	public ResponseEntity<?> getAllUsers(@RequestParam("userid") String userid,
			@RequestParam("followid") String followid) {

		User u = userRepository.findById(userid).get();
		HashSet<String> follows = u.getFollows();
		follows.add(followid);

		userRepository.save(u);

		return new ResponseEntity<Object>(u, HttpStatus.OK);
	}

	@GetMapping("user/follows")
	public ResponseEntity<?> getFollowUsers(@RequestParam String uuid) {
		List<User> follows = new ArrayList<User>();
		User u = userRepository.findById(uuid).get();
		for (String followuuid : u.getFollows()) {
			follows.add(userRepository.findById(followuuid).get());
		}

		return new ResponseEntity<Object>(follows, HttpStatus.OK);
	}

	@GetMapping("user/getlikes")
	public ResponseEntity<?> getLikesUsers(@RequestParam String uuid) {
		List<Message> likes = new ArrayList<Message>();
		User u = userRepository.findById(uuid).get();
		for (String followuuid : u.getLikes()) {
			likes.add(messagesRepository.findById(followuuid).get());
		}

		return new ResponseEntity<Object>(likes, HttpStatus.OK);
	}

	/**
	 * MESSAGES
	 */

	@PostMapping("message/save")
	public ResponseEntity<?> saveMessage(@RequestBody Message message) {

		User u = userRepository.findByUsername(message.getUserUsername());
		try {
			message.setAvatar(u.getAvatar());
		} catch (Exception e) {
		}

		List<String> allMatches = new ArrayList<String>();

		Pattern regex = Pattern.compile(".*?\\s(#\\w+).*?");
		Matcher regexMatcher = regex.matcher(String.valueOf(message.getContent()));
		while (regexMatcher.find()) {
			allMatches.add(regexMatcher.group(1));
		}

		List<Hastag> hashtags = hastagsRepository.findAll();
		try {
			Hastag hastag = hashtags.get(0);
			HashMap<String, Integer> map = hastag.getList();

			for (String match : allMatches) {
				if (map.containsKey(match)) {
					map.replace(match, map.get(match) + 1);
				} else {
					map.put(match, 1);
				}
			}

			hastag.setList(map);
			hastagsRepository.save(hastag);
		} catch (Exception e) {
		
		}
		
		message.setLikesUserID(new HashSet<String>());
		message.setUserUsername(u.getUsername());
		message.setUserFullname(u.getFullname());
		message = messagesRepository.save(message);

		List<String> listMessages = u.getMessagesId();
		listMessages.add(message.get_id());
		u.setMessagesId(listMessages);
		userRepository.save(u);

		return new ResponseEntity<Object>(message, HttpStatus.OK);
	}

	@GetMapping("message/{messageid}")
	public ResponseEntity<?> getMessage(@RequestParam String messageid) {
		Message m = messagesRepository.findById(messageid).get();
		return new ResponseEntity<Object>(m, HttpStatus.OK);

	}

	@GetMapping("message/getall")
	public ResponseEntity<?> getAllMessages() {
		List<Message> ms = messagesRepository.findAll();
		return new ResponseEntity<Object>(ms, HttpStatus.OK);
	}

	/**
	 * LIKES
	 */

	@PostMapping("message/like/{messageid}")
	public ResponseEntity<?> messageLike(@PathVariable String messageid, @RequestParam String username) {

		User user = userRepository.findByUsername(username);
		Message m = messagesRepository.findById(messageid).get();

		HashSet<String> list = user.getLikes();
		boolean alreadyLike = list.contains(messageid);
		int code = 0;
		HashSet<String> map = m.getLikesUserID();

		if (!alreadyLike) {
			m.setLike(m.getLike() + 1);

			map.add(username);

			m.setLikesUserID(map);
			list.add(messageid);
			user.setLikes(list);
			code = 300;

		} else {
			m.setLike(m.getLike() - 1);
			list.remove(messageid);
			user.setLikes(list);
			map.remove(username);

			code = 301;
		}

		userRepository.save(user);
		messagesRepository.save(m);

		ErrorResponse errorResponse = new ErrorResponse(code, "You like/dislike this tweet");
		return new ResponseEntity<Object>(errorResponse, HttpStatus.OK);
	}

	/**
	 * HASTAG
	 */
	@GetMapping("hastag/getall")
	public ResponseEntity<?> getAllHastag() {
		Hastag ms = hastagsRepository.findAll().get(0);

		return new ResponseEntity<Object>(ms.getList(), HttpStatus.OK);
	}

}
