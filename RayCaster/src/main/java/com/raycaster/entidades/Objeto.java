package com.raycaster.entidades;

import com.raycaster.engine.Estado;
import java.util.EnumSet;

/**
 *
 * @author vinicius
 */
public class Objeto extends Entidade {
    
    public Objeto(String nome, double x, double y, double largura, 
            EnumSet<Estado> possiveisEstados) {
        
        
        super(nome, 0, x, y, largura, 
                0, 0, possiveisEstados);
    }
    
}
