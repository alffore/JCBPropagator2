package cliente;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.net.http.HttpResponse.*;
import java.nio.charset.StandardCharsets;
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
     * Método que envia un mensaje via POST con un json
     * @param scad 
     * @param smd5 
     */
    public void eviaMensaje2(String scad,String smd5) {
        
        
        JsonObject jmessage = Json.object().add("cadena", scad).add("md5", smd5);
        
        System.out.println("ENVIO: "+jmessage.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(surl))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jmessage.toString()))
                .build();

        cliente.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                ;//.join();

    }
    

}
