package com.poc.firebasepoc.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class FireBaseInit {

	@PostConstruct
	private void initFireBaseConfig() throws IOException{
		//Service account details
		FileInputStream serviceAccount =
				new FileInputStream("Path to FireBase Service account");
		
		//Firebase Project location
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("Database Url. Look in Project Settings")
				.build();
		
		//Just create one instance
		if(FirebaseApp.getApps().isEmpty()) {
			FirebaseApp.initializeApp(options);
		}

	}
	
	public Firestore getFireBase() {
		return FirestoreClient.getFirestore();
	}
}
