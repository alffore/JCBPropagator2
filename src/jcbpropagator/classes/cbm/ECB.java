// https://www.developer.com/java/data/how-to-code-java-clipboard-functionality.html
// problema en macosx https://langui.sh/2010/11/14/pbpaste-pbcopy-in-mac-os-x-or-terminal-clipboard-fun/
// https://docs.oracle.com/en/java/javase/12/docs/api/java.datatransfer/java/awt/datatransfer/Clipboard.html
package cbm;

import static cbm.ManMem.getMd5;
import cliente.*;
import com.eclipsesource.json.JsonObject;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;
import javafx.scene.control.TableView;
import jcbpropagator.EntradaT;

/**
 *
 * @author alfonso
 */
public class ECB implements ClipboardOwner, FlavorListener {

    Clipboard cb;
    String scbname;

    ArrayList<SimpleCliente> asc;
    ArrayList<ConexionCliente> acc;

    ManMem mm;
    TableView tableView;

    /**
     *
     * @param acc
     * @param mm
     * @param tv
     */
    public ECB(ArrayList<ConexionCliente> acc, ManMem mm, TableView tv) {

        this.acc = acc;
        this.mm = mm;
        this.tableView = tv;

        asc = new ArrayList<>();

        this.creaClientes();

        cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        cb.addFlavorListener(this);

    }

    /**
     *
     * @param e
     */
    @Override
    public void flavorsChanged(FlavorEvent e) {
        this.recuperaObjetoCB();
    }

    /**
     *
     */
    private void creaClientes() {
        for (var cc : acc) {
            if ("cliente".equals(cc.stipo)) {
                String surl = "http://" + cc.sip + ":" + cc.puerto + "/in";
                var aux = new SimpleCliente(surl);
                asc.add(aux);
            }
        }
    }

    /**
     *
     */
    public void recuperaObjetoCB() {

        try {

            String scad = (String) cb.getData(DataFlavor.stringFlavor);
            System.out.println("se recupera String de CB local: " + scad + ", " + getMd5(scad));

            MemoriaC mem = mm.insertaMem(0, scad);
            ponEntradasTV();
            
            for (SimpleCliente c : asc) {
                c.eviaMensaje2(scad,mem.md5);
                System.out.println("\t" + c.surl + " " + scad);
            }

        } catch (UnsupportedFlavorException | IOException e) {
        }
    }

    /**
     *
     * @param clipboard
     * @param contents
     */
    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        Logger.getLogger(ECB.class.getName()).log(Level.INFO, null, clipboard);
        System.out.println("Se perdio la propiedad del CB");
        this.recuperaObjetoCB();
    }

    /**
     * 
     */
    public void ponEntradasTV(){
        tableView.getItems().clear();
        for(var m: mm.almem){
            //if(m.cliente_id!=0){
                tableView.getItems().add(new EntradaT(m.ip,m.sbuffer.substring(0, 30)+"..."));
            //}
        }
    }
    
    public void ponMemoria(JsonObject jsono){
        mm.insertaMemExt(jsono.getString("cadena",""), jsono.getString("md5",""), jsono.getString("ip",""));
        this.ponEntradasTV();
    }
}
