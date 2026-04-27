package ia;

import clases.News;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import rssreader.CompHoyRSSReader;
import rssreader.vidExtRSSReader;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class GroqClient {
    private final String apiUrl = "Privado";
    private final String apiKey = "Privado";
    private HttpClient client = HttpClient.newHttpClient();
    private ObjectMapper mapper = new ObjectMapper();

    public String classify(News noticia) throws IOException, InterruptedException {
        vidExtRSSReader readerVERR = new vidExtRSSReader();
        CompHoyRSSReader readerCHRR = new CompHoyRSSReader();
        StringBuilder sb = new StringBuilder();
        String source = "";
        switch (noticia.getSource()) {
            case "Vida Extra":
                List<String> listaCHRR = readerVERR.getInfoRelevante();
                for (int i = 1; i < listaCHRR.size(); i++) {
                    sb.append(i + ". " + listaCHRR.get(i) + "\n");
                }
                source = "Gaming";
                break;
            case "Computer Hoy":
                List<String> listaVERR = readerCHRR.getInfoRelevante();
                for (int i = 1; i < listaVERR.size(); i++) {
                    sb.append(i + ". " + listaVERR.get(i) + "\n");
                }
                source = "Tecnología";
                break;
        }

        String prompt = """
                    Clasifica la noticia según su relevancia y subcategoría dentro de la industria a la que corresponda.
                
                    Categoría: %s
                    Subcategorías posibles:
                    %s
                
                    Reglas de clasificación para GAMING:
                
                    Relevante (TRUE) si:
                    - describe un evento reciente dentro de su industria
                    - anuncia un producto, actualización, lanzamiento o cambio importante
                    - análisis de novedades o impacto real en la industria
                
                    Irrelevante (FALSE) si:
                    - ranking, listas o top-X
                    - opiniones sin novedad
                    - articulos y reviews sobre un producto que no sea un lanzamiento
                    - contenido promocional, tutorial, guía, trucos
                    - curiosidades o historia pasada sin impacto actual
                    - noticias de cine, anime, series o cultura pop
                
                    Noticia:
                    Título: %s
                    Descripción: %s
                
                    Responde SOLO en JSON con este formato EXACTO:
                    {"relevante": true/false, "subcategoria": "nombre exacto de una subcategoría"}
                """.formatted(source, sb, noticia.getTitulo(), noticia.getDescripcion());
        Map<String, Object> body = Map.of(
                "model", "llama-3.1-8b-instant",
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "temperature", 0
        );

        String json = mapper.writeValueAsString(body);

        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(apiUrl)).header("Authorization", "Bearer " + apiKey).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8)).build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        String respuesta = response.body();
        JsonNode root = mapper.readTree(respuesta);
        System.out.println(response.statusCode());
        System.out.println(response.body());
        JsonNode contentNode = root.get("choices").get(0).get("message").get("content");
        JsonNode resultNode = new ObjectMapper().readTree(contentNode.asText());
        boolean relevante = resultNode.get("relevante").asBoolean();
        String answer = relevante ? "TRUE" : "FALSE";
        System.out.println("RESPUESTA: " + answer);
        System.out.println("NOTICIA: " + noticia.getTitulo());

        return answer.equalsIgnoreCase("TRUE") ? "TRUE" : "FALSE";
    }
}
