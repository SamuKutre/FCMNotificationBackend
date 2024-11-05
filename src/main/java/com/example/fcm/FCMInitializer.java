//package com.example.fcm;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@Service
//public class FCMInitializer {
//
//    @PostConstruct
//    public void initialize() {
//        try {
//            FileInputStream serviceAccount =
//                    new FileInputStream("src/main/resources/firebase-adminsdk.json");
//
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//

package com.example.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FCMInitializer {

    @PostConstruct
    public void initialize() {
        try {
            // Check if the environment variable for JSON content is set
            String firebaseJson = System.getenv("FIREBASE_JSON");
            if (firebaseJson != null) {
                // Load credentials from the JSON string
                GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(firebaseJson.getBytes()));
                initializeFirebaseApp(credentials);
            } else {
                // Fallback to loading from a file path
                String path = System.getenv("FIREBASE_CONFIG_PATH");
                if (path == null) {
                    throw new IllegalStateException("Environment variable FIREBASE_CONFIG_PATH is not set. Please set it to the path of your Firebase JSON credentials file.");
                }

                // Load credentials from the specified path
                FileInputStream serviceAccount = new FileInputStream(path);
                GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
                initializeFirebaseApp(credentials);
            }
        } catch (IOException e) {
            System.out.println("Error initializing Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeFirebaseApp(GoogleCredentials credentials) {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        // Initialize FirebaseApp if not already initialized
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}


