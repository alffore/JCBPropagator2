// https://www.developer.com/java/data/how-to-code-java-clipboard-functionality.html
// problema en macosx https://langui.sh/2010/11/14/pbpaste-pbcopy-in-mac-os-x-or-terminal-clipboard-fun/
// https://docs.oracle.com/en/java/javase/12/docs/api/java.datatransfer/java/awt/datatransfer/Clipboard.html
package cbm;

import static cbm.ManMem.getMd5;
import cliente.*;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;

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

    /**
     *
     * @param acc
     * @param almem
     */
    public ECB(ArrayList<ConexionCliente> acc, ManMem mm) {

        this.acc = acc;
        this.mm = mm;

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
            String surl = "http://" + cc.sip + ":" + cc.puerto + "/in";
            var aux = new SimpleCliente(surl);
            asc.add(aux);
        }
    }

    /**
     *
     */
    public void recuperaObjetoCB() {

        try {

            String scad = (String) cb.getData(DataFlavor.stringFlavor);
            System.out.println("se recupera String de CB local: " + scad + ", " + getMd5(scad));

            MemoriaC mem =mm.insertaMem(0, scad);
            
            for (var c : asc) {
                //c.eviaMensaje(scad);
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

}
