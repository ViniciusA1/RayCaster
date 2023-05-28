/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raycaster.mapa;

import com.raycaster.mapa.Coords.Face;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.JList;

/**
 *
 * @author bruno
 */
public class MapGroup {
    private ArrayList<Mapa> mapas = new ArrayList<>();
    private ArrayList<Gate> gates = new ArrayList<>();
    
    public void addMapa(Mapa m){
        mapas.add(m);
        m.setGrupo(this);
    }
    
    public void removeMapa(Mapa m){
        mapas.add(m);
    }
    
    public void addGate(Gate g){
        gates.add(g);
    }
    
    public void removeGate(Gate g){
        gates.remove(g);
    }
    
    public Mapa getMapa(int index){
        return mapas.get(index);
    }
    public JList<Mapa> getMapa(){
        return new JList<>(new ListData<>(mapas));
    }
    
    public Gate getGate(int index){
        return gates.get(index);
    }
    public JList<Gate> getGate(){
        return new JList<>(new ListData<>(gates));
    }

    public boolean checaGate(Mapa map, int x, int y){
        if(mapas.contains(map)){
            for(Gate aux: gates){
                if(aux.getIn().equals(map)){
                    return true;
                }
                else if(aux.getSentido().equals(Gate.Sentido.BI_DIRECIONAL)  &&  aux.getOut().equals(map)){
                    return true;
                }
            }
        }
        return false;
    }
    
    public Coords converte(Mapa mapa, int x, int y, Face face){
        if(mapas.contains(mapa)){
            for(Gate aux: gates){
                if(aux.getIn().equals(mapa)  ||  (aux.getSentido().equals(Gate.Sentido.BI_DIRECIONAL)  &&  aux.getOut().equals(mapa))){
                    if(aux.getInCoord().x == x  &&  aux.getInCoord().y == y  &&  aux.getInCoord().face == face){
                        Coords novo = aux.getOutCoord();
                        if(novo.face == Face.NORTE)
                            novo.x--;
                        if(novo.face == Face.SUL)
                            novo.y--;
                        return novo;
                    }
                }
            }
        }
        return null;
    }
    
    public static Face checaFace(int pos1, int pos2, char eixo){
        int pos = pos2 - pos1;
        if(eixo == 'x'){
            if(pos > 0)
                return Face.OESTE;
            else
                return Face.LESTE;
        }
        else{
            if(pos > 0)
                return Face.NORTE;
            else
                return Face.SUL;
        }
    }
    
    public static class ListData<T> extends AbstractListModel{
        ArrayList<T> element;

        public ListData(ArrayList<T> elements) {
            this.element = elements;
        }

        @Override
        public int getSize() {
          return element.size();
        }

        @Override
        public Object getElementAt(int index) {
          return (index + 1) + " - " + element.get(index).toString();
        }
    }
}
