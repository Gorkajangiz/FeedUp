package main;

import clases.News;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import firebase.FirebaseInit;
import firebase.FirebaseUploader;
import ia.GroqClient;
import rssreader.CompHoyRSSReader;
import rssreader.vidExtRSSReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class main {
    public static void main(String[] args) {
        try {
            Firestore db = FirebaseInit.init();
            GroqClient classifier = new GroqClient();
            FirebaseUploader fu = new FirebaseUploader(db);
            CompHoyRSSReader readerCHRR = new CompHoyRSSReader();
            vidExtRSSReader readerVERR = new vidExtRSSReader();

            QuerySnapshot snapshot = db.collection("news").get().get();
            Set<String> titulosExistentes = new HashSet<String>();
            for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {
                String title = doc.getString("title");
                if (title != null) {
                    titulosExistentes.add(title);
                }
            }

            List<News> noticias = new ArrayList<>();
            noticias.addAll(readerVERR.getDatos());
            noticias.addAll(readerCHRR.getDatos());

            List<News> subida = new ArrayList<>();
            for (News news : noticias) {
                if(!titulosExistentes.contains(news.getTitulo())){
                    if(classifier.classify(news).equalsIgnoreCase("true")){
                        subida.add(news);
                    }
                    Thread.sleep(5000);
                }
            }

            for (News news : subida) {
                fu.upload(news);
            }

            System.out.println("Nuevas noticias subidas: " + subida.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
