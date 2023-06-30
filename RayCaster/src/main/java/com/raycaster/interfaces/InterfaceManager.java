package com.raycaster.interfaces;

import java.util.Stack;

/**
 *
 * @author vinicius
 */
public class InterfaceManager {
    private static Stack<Painel> pilha;
    
    static {
        pilha = new Stack<>();
    }
    
    public static void push(Painel novoPainel) {
        if(!pilha.isEmpty()) {
            pilha.peek().sair();
        }
        
        pilha.push(novoPainel);
        novoPainel.entrar();
    }
    
    public static void pop() {
        if(!pilha.isEmpty()) {
            pilha.peek().sair();
            pilha.pop();
        }
        
        if(!pilha.isEmpty()) {
            pilha.peek().entrar();
        }
    }
    
    public static Painel peek() {
        return pilha.peek();
    }
    
    public static void clear() {
        pilha.clear();
    }
}
