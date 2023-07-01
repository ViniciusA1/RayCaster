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
    private static Clip somAtual;
    private static Thread thread;
    
    static {
        thread = new Thread(() -> {
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            somAtual.setFramePosition(0);
            somAtual.start();
        });
    }

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
    
    public EfeitosSonoros(String nome, Estado estadoUnico) {
        sons = new HashMap<>();
        EnumSet<Estado> possiveisEstados = EnumSet.noneOf(Estado.class);
        possiveisEstados.add(estadoUnico);
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
                
                audioInputStream.close();
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
        somAtual = sons.get(estadoAtual);

        if (somAtual == null)
            return;
        
        if(somAtual.isRunning()) {
            somAtual.stop();
        }
        
        thread.run();
    }
    
    public void setLoop(Estado estadoSom) {
        somAtual = sons.get(estadoSom);
        
        if(somAtual == null)
            return;
        
        somAtual.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    public void stopLoop(Estado estadoSom) {
        somAtual = sons.get(estadoSom);
        
        if(somAtual == null)
            return;
        
        somAtual.stop();
    }
    
    public void close() {
        for(Clip clipAux : sons.values()) {
            clipAux.stop();
            clipAux.close();
        }
        
        somAtual = null;
        
        sons.clear();
    }
    
    public boolean isRunning(Estado estadoSom) {
        return sons.get(estadoSom).isRunning();
    }
}
