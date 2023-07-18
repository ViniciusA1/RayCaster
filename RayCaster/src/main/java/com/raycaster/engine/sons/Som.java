package com.raycaster.engine.sons;

import com.raycaster.utils.ArquivoUtils;
import com.raycaster.utils.Diretorio;
import com.raycaster.engine.Estado;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author vinicius
 */
public abstract class Som {

    private final Map<Estado, Clip> sons;

    /**
     * Construtor da classe que recebe o nome do objeto e um conjunto de seus
     * possíveis estados.
     *
     * @param nome Nome do objeto associado ao som
     * @param possiveisEstados Possíveis estados que o objeto pode assumir
     */
    public Som(String nome, EnumSet<Estado> possiveisEstados) {
        sons = new HashMap<>();
        carregarSons(nome, possiveisEstados);
    }

    public Som(String nome, Estado estadoUnico) {
        sons = new HashMap<>();
        EnumSet<Estado> possiveisEstados = EnumSet.noneOf(Estado.class);
        possiveisEstados.add(estadoUnico);
        carregarSons(nome, possiveisEstados);
    }
    
    protected final void init(Som somCriado) {
        SomManager.addSom(somCriado);
    }

    /**
     * Carrega todos os sons associados aos estados do objeto.
     *
     * @param nome Nome do objeto
     * @param possiveisEstados Possíveis estados associados ao objeto
     */
    private void carregarSons(String nome, EnumSet<Estado> possiveisEstados) {
        for (Estado estadoAux : possiveisEstados) {

            try {
                Clip clip = AudioSystem.getClip();

                String caminhoSom = Diretorio.convertePath(Diretorio.SONS + nome,
                        estadoAux) + ArquivoUtils.FORMATO_SOM;

                AudioInputStream audioInputStream = AudioSystem.
                        getAudioInputStream(new File(caminhoSom));

                clip.open(audioInputStream);
                sons.put(estadoAux, clip);

                audioInputStream.close();
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    public final Map<Estado, Clip> getSom() {
        return sons;
    }
    
    public final void setVolume(float novoVolume) {
        for(Clip clipAux : sons.values()) {
            FloatControl controle = (FloatControl) clipAux.getControl(FloatControl.Type.MASTER_GAIN);
            controle.setValue(novoVolume);
        }
    }

    public void close() {
        for (Clip clipAux : sons.values()) {
            clipAux.stop();
            clipAux.close();
        }
        
        SomManager.removeSom(this);

        sons.clear();
    }

    public boolean isRunning(Estado estadoSom) {
        return sons.get(estadoSom).isRunning();
    }
    
    public abstract void playSom(Estado estadoSom);
    
    public abstract void stopSom(Estado estadoSom);
}
