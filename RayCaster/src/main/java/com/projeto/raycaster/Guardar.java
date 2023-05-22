/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.projeto.raycaster;

import java.io.IOException;

/**
 *
 * @author bruno
 */
public interface Guardar {
    public void carregar();
    public void salvar() throws IOException;
    public void excluir();
}
