class Card {
    String suit, rank;
    int score;

    public Card(int inputSuit, int inputRank) {
        suit = convertSuit(inputSuit);
        rank = convertRank(inputRank);
        score = cardScore();
    }

    public void setScore(int score) {
        this.score = score;
    }

    private int cardScore() {
        switch (this.rank) {
            case "A":
                return 11;
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "10":
                return Integer.parseInt(this.rank);
            case "J":
            case "Q":
            case "K":
                return 10;
        }
        return 0;
    }

    private String convertSuit(int num) {
        switch (num) {
            case 0:
                return "♠";
            case 1:
                return "♣";
            case 2:
                return "♥";
            case 3:
                return "◆";
        }
        return null;
    }

    private String convertRank(int num) {
        switch (num) {
            case 0:
                return "A";
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return String.valueOf(num + 1);
            case 10:
                return "J";
            case 11:
                return "Q";
            case 12:
                return "K";
        }
        return null;
    }
}