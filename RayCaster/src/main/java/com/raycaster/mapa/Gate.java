/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raycaster.mapa;

/**
 *
 * @author bruno
 */
public class Gate{

    public static enum Sentido {BI_DIRECIONAL, MONO_DIRECIONAL};
    public static enum Tipo {PORTA, TRANLOC};
    private Tipo tipo;
    private Sentido sentido;
    private Mapa in;
    private Coords inCoord;
    private Mapa out;
    private Coords outCoord;
    

    public Gate(Mapa in, Mapa out) {
        this.tipo = null;
        this.sentido = null;
        this.in = in;
        this.inCoord = null;
        this.out = out;
        this.outCoord = null;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Sentido getSentido() {
        return sentido;
    }

    public Mapa getIn() {
        return in;
    }

    public Mapa getOut() {
        return out;
    }

    public Coords getInCoord() {
        return inCoord;
    }

    public Coords getOutCoord() {
        return outCoord;
    }
    

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public void setSentido(Sentido sentido) {
        this.sentido = sentido;
    }

    public void setIn(Mapa in) {
        this.in = in;
    }

    public void setOut(Mapa out) {
        this.out = out;
    }

    public void setInCoord(Coords inCoord) {
        this.inCoord = inCoord;
    }

    public void setOutCoord(Coords outCoord) {
        this.outCoord = outCoord;
    }
    
    
    @Override
    public String toString() {
        return (getIn().getNomeMapa() + " - " + getOut().getNomeMapa() + " - " + getTipo().toString() + " - " +getSentido().toString());
    }
    

}

