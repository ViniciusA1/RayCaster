package com.raycaster.engine;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Classe que conteḿ os atributos e métodos dos efeitos sonoros do jogo.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class EfeitosSonoros {

    private final Map<Estado, Clip> sons;

    /**
     * Construtor da classe que recebe o nome do objeto e um conjunto de seus
     * possíveis estados.
     *
     * @param nome Nome do objeto associado ao som
     * @param possiveisEstados Possíveis estados que o objeto pode assumir
     */
    public EfeitosSonoros(String nome, EnumSet<Estado> possiveisEstados) {
        sons = new HashMap<>();
        carregarSons(nome, possiveisEstados);
    }

    /**
     * Carrega todos os sons associados aos estados do objeto.
     *
     * @param nome Nome do objeto
     * @param possiveisEstados Possíveis estados associados ao objeto
     */
    private void carregarSons(String nome, EnumSet<Estado> possiveisEstados) {
        for (Estado estadoAux : possiveisEstados) {
            String nomeEstado = estadoAux.name();

            try {
                Clip clip = AudioSystem.getClip();

                String caminhoSom = Diretorio.convertePath(Diretorio.SONS + nome,
                        estadoAux) + ArquivoUtils.FORMATO_SOM;

                AudioInputStream audioInputStream = AudioSystem.
                        getAudioInputStream(new File(caminhoSom));

                clip.open(audioInputStream);
                sons.put(estadoAux, clip);
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Emite o som de acordo com o estado recebido.
     *
     * @param estadoAtual Estado recebido pelo método
     */
    public synchronized void emiteSom(Estado estadoAtual) {
        Clip somDesejado = sons.get(estadoAtual);

        if (somDesejado == null)
            return;
        
        if(somDesejado.isRunning()) {
            somDesejado.stop();
        }
        
        Thread threadSom = new Thread(() -> {
            somDesejado.setFramePosition(0);
            somDesejado.start();
        });
        
        threadSom.start();
    }
}
