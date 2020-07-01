package com.sdrc.fcmdemo.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sdrc.fcmdemo.model.Person;
import com.sdrc.fcmdemo.service.AndroidPushNotificationsService;
import com.sdrc.fcmdemo.service.FirebaseService;

@RestController
public class RestDemoController {

	@Autowired
	private FirebaseService firebaseService;
	
	@Autowired
	private AndroidPushNotificationsService androidPushNotificationsService;
	
	
	private final String TOPIC = "FCMDemo";

	
	@GetMapping("/getUserDetails")
	public Person getExample(@RequestHeader() String name) throws InterruptedException, ExecutionException {
		return firebaseService.getUserDetails(name);
	}
	
	@PostMapping("/createUser")
	public String postExample(@RequestBody Person person) throws InterruptedException, ExecutionException {
		return firebaseService.SaveUserDetails(person);
	}
	
	@PutMapping("/updateUser")
	public String putExample(@RequestBody Person person) throws InterruptedException, ExecutionException {
		return firebaseService.updateUserDetails(person);
	}
	
	@DeleteMapping("/deleteUser")
	public String deleteExample(@RequestHeader String name) throws InterruptedException, ExecutionException {
		return firebaseService.deleteUserDetails(name);
	}
	
	
	@PutMapping("/updateUserByName")
	public String putExample(@RequestBody String name) throws InterruptedException, ExecutionException {
		return firebaseService.updateUserDetailsByName(name);
	}
	

	@RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> send() throws JSONException {

		JSONObject body = new JSONObject();
		body.put("to", "/topics/" + TOPIC);
		body.put("priority", "high");

		JSONObject notification = new JSONObject();
		notification.put("title", "JSA Notification");
		notification.put("body", "Happy Message!");
		
		JSONObject data = new JSONObject();
		data.put("Key-1", "JSA Data 1");
		data.put("Key-2", "JSA Data 2");

		body.put("notification", notification);
		body.put("data", data);

		HttpEntity<String> request = new HttpEntity<>(body.toString());

		CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
		CompletableFuture.allOf(pushNotification).join();

		try {
			String firebaseResponse = pushNotification.get();
			
			return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
	}
	


	
}
