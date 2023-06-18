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
     * @param fb Filtro de string utilizado
     * @param offset Offset para verificação
     * @param string String para verificação
     * @param attr Atributo para verificação
     * @throws BadLocationException Exception lançada pelo uso do método
     */
    @Override
    public void insertString(FilterBypass fb, int offset, String string, 
            AttributeSet attr) throws BadLocationException 
    {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (test(sb.toString())) 
            super.insertString(fb, offset, string, attr);
    }

    /**
     * Metodo que testa se a String inserida é composta de inteiros ou não
     * @param text Texto inserido pelo usuário
     * @return true se a Strig for composta de numeros inteiros e false caso contrario
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
     * @param fb Filtro utilizado no campo
     * @param offset Offset utilizado na verificação
     * @param length Tamanho do texto utilizado na verificação
     * @param text Texto para ser analisado
     * @param attrs Atributos necessários para verificação
     * @throws BadLocationException Exception lançado pelo uso do método
     */
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, 
            AttributeSet attrs) throws BadLocationException 
    {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        if (test(sb.toString())) 
            super.replace(fb, offset, length, text, attrs);

    }
    
    
    /**
     * Metodo responsavel por apagar uma caractere
     * @param fb Filtro utilizado no campo
     * @param offset Offset utilizado na verificação
     * @param length Tamanho do texto utilizado na verificação
     * @throws BadLocationException Exception lançado pelo uso do método
     */
    @Override
    public void remove(FilterBypass fb, int offset, int length) 
            throws BadLocationException 
    {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if (test(sb.toString())) 
            super.remove(fb, offset, length);

   }
}

