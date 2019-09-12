package cliente;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpResponse.*;
import java.time.Duration;

/**
 *
 * @author alfonso
 * @see https://openjdk.java.net/groups/net/httpclient/intro.html
 * @see https://github.com/ralscha/blog2019/tree/master/java11httpclient
 * @see https://dzone.com/articles/java-11-standardized-http-client-api
 */
public class SimpleCliente {

    //private int ultimo_ide; 
    HttpClient cliente;

    public String surl;

    /**
     *
     * @param surl
     */
    public SimpleCliente(String surl) {
        this.surl = surl;
        cliente = HttpClient.newHttpClient();
    }

    /**
     * Método para enviar mensaje en forma asincrona a los clientes conectados
     */
    public void eviaMensaje(String scad) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(surl))
                .build();

        cliente.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();

    }

    /**
     * Metodo que envia un mensaje via POST con un json
     * @param scad 
     */
    public void eviaMensaje2(String scad) {
        
        
        JsonObject jmessage = Json.object().add("cadena", scad).add("points", 23);
        
        

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(surl))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jmessage.toString()))
                .build();

        cliente.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();

    }
}
