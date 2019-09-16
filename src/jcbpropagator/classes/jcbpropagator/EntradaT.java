
package jcbpropagator;

/**
 *
 * @author alfonso
 */
public class EntradaT {
 
    private String snombre = null;
    private String sbuffer = null;
    
    public EntradaT(){}
    
    public EntradaT(String snombre,String sbuffer){
        this.snombre = snombre;
        this.sbuffer = sbuffer;        
    }

    public String getSnombre(){
        return snombre;
    }

    public void setSnombre(String snombre){
        this.snombre = snombre;
    }

    public String getSbuffer(){
        return sbuffer;
    }

    public void setSbuffer(String sbuffer){
        this.sbuffer=sbuffer;
    }
}
