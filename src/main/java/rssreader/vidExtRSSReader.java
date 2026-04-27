package rssreader;

import clases.News;
import com.google.cloud.Timestamp;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class vidExtRSSReader {
    public List<News> getDatos() throws IOException, FeedException {
        String feedurl = "https://www.vidaextra.com/feedburner.xml"; //vidaextra
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
            contenido = entry.getDescription().getValue();
            if (!encontrado) {
                n.setTitulo(titulo);
                n.setLink(link);
                n.setDescripcion(contenido);
                n.setAutor(Jsoup.parse(entry.getAuthor()).text());
                n.setFecha(Timestamp.of(entry.getPublishedDate()));
                n.setCategoria("Juegos");
                n.setImagen(Jsoup.parse(entry.getDescription().getValue()).select("img").attr("src"));
                n.setSource("Vida Extra");
                retorno.add(n);
            }
        }
        return retorno;
    }

    public List<String> getInfoRelevante() {
        List<String> retorno = new ArrayList<>();
        retorno.add("Gaming");
        retorno.add("Hardware");
        retorno.add("Lanzamientos y anuncios");
        retorno.add("Industria");
        retorno.add("Esports");
        return retorno;
    }
}
