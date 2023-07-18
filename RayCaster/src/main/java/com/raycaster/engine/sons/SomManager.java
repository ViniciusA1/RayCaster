package com.raycaster.engine.sons;

import com.raycaster.engine.Estado;
import com.raycaster.utils.ArquivoUtils;
import com.raycaster.utils.Diretorio;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author vinicius
 */
public class SomManager {

    private static final List<Musica> listaMusica;
    private static final List<EfeitoSonoro> listaSfx;
    
    private static float decibeisMusica;
    private static float decibeisSfx;
    
    private static int volumeMusica;
    private static int volumeSfx;

    static {
        listaMusica = new ArrayList<>();
        listaSfx = new ArrayList<>();
        
        Properties propriedades = ArquivoUtils.
                lePropriedade(Diretorio.DADOS_ENGINE + "config");
        
        volumeMusica = Integer.parseInt(propriedades.
                getProperty("musica"));
        
        volumeSfx = Integer.parseInt(propriedades.
                getProperty("sfx"));
        
        volumeMusica = Math.min(Math.max(volumeMusica, 0), 100);
        volumeSfx = Math.min(Math.max(volumeSfx, 0), 100);
        
        decibeisMusica = converteVolume(volumeMusica);
        decibeisSfx = converteVolume(volumeSfx);
    }

    private SomManager() {
    }
    
    public static int getVolumeMusica() {
        return volumeMusica;
    }
    
    public static int getVolumeSfx() {
        return volumeSfx;
    }

    public static void addSom(Som novoSom) {
        if(novoSom == null)
            return;
        
        if (novoSom instanceof Musica musica) {
            listaMusica.add(musica);
            novoSom.setVolume(decibeisMusica);
        } else {
            listaSfx.add((EfeitoSonoro) novoSom);
            novoSom.setVolume(decibeisSfx);
        }
    }

    public static void removeSom(Som somAtual) {
        if (somAtual instanceof Musica musica) {
            listaMusica.remove(musica);
        } else {
            listaSfx.remove((EfeitoSonoro) somAtual);
        }
    }

    private static float converteVolume(int novoVolume) {
        float volumeLog = (float) (Math.log10(novoVolume / 100.0) * 20.0);
        
        if(volumeLog < -80.0f)
            volumeLog = -80.0f;
        else if(volumeLog > 6.0206f)
            volumeLog = 6.0206f;
        
        return volumeLog;
    }

    public static void ajustaMusica(int novoVolume) {
        volumeMusica = novoVolume;
        decibeisMusica = converteVolume(novoVolume);
        
        for (Musica musicaAux : listaMusica) {
            musicaAux.setVolume(decibeisMusica);
        }
    }

    public static void ajustaSfx(int novoVolume) {
        volumeSfx = novoVolume;
        decibeisSfx = converteVolume(novoVolume);
        
        for (EfeitoSonoro sfxAux : listaSfx) {
            sfxAux.setVolume(decibeisSfx);
        }
    }

    public synchronized static void close() {
        for (Musica musicaAux : listaMusica) {
            musicaAux.close();
        }

        for (EfeitoSonoro sfxAux : listaSfx) {
            sfxAux.close();
        }

        listaMusica.clear();
        listaSfx.clear();
    }
}
