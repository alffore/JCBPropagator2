// https://www.developer.com/java/data/how-to-code-java-clipboard-functionality.html
// problema en macosx https://langui.sh/2010/11/14/pbpaste-pbcopy-in-mac-os-x-or-terminal-clipboard-fun/
// https://docs.oracle.com/en/java/javase/12/docs/api/java.datatransfer/java/awt/datatransfer/Clipboard.html

package cbm;

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
public class ECB implements ClipboardOwner, FlavorListener{
    
    Clipboard cb;
    String scbname;
    
    ArrayList<SimpleCliente> asc;
    ArrayList<ConexionCliente> acc;
    ArrayList<MemoriaC>almem;
    
    /**
     * 
     * @param acc
     * @param almem 
     */
    public ECB(ArrayList<ConexionCliente> acc,ArrayList<MemoriaC>almem){
        
        this.acc=acc;
        this.almem = almem;
        
        asc = new ArrayList<>();
        
        this.creaClientes();
        
        cb=Toolkit.getDefaultToolkit().getSystemClipboard();
        cb.addFlavorListener(this);
        
        Transferable selection = cb.getContents(this);
        if(selection!=null){
            //this.gainOwnership(selection);
        }
        scbname=cb.getName();
        
        System.out.println("Clipboard name: "+scbname);
       
    }

    /**
     * 
     * @param e 
     */
    @Override
    public void flavorsChanged(FlavorEvent e) {
//        Logger.getLogger(ECB.class.getName()).log(Level.INFO, null, e);
//        System.out.println(Arrays.toString(((Clipboard)e.getSource()).getAvailableDataFlavors()));
        this.recuperaObjetoCB();
    }
    
    /**
     * 
     */
    private void creaClientes(){
        for(var cc: acc){
            String surl = "http://"+cc.sip+":"+cc.puerto+"/in";
            var aux = new SimpleCliente(surl);
            asc.add(aux);
        }
    }
    
    /**
     * 
     */
    private void recuperaObjetoCB(){
        try {
            
            String scad=(String) cb.getData(DataFlavor.stringFlavor);
            
            for(var c: asc){
                //c.eviaMensaje(scad);
                System.out.println("\t"+c.surl+" "+scad);
            }
            
        } 
        catch (UnsupportedFlavorException | IOException e) {}
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        Logger.getLogger(ECB.class.getName()).log(Level.INFO, null, clipboard);
        System.out.println("Se perdio la propiedad del CB");
        //gainOwnership(contents);
    }
    
//    public void gainOwnership(Transferable t){ 
//        try {ClipboardListener.sleep(100);} 
//        catch (InterruptedException e) {}
//        cb.setContents(t, this);  
//    }
}
