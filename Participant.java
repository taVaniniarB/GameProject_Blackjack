import java.util.Random;

public class Participant {
    private Card[] deck;
    private int score;
    private int money;
    public Participant(int length, int score, int money){
        deck = new Card[length];
        this.score = score;
        this.money = money;
    }
    public int getMoney() {return money;}
    public void setMoney(int money) {this.money = money;}
    public int getScore() {
        return score;
    }

    public void setScore(int score) {this.score = score;}

    public Card[] getDeck() { return deck; }
    public int getCardCount(){
        return countCard();
    }
    public void addCard(Card[][] cards){
        Random r = new Random();
        int suitNum;
        int rankNum;
        while (true)
        {
            suitNum = r.nextInt(4);
            rankNum = r.nextInt(13);
            if (cards[suitNum][rankNum] == null){
                continue;
            }
            break;
        }
        deck[countCard()] = new Card(suitNum, rankNum);
        cards[suitNum][rankNum] = null;
        score += deck[countCard()-1].score;
        if (score > 21){
            score = scoreCal();
        }
    }
    public void clearDeck() {
        for (int i = 0; i < deck.length; i++) {
            deck[i] = null;
        }
    }
    private int scoreCal(){
        int point = 0;
        for (int i = 0; i < countCard(); i++) {
            if (deck[i].rank.equals("A")){
                deck[i].setScore(1);
            }
        }
        for (int i = 0; i < countCard(); i++){
            point += this.deck[i].score;
        }
        return point;
    }
    private int countCard (){
        int numberOfCard = 0;
        for (int i = 0; i < deck.length; i++){
            if (deck[i] != null){
                numberOfCard++;
            }
            else {
                return numberOfCard;
            }
        }
        return numberOfCard;
    }
}

