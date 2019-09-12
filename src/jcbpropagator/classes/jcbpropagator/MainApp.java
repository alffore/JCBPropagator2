
package jcbpropagator;

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
import javafx.stage.Stage;
import servidor.SimpleServidor;

/**
 *
 * @author alfonso
 */
public class MainApp extends Application{

    private String[] args;
    
    ArrayList<MemoriaC> almem ;
    
    ArrayList<ConexionCliente> alconex;
    
    private int clientes;
    SimpleServidor ss;
    
    
    public MainApp(String[] args){
        this.args=args;
        clientes=0;
        
        
        almem = new ArrayList<>();
        alconex = new ArrayList<>();
        
        //this.recuperaConexiones(args[0]);
        
       ss = new SimpleServidor(8000);
    }
    
    /**
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
       
        TableView tableView = new TableView();
        
        TableColumn<String, EntradaT> column1 = new TableColumn<>("Maquina");
        column1.setCellValueFactory(new PropertyValueFactory<>("snombre"));
        
        TableColumn<String, EntradaT> column2 = new TableColumn<>("Memoria");
        column2.setCellValueFactory(new PropertyValueFactory<>("smemoria"));
        
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        
        VBox vbox = new VBox(tableView);
        
        Scene scene = new Scene(vbox);
        
        primaryStage.setScene(scene);
        
        primaryStage.show();
    }
    
    
    public static void main(String[] args) {    
        var mapp = new MainApp(args);
        launch(args);
        mapp.ss.paraServidor();
    }
    
    /**
     * 
     * @param sarchivo 
     */
    private void recuperaConexiones(String sarchivo){
        try {
            FileReader mifr = new FileReader(sarchivo);
            char[] buffer = new char[8096];
            int numberOfCharsRead = mifr.read(buffer);
            String sjson =String.valueOf(buffer, 0, numberOfCharsRead);
            System.out.println(numberOfCharsRead+":: "+sjson);
            
            JsonArray items = Json.parse(sjson).asArray();
            
            for(JsonValue item: items){
                ConexionCliente cc = new ConexionCliente();
                
                cc.cliente_id=clientes;
                
                cc.snombre=item.asObject().getString("nombre", "Indefinido");
                cc.sip=item.asObject().getString("ip","");
                cc.puerto=item.asObject().getInt("puerto", 8000);
                
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
}
