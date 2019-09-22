package cbm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 *
 * @author alfonso
 */
public class ManMem {

    private long idm;
    public ArrayList<MemoriaC> almem;

    /**
     *
     * @param almem
     */
    public ManMem(ArrayList<MemoriaC> almem) {
        this.almem = almem;
        idm = 0;
    }

    /**
     * @see https://www.geeksforgeeks.org/md5-hash-in-java/
     * @param input
     * @return
     */
    public static String getMd5(String input) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param cliente_id
     * @param scad
     * @return
     */
    public MemoriaC insertaMem(int cliente_id, String scad) {
        String smd5 = getMd5(scad);
        
        for (var m : almem) {
            if (m.cliente_id == cliente_id) {
                if (smd5 == m.md5) {
                    return m;
                }
            }
        }

        MemoriaC mem = new MemoriaC();
        mem.cliente_id = cliente_id;
        mem.idm = this.obtenIdm();
        mem.sbuffer = scad;
        mem.md5 = smd5;
        mem.ip ="local";

        almem.add(mem);

        return mem;
    }

    /**
     * 
     * @param scad
     * @param md5
     * @param ip
     * @return 
     */
    public MemoriaC insertaMemExt(String scad,String md5,String ip){
        
        for (var m : almem) {
            if(m.md5.equals(md5) && m.ip.equals(ip)){
                return m;
            }
        }
        
        MemoriaC mem = new MemoriaC();
        mem.idm = this.obtenIdm();
        mem.sbuffer = scad;
        mem.md5 = md5;
        mem.ip=ip;
        
        almem.add(mem);
        
        return mem;
    }
    
    
    
    /**
     *
     * @return
     */
    public synchronized long obtenIdm() {
        return this.idm++;
    }
}
