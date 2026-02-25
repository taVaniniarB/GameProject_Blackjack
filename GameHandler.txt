import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.*;

public class GameHandler {

    Participant player = new Participant(10, 0, 100);
    Participant dealer = new Participant(10, 0, 10000);
    Card[][] cards = new Card[4][13];
    private int betMoney;
    private final int SCREEN_HEIGHT = 40;
    private final int SCREEN_WIDTH = 70;
    private final JTextArea textArea;
    private final char[][] buffer;
    private boolean isGameOver;
    public GameHandler(JTextArea ta){
        textArea = ta;
        buffer = new char[SCREEN_WIDTH][SCREEN_HEIGHT];
        initData();
    }
    public void initData(){
        isGameOver = false;
        initCard();
        betMoney = 0;
        player.clearDeck();
        player.setScore(0);
        dealer.clearDeck();
        dealer.setScore(0);
        clearBuffer();
        drawboard();
        drawToBuffer(4, 5, "1. START NEW GAME");
        try {
            BufferedReader in = new BufferedReader(new FileReader("lastgame.txt"));
            String str;
            str = in.readLine();
            if (str != null && !str.isEmpty())
                drawToBuffer(4, 7, "2. CONTINUE GAME");
            in.close();
        }catch (FileNotFoundException e){
        }catch (IOException e){
            e.printStackTrace();
        }
        drawToBuffer(8, 11, "Select : ");
    }
    public void gameTiming() {
        try
        {
            Thread.sleep(50);
        } catch
        (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    private void clearBuffer(){
        for (int y = 0; y < SCREEN_HEIGHT; y++){
            for (int x = 0; x < SCREEN_WIDTH; x++){
                buffer[x][y] = ' ';
            }
        }
    }
    private void clearboard(){
        for (int y = 4; y < 8; y++){
            for (int x = 0; x < SCREEN_WIDTH; x++){
                buffer[x][y] = ' ';
            }
        }
    }
    private void clearTextField(){
        for (int y = 9; y < SCREEN_HEIGHT; y++){
            for (int x = 0; x < SCREEN_WIDTH; x++){
                buffer[x][y] = ' ';
            }
        }
    }
    private void drawToBuffer(int px, int py, String c){
        for (int x = 0; x < c.length(); x++){
            int LEFT_PADDING = 1;
            buffer[px + x + LEFT_PADDING][py] = c.charAt(x);
        }
    }
    private void initCard(){
        for (int i = 0; i < cards.length; i++){
            for (int j = 0; j < cards[i].length; j++){
                cards[i][j] = new Card(i, j);
            }
        }
    }

    private void drawboard(){
        drawToBuffer(1, 3, "-------------Jack's BlackJack Game ---------------");
        drawToBuffer(1, 8, "--------------------------------------------------");
    }
    public void drawGameOver() {
        clearboard();
        clearTextField();
        drawToBuffer(1, 5, "You earned $" + String.valueOf(player.getMoney() - 100));
        render();
        BufferedWriter out;
        try{
            out = new BufferedWriter(new FileWriter("lastgame.txt"));
            out.write(String.valueOf(player.getMoney()));
            out.newLine();
            out.write(String.valueOf(dealer.getMoney()));
            out.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void gameOver(){
        clearboard();

        drawToBuffer(4, 6, "# Dealer($" + dealer.getMoney() + ")");
        drawToBuffer(4, 7, "# Player($" + player.getMoney() + ")");
        for (int i = 0; i < dealer.getCardCount(); i++) {
            drawToBuffer(22 + i*6, 6, dealer.getDeck()[i].rank + "(" + dealer.getDeck()[i].suit + ")");
        }
        for (int i = 0; i < player.getCardCount(); i++) {
            drawToBuffer(22+ i*6, 7,player.getDeck()[i].rank + "(" + player.getDeck()[i].suit + ")");
        }
        if (player.getMoney() == 0){
            clearTextField();
            drawToBuffer(8, 11, "You lost EVERYTHING! Quit Game!");
            try{
                BufferedWriter out = new BufferedWriter(new FileWriter("lastgame.txt"));
                out.write("");
                out.newLine();
                out.write("");
                out.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        else {drawToBuffer(8, 13, "Play Again? (Y/N):");}
        isGameOver = true;
        render();
    }
    public boolean isGameOver() {
        return isGameOver;
    }
    public void newGame() {
        initData();
        clearBuffer();
        drawboard();
        drawToBuffer(4, 6, "# Dealer($" + dealer.getMoney() + ")");
        drawToBuffer(4, 7, "# Player($" + player.getMoney() + ")");
        drawToBuffer(8, 11, "How much money do you want to bet?");
        int betMoney = 0;
        drawToBuffer(18, 13, "(Up/Down/Enter)");
        drawToBuffer(14, 13, String.valueOf(betMoney));    
    }
    public void loadGame(){
        try {
            BufferedReader in = new BufferedReader(new FileReader("lastgame.txt"));
            String str;

            str = in.readLine();
            if (str != null) {
                int playerMoney = Integer.parseInt(str);
                player.setMoney(playerMoney);
            }

            str = in.readLine();
            if (str != null) {
                int dealerMoney = Integer.parseInt(str);
                dealer.setMoney(dealerMoney);
            }

            in.close();
        }catch (IOException e){e.printStackTrace();}
        newGame();
    }
    public void handleKeyEvent(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                betMoney += 10;
                if (betMoney > player.getMoney() || betMoney > 100) {
                    betMoney -= 10;
                }
                drawToBuffer(14, 13, "   ");
                drawToBuffer(14, 13, String.valueOf(betMoney));
                break;
            case KeyEvent.VK_DOWN:
                betMoney -= 10;
                if (betMoney < 10) {
                    betMoney += 10;
                }
                drawToBuffer(14, 13, "   ");
                drawToBuffer(14, 13, String.valueOf(betMoney));
                break;
            case KeyEvent.VK_ENTER:
                player.setMoney(player.getMoney() - betMoney);
                drawToBuffer(14, 7, "      ");
                drawToBuffer(14, 7, player.getMoney() + ")");
                drawToBuffer(4, 4, "# Betting: " + String.valueOf(betMoney));
                gameStart();
                break;
            case KeyEvent.VK_H:
                player.addCard(cards);
                showDeck();
                gameLogic();
                if (dealer.getScore() < 17){
                    dealer.addCard(cards);
                    showDeck();
                }
                break;
            case KeyEvent.VK_S:
                gameLogic();
                while (dealer.getScore() < 17){
                    dealer.addCard(cards);
                    showDeck();
                    if (dealer.getScore() >= 17){
                        break;
                    }
                }
                if (21 - player.getScore() < 21 - dealer.getScore()) {
                    clearTextField();
                    drawToBuffer(8, 11, "Player Wins...");
                    dealer.setMoney(dealer.getMoney() - betMoney);
                    player.setMoney(player.getMoney() + 2 * betMoney);
                    gameOver();
                } else if (player.getScore() == dealer.getScore()) {
                    clearTextField();
                    player.setMoney(player.getMoney() + betMoney);
                    drawToBuffer(8, 11, "Equal points...");
                    gameOver();
                } else if((21 - player.getScore() > 21 - dealer.getScore())){
                    clearTextField();
                    dealer.setMoney(dealer.getMoney() + betMoney);
                    drawToBuffer(8, 11, "Dealer Wins...");
                    gameOver();
                }
        }
    }
    private void showDeck(){
        for (int i = 0; i < dealer.getCardCount() - 1; i++) {
            drawToBuffer(22 + i*6, 6, dealer.getDeck()[i].rank + "(" + dealer.getDeck()[i].suit + ")");
        }
        drawToBuffer((dealer.getCardCount()-1)*6 + 23, 6,"XX");
        for (int i = 0; i < player.getCardCount(); i++) {
            drawToBuffer(22+ i*6, 7,player.getDeck()[i].rank + "(" + player.getDeck()[i].suit + ")");
        }
    }
    public void gameStart() {
        for (int i = 0; i < 2; i++)
        {
            player.addCard(cards);
            dealer.addCard(cards);
        }
        showDeck();
        clearTextField();
        drawToBuffer(8, 11, "Hit or Stand? (H/S):");
    }

    public void gameLogic(){
        if (player.getScore() == 21 && dealer.getScore() == 21){
            clearTextField();
            player.setMoney(player.getMoney() + betMoney);
            drawToBuffer(8, 11, "Equal points...");
            gameOver();
        }
        if (player.getScore() == 21){
            clearTextField();
            dealer.setMoney(dealer.getMoney() - betMoney);
            player.setMoney(player.getMoney() + 2 * betMoney);
            drawToBuffer(8, 11, "Player Wins...");
            gameOver();
        }
        if (dealer.getScore() == 21){
            clearTextField();
            dealer.setMoney(dealer.getMoney() + betMoney);
            drawToBuffer(8, 11, "Dealer win...");
            gameOver();
        }
        if (dealer.getScore() > 21) {
            dealer.setMoney(dealer.getMoney() - betMoney);
            player.setMoney(player.getMoney() + 2 * betMoney);
            clearTextField();
            drawToBuffer(8, 11, "Dealer busted...");
            gameOver();
        }
        if (player.getScore() > 21){
            clearTextField();
            dealer.setMoney(dealer.getMoney() + betMoney);
            drawToBuffer(8, 11, "Player busted...");
            gameOver();
        }

    }
    public void drawAll(){

        render();
    }
    private void render() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < SCREEN_HEIGHT; y++) {
            for (int x = 0; x < SCREEN_WIDTH; x++) {
                sb.append(buffer[x][y]);
            }
            sb.append("\n");
        }
        textArea.setText(sb.toString());
    }
}