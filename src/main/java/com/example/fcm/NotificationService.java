package com.example.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification(NotificationRequest notificationRequest) {
        // Create the Notification object using the builder pattern
        Notification notification = Notification.builder()
                .setTitle(notificationRequest.getTitle())  // Set the title
                .setBody(notificationRequest.getBody())    // Set the body
                .build();

        // Create the Message object with the token and notification
        Message message = Message.builder()
                .setToken(notificationRequest.getToken())  // Set the token
                .setNotification(notification)             // Attach the notification
                .build();

        try {
            // Send the message to the device corresponding to the provided registration token
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (Exception e) {
            System.out.println("Error sending FCM message: " + e.getMessage());
        }
    }
}