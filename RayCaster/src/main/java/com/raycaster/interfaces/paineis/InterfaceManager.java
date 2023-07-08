package com.raycaster.interfaces.paineis;

import com.raycaster.interfaces.componentes.BotaoCustom;
import java.awt.Font;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author vinicius
 */
public class InterfaceManager {

    private static Stack<Painel> pilha;

    static {
        pilha = new Stack<>();
    }

    public static void push(JFrame janela, Painel novoPainel) {
        Painel painelAnterior;

        if (!pilha.isEmpty()) {
            painelAnterior = pilha.peek();
            janela.remove(painelAnterior);
            painelAnterior.sairPush();
        }

        pilha.push(novoPainel);
        novoPainel.entrar();

        janela.add(novoPainel);
        janela.revalidate();
        janela.repaint();
    }

    public static void pop() {
        JFrame janela;
        Painel painel;

        if (pilha.isEmpty()) {
            return;
        }

        painel = pilha.peek();
        janela = (JFrame) SwingUtilities.getWindowAncestor(painel);

        janela.remove(painel);

        painel.sairPop();
        pilha.pop();

        if (!pilha.isEmpty()) {
            painel = pilha.peek();
            painel.entrar();
            janela.add(painel);
            janela.revalidate();
            janela.repaint();
        }
    }

    public static Painel peek() {
        return pilha.peek();
    }

    public static void clear() {
        pilha.clear();
    }
}
