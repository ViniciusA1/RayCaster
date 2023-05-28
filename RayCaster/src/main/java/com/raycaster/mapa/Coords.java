/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raycaster.mapa;

/**
 *
 * @author bruno
 */
public class Coords {
    public enum Face {
        SUL, NORTE, LESTE, OESTE, CENTRO
    }
    public int x;
    public int y;
    public Face face;
    public Mapa map;

    public Coords(int x, int y, Face face, Mapa map) {
        this.x = x;
        this.y = y;
        this.face = face;
        this.map = map;
    }
}
