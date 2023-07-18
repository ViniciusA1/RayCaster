package com.raycaster.interfaces.componentes;

import com.raycaster.utils.ArquivoUtils;
import com.raycaster.utils.Diretorio;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

/**
 *
 * @author vinicius
 */
public class CheckBox extends JCheckBox {
    
    private static final ImageIcon checkIcone;
    private static final ImageIcon uncheckIcone;
    
    private ImageIcon iconeAtual;
    
    static {
        checkIcone = ArquivoUtils.leImagem(Diretorio.ICONES + "check");
        uncheckIcone = ArquivoUtils.leImagem(Diretorio.ICONES + "uncheck");
    }
    
    public CheckBox() {
        setFocusable(false);
        setOpaque(false);
        setBorderPainted(false);
    }
    
    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        
        iconeAtual = isEnabled ? checkIcone : uncheckIcone;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        g.drawImage(iconeAtual.getImage(), 0, 0, 
                getWidth(), getHeight(), null);
    }
}
