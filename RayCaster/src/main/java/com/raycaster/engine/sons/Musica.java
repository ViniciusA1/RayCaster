package com.raycaster.engine.sons;

import com.raycaster.engine.Estado;
import java.util.EnumSet;
import javax.sound.sampled.Clip;

/**
 *
 * @author vinicius
 */
public class Musica extends Som {
    
    public Musica(String nome, EnumSet<Estado> possiveisEstados) {
        super(nome, possiveisEstados);
        init();
    }
    
    public Musica(String nome, Estado estadoUnico) {
        super(nome, estadoUnico);
        init();
    }
    
    private void init() {
        SomManager.addSom(this);
    }
    
    @Override
    public void playSom(Estado estadoSom) {
        Clip som = getSom().get(estadoSom);
        
        if(som == null || som.isRunning())
            return;
        
        som.start();
        som.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void stopSom(Estado estadoSom) {
        Clip som = getSom().get(estadoSom);
        
        som.stop();
    }
    
}
