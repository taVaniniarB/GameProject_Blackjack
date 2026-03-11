import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class BlackJack extends JFrame implements KeyListener {
    private GameHandler handler;
    private JTextArea textArea = new JTextArea();

    public BlackJack() {
        setTitle("Let's play BlackJack");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 20));
        addKeyListener(this);
        setFocusable(true);

        add(textArea);
        textArea.setEditable(false);
        setVisible(true);

        handler = new GameHandler(textArea);
        new Thread(new GameThread()).start();
    }

    public static void main(String[] args){
        new BlackJack();
    }
    public void restart() {
        handler.newGame();
        new Thread(new GameThread()).start();
    }

    class GameThread implements Runnable {
        @Override
        public void run() {
            // game loop
            while (!handler.isGameOver()) {
                handler.gameTiming();
                handler.gameLogic();
                handler.drawAll();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_1:
                handler.newGame();
                break;
            case KeyEvent.VK_2:
                handler.loadGame();
                break;
            case KeyEvent.VK_H:
            case KeyEvent.VK_S:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_ENTER:
                handler.handleKeyEvent(e);
                break;
            case KeyEvent.VK_Y: // Y key pressed
                if(handler.isGameOver())
                    restart();
            case KeyEvent.VK_N: // N key pressed
                if(handler.isGameOver())
                    handler.drawGameOver();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
