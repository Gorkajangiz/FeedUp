package main;

import org.jsoup.Jsoup;

import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class prueba {
    public static void main(String[] args) {

        while (true) {
            try {
                System.out.println("Actualizando RSS...");

                Document doc = Jsoup.connect("https://www.levelup.com/")
                        .userAgent("Mozilla/5.0")
                        .get();

                // 🔥 Seleccionamos TODOS los artículos
                Elements articulos = doc.select("article");

                StringBuilder rss = new StringBuilder();

                rss.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                rss.append("<rss version=\"2.0\">\n");
                rss.append("<channel>\n");
                rss.append("<title>LevelUp Noticias</title>\n");
                rss.append("<link>https://www.levelup.com/</link>\n");
                rss.append("<description>Feed automático</description>\n");

                int count = 0;

                for (Element art : articulos) {

                    Element tituloEl = art.selectFirst("h3 a");
                    if (tituloEl == null) continue;

                    String titulo = tituloEl.text();
                    String link = tituloEl.absUrl("href");

                    Element descEl = art.selectFirst("p.line-clamp-2");
                    String descripcion = (descEl != null) ? descEl.text() : "";

                    Element autorEl = art.selectFirst("span");
                    String autor = (autorEl != null) ? autorEl.text() : "Desconocido";

                    rss.append("<item>\n");
                    rss.append("<title>").append(titulo).append("</title>\n");
                    rss.append("<link>").append(link).append("</link>\n");
                    rss.append("<description>")
                            .append(descripcion)
                            .append(" - Autor: ").append(autor)
                            .append("</description>\n");
                    rss.append("<pubDate>")
                            .append(ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                            .append("</pubDate>\n");
                    rss.append("</item>\n");

                    count++;
                    if (count >= 15) break;
                }

                rss.append("</channel>\n</rss>");

                FileWriter writer = new FileWriter("feed.xml");
                writer.write(rss.toString());
                writer.close();

                System.out.println("RSS generado con " + count + " noticias.");

                Thread.sleep(3600000); // 1 hora

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}