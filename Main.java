import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Card[][] cards = new Card[4][13];

        for (int i = 0; i < cards.length; i++) {
            for (int j = 0; j < cards[i].length; j++) {
                cards[i][j] = new Card(i, j);
            }
        }

        Participant player = new Participant(10, 0, "");
        Participant dealer = new Participant(10, 0, "h");

        for (int i = 0; i < 2; i++) {
            player.addCard(cards);
            dealer.addCard(cards);
        }

        while (true) {
            System.out.println("-------------Jack's BlackJack Game -------------");

            System.out.print("# Dealer: ");
            for (int i = 0; i < dealer.getCardCount() - 1; i++) {
                System.out.print(dealer.Deck[i].rank + "(" + dealer.Deck[i].suit + ") ");
            }
            System.out.println("XX");

            System.out.print("# Player: ");
            for (int i = 0; i < player.getCardCount(); i++) {
                System.out.print(player.Deck[i].rank + "(" + player.Deck[i].suit + ") ");
            }

            System.out.println("\n-----------------------------------------------");

            playerAction(player);

            if (player.getHS().equals("h")) {
                player.addCard(cards);

                if (player.getScore() > 21) {
                    System.out.println("Player busted...");
                    gameOver(dealer, player);
                    break;
                }

                if (player.getScore() == 21) {
                    System.out.println("Player win...");
                    gameOver(dealer, player);
                    break;
                }

                if (dealer.getScore() < 17) {
                    dealer.addCard(cards);

                    if (dealer.getScore() > 21) {
                        System.out.println("Dealer busted...");
                        gameOver(dealer, player);
                        break;
                    }
                } else if (dealer.getScore() >= 17) {
                    dealer.setHS("s");
                }

                continue;
            }

            if (player.getHS().equals("s")) {
                while (dealer.getHS().equals("h")) {
                    if (dealer.getScore() >= 17) {
                        dealer.setHS("s");
                        break;
                    }

                    dealer.addCard(cards);

                    if (dealer.getScore() > 21) {
                        System.out.println("Dealer busted...");
                        gameOver(dealer, player);
                        break;
                    }
                }

                if (dealer.getHS().equals("s")) {
                    if (21 - player.getScore() < 21 - dealer.getScore()) {
                        System.out.println("Player Wins...");
                    } else if (player.getScore() == dealer.getScore()) {
                        System.out.println("Equal points...");
                    } else {
                        System.out.println("Dealer Wins...");
                    }

                    gameOver(dealer, player);
                    break;
                }
            }

            break;
        }
    }

    public static void gameOver(Participant dealer, Participant player) {
        System.out.println("\n-------------Jack's BlackJack Game -------------");

        System.out.print("# Dealer: ");
        for (int i = 0; i < dealer.getCardCount(); i++) {
            System.out.print(dealer.Deck[i].rank + "(" + dealer.Deck[i].suit + ") ");
        }

        System.out.print("\n# Player: ");
        for (int i = 0; i < player.getCardCount(); i++) {
            System.out.print(player.Deck[i].rank + "(" + player.Deck[i].suit + ") ");
        }

        System.out.println("\n-----------------------------------------------");
    }

    public static void playerAction(Participant player) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Hit or Stand? (H/S): ");
        String action = scanner.nextLine();

        while (!action.equals("h") && !action.equals("s")) {
            System.out.print("Invalid action. Hit or Stand? (H/S): ");
            action = scanner.nextLine();
        }

        player.setHS(action);
    }
}