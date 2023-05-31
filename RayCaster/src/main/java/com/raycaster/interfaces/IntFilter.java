/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raycaster.interfaces;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * Classe que cria um filtro que permite apenas a entrada de inteiros
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class IntFilter extends DocumentFilter{
    
    /**
     * Metodo que verifica se a string inserida é um inteiro
     * @param fb
     * @param offset
     * @param string
     * @param attr
     * @throws BadLocationException 
     */
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (test(sb.toString())) 
            super.insertString(fb, offset, string, attr);
    }

    /**
     * Metodo que testa se a String inserida é composta de inteiros ou não
     * @param text
     * @return true se a Strig for composta de numeros inteiros e false caso contrario
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    private boolean test(String text) {
        try {
            Integer.valueOf(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Metodo que sobrescreve alguma parte do documento
     * @param fb
     * @param offset
     * @param length
     * @param text
     * @param attrs
     * @throws BadLocationException 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        if (test(sb.toString())) 
            super.replace(fb, offset, length, text, attrs);

    }
    
    
    /**
     * Metodo responsavel por apagar uma caractere
     * @param fb
     * @param offset
     * @param length
     * @throws BadLocationException 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if (test(sb.toString())) 
            super.remove(fb, offset, length);

   }
}

