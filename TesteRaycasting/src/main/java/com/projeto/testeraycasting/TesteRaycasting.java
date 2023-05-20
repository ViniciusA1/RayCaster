package com.projeto.testeraycasting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class TesteRaycasting extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int MAP_WIDTH = 10;
    private static final int MAP_HEIGHT = 10;
    private static final int TILE_SIZE = 64;
    private Timer gameTimer;
    
    private static final int[][] MAP = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    private double playerX = 2.5;
    private double playerY = 2.5;
    private double playerAngle = 0;
    private double speed = 5;
    
    private int prevMouseX;

    // tentei fazer alguma coisa com hash mas ficou a mesma coisa
    private Set<Integer> keysPressed = new HashSet<>();

    public TesteRaycasting() {
        JFrame frame = new JFrame("Raycasting Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.add(this);
        
        this.setFocusable(true);
        this.requestFocus();
        
        
        // Adiciona os listeners das teclas
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keysPressed.add(e.getKeyCode());
                handlePlayerMovement();
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keysPressed.remove(e.getKeyCode());
            }
        });
        
       addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMovement(e);
            }
        });
        
        /*try {
            Robot robot = new Robot();
            robot.mouseMove(getLocationOnScreen().x + getWidth() / 2, getLocationOnScreen().y + getHeight() / 2);
        } catch (AWTException e) {
            e.printStackTrace();
        }*/
       
        
        frame.setVisible(true);
    }

    private void handlePlayerMovement() {
        if (keysPressed.contains(KeyEvent.VK_W)) {
            double deltaX = Math.cos(playerAngle) * speed * 0.016f;
            double deltaY = Math.sin(playerAngle) * speed * 0.016f;
            movePlayer(deltaX, deltaY);
        }
        if (keysPressed.contains(KeyEvent.VK_S)) {
            double deltaX = -Math.cos(playerAngle) * speed * 0.016f;
            double deltaY = -Math.sin(playerAngle) * speed * 0.016f;
            movePlayer(deltaX, deltaY);
        }
        if (keysPressed.contains(KeyEvent.VK_A)) {
            double deltaX = Math.cos(playerAngle - Math.PI / 2) * speed * 0.016f;
            double deltaY = Math.sin(playerAngle - Math.PI / 2) * speed * 0.016f;
            movePlayer(deltaX, deltaY);
        }
        if (keysPressed.contains(KeyEvent.VK_D)) {
            double deltaX = Math.cos(playerAngle + Math.PI / 2) * speed * 0.016f;
            double deltaY = Math.sin(playerAngle + Math.PI / 2) * speed * 0.016f;
            movePlayer(deltaX, deltaY);
        }
    }

    private void movePlayer(double deltaX, double deltaY) {
        double newPlayerX = playerX + deltaX;
        double newPlayerY = playerY + deltaY;

        if (!isWallCollision(newPlayerX, newPlayerY)) {
            playerX = newPlayerX;
            playerY = newPlayerY;
        }
    }
    
    
    private void handleMouseMovement(MouseEvent e) {
        int mouseX = e.getX();

        if (prevMouseX == 0) {
            // Primeira vez que o mouse é movido, não há alteração no ângulo
            prevMouseX = mouseX;
        } else {
            // Calcular a diferença no movimento do mouse
            int deltaX = mouseX - prevMouseX;

            // Atualizar o ângulo do jogador com base na diferença no movimento do mouse
            playerAngle += deltaX * 0.01;

            // Limitar o ângulo do jogador entre 0 e 2*PI
            if (playerAngle < 0) {
                playerAngle += 2 * Math.PI;
            } else if (playerAngle >= 2 * Math.PI) {
                playerAngle -= 2 * Math.PI;
            }

            // Atualizar as posições anteriores do mouse
            prevMouseX = mouseX;
        }
    }

    private boolean isWallCollision(double x, double y) {
        return MAP[(int) x][(int) y] == 1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int screenWidth = getWidth();
        int screenHeight = getHeight();

        // Drawing the walls
        for (int x = 0; x < screenWidth; x++) {
            double rayAngle = playerAngle - Math.toRadians(30) + (x / (double) screenWidth) * Math.toRadians(60);

            double distance = 0;
            boolean hitWall = false;

            double rayX = playerX;
            double rayY = playerY;

            double cosAngle = Math.cos(rayAngle);
            double sinAngle = Math.sin(rayAngle);

            while (!hitWall) {
                distance += 0.01;

                int mapX = (int) (rayX + distance * cosAngle);
                int mapY = (int) (rayY + distance * sinAngle);

                if (mapX < 0 || mapX >= MAP_WIDTH || mapY < 0 || mapY >= MAP_HEIGHT || MAP[mapX][mapY] == 1) {
                    hitWall = true;
                }
            }
            
            distance *= Math.cos(rayAngle - playerAngle);

            int ceilingHeight = (int) (screenHeight / 2.0 - screenHeight / distance);
            int floorHeight = screenHeight - ceilingHeight;

            g.setColor(Color.BLACK);
            g.drawLine(x, 0, x, ceilingHeight);
            g.setColor(Color.WHITE);
            g.drawLine(x, ceilingHeight, x, floorHeight);
            g.setColor(Color.BLACK);
            g.drawLine(x, floorHeight, x, screenHeight);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TesteRaycasting::new);
    }
}
