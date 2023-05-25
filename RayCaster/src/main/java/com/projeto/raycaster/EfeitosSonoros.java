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

    private final static String PATH = "sons" + File.separator;
    private Map<Estado, Clip> sons;

    public EfeitosSonoros(String nome) {
        sons = new HashMap<>();
        carregarSons(nome);
    }

    private void carregarSons(String nome) {
        File pastaSons = new File(PATH + File.separator + nome);

        Estado[] possiveisSons = Estado.values();

        try {
            Clip clipAtirando = AudioSystem.getClip();
            AudioInputStream audioInputStreamAtirando = AudioSystem.getAudioInputStream(new File(PATH + File.separator + nome + File.separator + nome + "-" + "ATIRANDO" + ".wav"));
            clipAtirando.open(audioInputStreamAtirando);
            sons.put(Estado.ATIRANDO, clipAtirando);

            Clip clipRecarregando = AudioSystem.getClip();
            AudioInputStream audioInputStreamRecarregando = AudioSystem.getAudioInputStream(new File(PATH + File.separator + nome + File.separator + nome + "-" + "RECARREGANDO" + ".wav"));
            clipRecarregando.open(audioInputStreamRecarregando);
            sons.put(Estado.RECARREGANDO, clipRecarregando);
        } catch (Exception e) {
            System.err.println("Som de " + nome + " não existe!");
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
