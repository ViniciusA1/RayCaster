package com.raycaster.interfaces.paineis;

import com.raycaster.engine.Estado;
import com.raycaster.engine.sons.EfeitoSonoro;
import com.raycaster.engine.sons.SomManager;
import java.util.EnumSet;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author vinicius
 */
public class InterfaceManager {

    private static Stack<Painel> pilha;
    
    private final static EfeitoSonoro somInterface;

    static {
        pilha = new Stack<>();
        
        EnumSet<Estado> estados = EnumSet.noneOf(Estado.class);

        estados.add(Estado.USANDO);
        estados.add(Estado.TROCANDO);

        somInterface = new EfeitoSonoro("interface", estados);
        SomManager.addSom(somInterface);
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
    
    public static void playSom(Estado tipoSom) {
        somInterface.playSom(tipoSom);
    }
}
