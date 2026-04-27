package rssreader;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.Jsoup;

import java.net.URL;

public class GameReactorRSSReader {
    public void getDatos(String categoria) {
        try {
            String feedurl = "https://www.gamereactor.es/rss/rss.php?texttype="; //GAMEREACTOR
            URL url;
            feedurl += categoria;
            url = new URL(feedurl);
            SyndFeedInput sfi = new SyndFeedInput();
            SyndFeed feed;
            feed = sfi.build(new XmlReader(url));
            String[] lista = new String[]{"PS5", "Nintendo", "Play Station", "Xbox", "PC"};
            String categorias;
            for (SyndEntry entry : feed.getEntries()) {
                if (categoria.equals("4")) { //Noticias mixtas
                    categorias = entry.getCategories().toString();
                    for (String a : lista) {
                        if (categorias.contains(a)) {
                            System.out.println("Título: " + Jsoup.parse(entry.getTitle()).text());
                            System.out.println("Link: " + entry.getLink());
                            System.out.println("Descripción: " + Jsoup.parse(entry.getDescription().getValue()).text());
                            System.out.println("------------------------------------");
                            break;
                        }
                    }
                } else {
                    System.out.println("Título: " + Jsoup.parse(entry.getTitle()).text());
                    System.out.println("Link: " + entry.getLink());
                    System.out.println("Descripción: " + Jsoup.parse(entry.getDescription().getValue()).text());
                    System.out.println("------------------------------------");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
