package servidor;

import cbm.ECB;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
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
        String response = procesaJSON();
        t.sendResponseHeaders(200, response.length());
        try (OutputStream os = t.getResponseBody()) {
            os.write(response.getBytes());
        }

    }

    private String procesaJSON() {
        return "ok";
    }
}
