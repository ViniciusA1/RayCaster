package com.raycaster.engine.sons;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vinicius
 */
public class SomManager {
    private static final List<Musica> listaMusica;
    private static final List<EfeitoSonoro> listaSfx;
    
    static {
        listaMusica = new ArrayList<>();
        listaSfx = new ArrayList<>();
    }
    
    private SomManager(){}
    
    public static void addSom(Som novoSom) {
        if(novoSom instanceof Musica musica)
            listaMusica.add(musica);
        else
            listaSfx.add((EfeitoSonoro) novoSom);
    }
    
    public static void removeSom(Som somAtual) {
        if(somAtual instanceof Musica musica)
            listaMusica.remove(musica);
        else
            listaSfx.remove((EfeitoSonoro) somAtual);
    }
    
    public static void ajustaMusica(int novoVolume) {
        
    }
    
    public static void ajustaSfx(int novoVolume) {
        
    }
    
    public synchronized static void close() {
        for(Musica musicaAux : listaMusica)
            musicaAux.close();
        
        for(EfeitoSonoro sfxAux : listaSfx)
            sfxAux.close();
        
        listaMusica.clear();
        listaSfx.clear();
    }
}
