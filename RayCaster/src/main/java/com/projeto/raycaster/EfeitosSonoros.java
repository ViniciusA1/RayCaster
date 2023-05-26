package com.projeto.raycaster;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author vinicius
 */
public class EfeitosSonoros {
    private Map<Estado, Clip> sons;

    public EfeitosSonoros(String nome) {
        sons = new HashMap<>();
        carregarSons(nome);
    }

    private void carregarSons(String nome) {
        Estado[] possiveisEstados = Estado.values();
        
        for (Estado estadoAux : possiveisEstados) {
            String nomeEstado = estadoAux.name();
            try {
                Clip clip = AudioSystem.getClip();
                
                String caminhoSom = Diretorio.convertePath(Diretorio.SONS + nome, estadoAux) + 
                                    ArquivoUtils.FORMATO_SOM;
                
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(caminhoSom));
                
                clip.open(audioInputStream);
                sons.put(estadoAux, clip);
            } catch (Exception e) {
                System.err.println("O estado " + nomeEstado +" não está atribuido ao item " + nome);
            }
        }
    }

    public void emiteSom(Estado estadoAtual) {
        Clip somDesejado = sons.get(estadoAtual);
        
        if(somDesejado == null)
            return;
            
        if(somDesejado.isRunning()) {
            somDesejado.stop();
            System.out.println("Parou o som anterior");
        }
        
        somDesejado.setFramePosition(0);
        somDesejado.start();
        System.out.println("Chamou o som\n");
    }
}
