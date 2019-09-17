
package cliente;

/**
 *
 * @author alfonso
 */
public class ConexionCliente {
    public int cliente_id;
    public int idex;
    
    public String snombre;
    public String sip;
    public int puerto;
    public String stipo;
    
    @Override
    public String toString(){
        return cliente_id+"::"+snombre+"::"+sip+"::"+puerto;
    }
}
