package servidor;

import cbm.ECB;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 *
 * @author alfonso
 */
public class ProcesaHandler implements HttpHandler {

    ECB ecb;

    public ProcesaHandler(ECB ecb) {
        this.ecb = ecb;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = procesaJSON(t.getRequestBody());
        t.sendResponseHeaders(200, response.length());
        try (OutputStream os = t.getResponseBody()) {
            os.write(response.getBytes());
        }

    }

    
    /**
     * @see https://stackoverflow.com/questions/14392967/best-way-to-read-an-input-stream-to-a-buffer
     * @see https://www.codeproject.com/Tips/1040097/Create-a-Simple-Web-Server-in-Java-HTTP-Server
     * @param in
     * @return
     * @throws IOException 
     */
    private String procesaJSON(InputStream in) throws IOException {

        StringBuilder sb = new StringBuilder();

        try (BufferedReader rdr = new BufferedReader(new InputStreamReader(in,"utf-8"))) {
            for (int c; (c = rdr.read()) != -1;) {
                sb.append((char) c);

            }
        }
        //return sb.toString();
        System.out.println("RECIBIO: "+sb.toString());

        return "Respuesta "+sb.toString();
    }
}
