package firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;

public class FirebaseInit {
    public static Firestore init() throws Exception{
        FileInputStream fis = new FileInputStream("firebasekey.json");

        FirebaseOptions fbo = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(fis)).build();

        FirebaseApp.initializeApp(fbo);
        System.out.println("inicializado");
        return FirestoreClient.getFirestore();
    }
}
