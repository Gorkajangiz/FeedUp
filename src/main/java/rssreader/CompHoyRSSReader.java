package rssreader;

import clases.News;
import com.google.cloud.Timestamp;
import com.rometools.modules.content.ContentModule;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CompHoyRSSReader {
    public List<News> getDatos() throws IOException, FeedException {
        String feedurl = "https://computerhoy.20minutos.es/rss/"; //vidaextra
        URL url = new URL(feedurl);
        SyndFeedInput sfi = new SyndFeedInput();
        SyndFeed feed = sfi.build(new XmlReader(url));
        List<News> retorno = new ArrayList<>();
        News n;
        String titulo = "";
        String link = "";
        String contenido = "";
        boolean encontrado = false;
        int limite = 0;
        for (SyndEntry entry : feed.getEntries()) {
            if (limite >= 2) {
                break;
            }
            limite++;
            n = new News();
            titulo = Jsoup.parse(entry.getTitle()).text();
            link = entry.getLink();
            contenido = "";
            ContentModule cm = (ContentModule) entry.getModule("http://purl.org/rss/1.0/modules/content/");
            if (cm != null && cm.getEncodeds() != null && cm.getEncodeds().size() > 0) {
                contenido = cm.getEncodeds().get(0);
            } else if (entry.getDescription() != null) {
                contenido = Jsoup.parse(entry.getDescription().getValue()).text();
            }
            n.setTitulo(titulo);
            n.setLink(link);
            n.setDescripcion(contenido);
            n.setAutor(Jsoup.parse(entry.getAuthor()).text());
            n.setFecha(Timestamp.of(entry.getPublishedDate()));
            n.setCategoria("Juegos");
            n.setImagen(Jsoup.parse(entry.getDescription().getValue()).select("img").attr("src"));
            n.setSource("Computer Hoy");
            retorno.add(n);
        }
        return retorno;
    }

    public List<String> getInfoRelevante() {
        List<String> retorno = new ArrayList<>();
        retorno.add("Tecnología");
        retorno.add("Hardware");
        retorno.add("Lanzamientos y anuncios");
        retorno.add("Industria");
        retorno.add("IA");
        retorno.add("Software");
        return retorno;
    }
}
