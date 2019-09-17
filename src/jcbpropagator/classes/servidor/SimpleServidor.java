/**
 * Este es un Servidor de HTTP que resuelve las solicitudes
 */
package servidor;


import cbm.ECB;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;

/**
 *
 * @author alfonso
 * @see https://www.codeproject.com/Tips/1040097/Create-a-Simple-Web-Server-in-Java-HTTP-Server
 * @see https://stackoverflow.com/questions/3732109/simple-http-server-in-java-using-only-java-se-api
 */
public class SimpleServidor {

    HttpServer server;
    

    public SimpleServidor(int puerto,ECB ecb) {
        
        try {
            server = HttpServer.create(new InetSocketAddress(puerto), 0);
            server.createContext("/in", new ProcesaHandler(ecb));
            server.setExecutor(null);
            server.start();
        } catch (IOException ioex) {
            System.err.println(ioex);
        }
    }
    
    
    public void detenerServidor(){
        server.stop(0);
        
    }
}
