package com.sdrc.fcmdemo.service;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.sdrc.fcmdemo.model.Person;

@Service
public class FirebaseService {
public String SaveUserDetails(Person person) throws InterruptedException, ExecutionException {
     Firestore dbFireStore = FirestoreClient.getFirestore();
	ApiFuture<WriteResult>  collectionApiFutere = dbFireStore.collection("users").document(person.getName()).set(person);
	return collectionApiFutere.get().getUpdateTime().toString();
}

public Person getUserDetails(String name) throws InterruptedException, ExecutionException {
	Firestore dbFireStore = FirestoreClient.getFirestore();
	DocumentReference documentReference =dbFireStore.collection("users").document(name);
	ApiFuture<DocumentSnapshot> future = documentReference.get();
	DocumentSnapshot document = future.get();
	
	Person person =  null;
	if (document.exists()) {
		person = document.toObject(Person.class);
		return person;
	}else {
		return null;
	}
}

public String updateUserDetails(Person person) throws InterruptedException, ExecutionException {
	Firestore firestore = FirestoreClient.getFirestore();
	ApiFuture<WriteResult>  collectionApiFutere = firestore.collection("users").document(person.getName()).set(person);
	return collectionApiFutere.get().getUpdateTime().toString();
}

public String deleteUserDetails(String name) throws InterruptedException, ExecutionException {
	Firestore firestore = FirestoreClient.getFirestore();
	ApiFuture<WriteResult>  collectionApiFutere = firestore.collection("users").document(name).delete();
	return "Documentwith ID "+name+" has been deleted." ;
}

public String updateUserDetailsByName(String name) throws InterruptedException, ExecutionException {
	Firestore firestore = FirestoreClient.getFirestore();
	DocumentReference documentReference =firestore.collection("users").document(name);
	ApiFuture<DocumentSnapshot> future = documentReference.get();
	DocumentSnapshot document = future.get();
	
	Person person =  null;
	if (document.exists()) {
		person = document.toObject(Person.class);
	}
	ApiFuture<WriteResult>  collectionApiFutere = firestore.collection("users").document(person.getName()).set(person);
	return collectionApiFutere.get().getUpdateTime().toString();
}
}
