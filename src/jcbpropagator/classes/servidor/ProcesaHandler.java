package servidor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author alfonso
 */
public class ProcesaHandler implements HttpHandler {

    int id;

    public ProcesaHandler() {
        id = 0;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = " " + id;
        t.sendResponseHeaders(200, response.length());
        try (OutputStream os = t.getResponseBody()) {
            os.write(response.getBytes());
        }
        id++;
    }

}
