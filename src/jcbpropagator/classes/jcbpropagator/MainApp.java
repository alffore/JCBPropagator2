// https://stackoverflow.com/questions/44973129/javafx-alert-and-stage-focus
// http://broadlyapplicable.blogspot.com/2015/02/javafx-restore-window-size-position.html

package jcbpropagator;

import cbm.ECB;
import cbm.ManMem;
import cbm.MemoriaC;
import cliente.ConexionCliente;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import servidor.SimpleServidor;
import java.util.prefs.Preferences;

/**
 *
 * @author alfonso
 */
public class MainApp extends Application {

    private static final String WINDOW_POSITION_X = "Window_Position_X";
    private static final String WINDOW_POSITION_Y = "Window_Position_Y";
    private static final String WINDOW_WIDTH = "Window_Width";
    private static final String WINDOW_HEIGHT = "Window_Height";
    private static final double DEFAULT_X = 10;
    private static final double DEFAULT_Y = 10;
    private static final double DEFAULT_WIDTH = 300;
    private static final double DEFAULT_HEIGHT = 200;
    private static final String NODE_NAME = "MainApp";
    
    
    
    private static String[] args;

    ArrayList<MemoriaC> almem;

    ArrayList<ConexionCliente> alconex;

    private int clientes;
    SimpleServidor ss;
    
    TableView tableView;
    
    ManMem manm;
    ECB ecb;

    /**
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        almem = new ArrayList<>();
        alconex = new ArrayList<>();
        clientes = 0;

        
        tableView = new TableView();
        
        
        this.recuperaConexiones(args[0]);
        manm = new ManMem(almem);
        ecb = new ECB(alconex,manm,tableView);
        ss = new SimpleServidor(8000,ecb);
        
        

        TableColumn<String, EntradaT> column1 = new TableColumn<>("Maquina");
        column1.setCellValueFactory(new PropertyValueFactory<>("snombre"));

        TableColumn<String, EntradaT> column2 = new TableColumn<>("Memoria");
        column2.setCellValueFactory(new PropertyValueFactory<>("sbuffer"));

        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);

        VBox vbox = new VBox(tableView);

        Scene scene = new Scene(vbox,300,200);
        
        //recupera preferencias
        Preferences pref = Preferences.userRoot().node(NODE_NAME);
        double x = pref.getDouble(WINDOW_POSITION_X, DEFAULT_X);
        double y = pref.getDouble(WINDOW_POSITION_Y, DEFAULT_Y);
        double width = pref.getDouble(WINDOW_WIDTH, DEFAULT_WIDTH);
        double height = pref.getDouble(WINDOW_HEIGHT, DEFAULT_HEIGHT);
        primaryStage.setX(x);
        primaryStage.setY(y);
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        
       

        primaryStage.setScene(scene);
        
        Image imag = new Image("icono5.png");
        primaryStage.getIcons().add(imag);
        
        primaryStage.setTitle("JCBPropagator");
        
        primaryStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            recuperaUltimaMemoria(oldValue,newValue);
        });
        
        // guarda configuracion de posicion y tamaño
        primaryStage.setOnCloseRequest((final WindowEvent event) -> {
        Preferences preferences = Preferences.userRoot().node(NODE_NAME);
            preferences.putDouble(WINDOW_POSITION_X, primaryStage.getX());
            preferences.putDouble(WINDOW_POSITION_Y, primaryStage.getY());
            preferences.putDouble(WINDOW_WIDTH, primaryStage.getWidth());
            preferences.putDouble(WINDOW_HEIGHT, primaryStage.getHeight());
        });

        primaryStage.show();
    }
    

    /**
     * 
     * @throws Exception 
     */
    public void stop() throws Exception {
        ss.detenerServidor();
    }

    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {

        MainApp.args = args;

        launch(args);

    }

    /**
     * Método que procesa el archivo de configuracion JSON
     * @param sarchivo
     */
    private void recuperaConexiones(String sarchivo) {
        if(sarchivo== null || sarchivo.length()==0){
            return;
        }
        
        try {
            FileReader mifr = new FileReader(sarchivo);
            char[] buffer = new char[8096];
            int numberOfCharsRead = mifr.read(buffer);
            String sjson = String.valueOf(buffer, 0, numberOfCharsRead);
            //System.out.println(numberOfCharsRead + ":: " + sjson);

            JsonArray items = Json.parse(sjson).asArray();

            for (JsonValue item : items) {
                ConexionCliente cc = new ConexionCliente();

                cc.cliente_id = clientes;

                cc.snombre = item.asObject().getString("nombre", "Indefinido");
                cc.sip = item.asObject().getString("ip", "");
                cc.puerto = item.asObject().getInt("puerto", 8000);
                cc.stipo = item.asObject().getString("tipo","cliente");
                alconex.add(cc);

                clientes++;
            }

            mifr.close();

            alconex.forEach((c) -> {
                System.out.println(c);
            });

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * @param oldValue
     * @param newValue 
     */
    private void recuperaUltimaMemoria(Boolean oldValue,Boolean newValue){
        
        if(newValue){
            //System.out.println("PrimaryStage focused : "+newValue+" ("+oldValue+")");
            ecb.recuperaObjetoCB();
        }
    }
}
