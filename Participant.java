import java.util.Random;

public class Participant {
    public Card[] Deck;
    public int Score;
    private String HS;

    public Participant(int length, int score, String hs) {
        Deck = new Card[length];
        Score = score;
        HS = hs;
    }

    public int getScore() {
        return Score;
    }

    public String getHS() {
        return HS;
    }

    public void setHS(String HS) {
        this.HS = HS;
    }

    public int getCardCount() {
        return countCard();
    }

    public void addCard(Card[][] cards) {
        Random r = new Random();
        int suitNum;
        int rankNum;

        while (true) {
            suitNum = r.nextInt(4);
            rankNum = r.nextInt(13);

            if (cards[suitNum][rankNum] == null) {
                continue;
            }

            break;
        }

        Deck[countCard()] = new Card(suitNum, rankNum);
        cards[suitNum][rankNum] = null;

        Score += Deck[countCard() - 1].score;

        if (Score > 21) {
            Score = scoreCal();
        }
    }

    private int scoreCal() {
        int point = 0;

        for (int i = 0; i < countCard(); i++) {
            if (Deck[i].rank.equals("A")) {
                Deck[i].setScore(1);
            }
        }

        for (int i = 0; i < countCard(); i++) {
            point += this.Deck[i].score;
        }

        return point;
    }

    private int countCard() {
        int numberOfCard = 0;

        for (int i = 0; i < Deck.length; i++) {
            if (Deck[i] != null) {
                numberOfCard++;
            } else {
                return numberOfCard;
            }
        }

        return numberOfCard;
    }
}