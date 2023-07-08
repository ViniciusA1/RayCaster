package com.raycaster.engine.sons;

import com.raycaster.engine.Estado;
import java.util.EnumSet;
import javax.sound.sampled.Clip;

/**
 * Classe que conteḿ os atributos e métodos dos efeitos sonoros do jogo.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class EfeitoSonoro extends Som {

    public EfeitoSonoro(String nome, EnumSet<Estado> possiveisEstados) {
        super(nome, possiveisEstados);
        init();
    }
    
    public EfeitoSonoro(String nome, Estado estadoUnico) {
        super(nome, estadoUnico);
        init();
    }
    
    private void init() {
        SomManager.addSom(this);
    }

    @Override
    public void playSom(Estado estadoSom) {
        Clip som = getSom().get(estadoSom);

        if (som == null) {
            return;
        }

        if (som.isRunning()) {
            som.stop();
        }

        try {
            Thread.sleep(0, 1);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        som.setFramePosition(0);
        som.start();
    }

    @Override
    public void stopSom(Estado estadoSom) {
        Clip som = getSom().get(estadoSom);
        
        som.stop();
    }

}
