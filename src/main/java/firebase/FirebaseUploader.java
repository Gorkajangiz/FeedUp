package firebase;

import clases.News;
import com.google.cloud.firestore.Firestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUploader {
    Firestore db;

    public FirebaseUploader(Firestore db) {
        this.db = db;
    }

    public void upload (News n){
        try {
            Map<String, Object> news = new HashMap<>();
            news.put("title", n.getTitulo());
            news.put("description", n.getDescripcion());
            news.put("link", n.getLink());
            news.put("source", n.getSource());
            news.put("pubdate", n.getFecha());
            news.put("category", n.getCategoria());
            news.put("image", n.getImagen());
            news.put("author", n.getAutor());
            db.collection("news").add(news).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
