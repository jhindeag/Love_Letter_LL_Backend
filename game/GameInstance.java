import java.util.ArrayList;
import java.util.Scanner;

public class GameInstance {
    private ArrayList<Player> playerList = new ArrayList<>();
    private CardPile cardPile;
    private Player currPlayer;
    private int currPlayerIndex;
    private int playerCount;
    private Scanner inputReader = new Scanner(System.in);
    // private int alivePlayerCount;
    private int chosenCardIndex = -1;
    private Card playedCard = null;
    private Player chosenPlayer1 = null;
    private Player chosenPlayer2 = null;
    private int chosenNumber = -1;
    // private int validTargetCount;
    private Player[] winners;
    private Player sycophantForced;
    private ArrayList<Player> jesterPair;

    public GameInstance(String[] playerList) {
        for (String name : playerList) {
            this.playerList.add(new Player(name));
        }
        playerCount = this.playerList.size();
        currPlayerIndex = (int) Math.floor(Math.random() * this.playerList.size());
        currPlayer = this.playerList.get(currPlayerIndex);
        cardPile = new CardPile();
        deal();
        gameStart();
    }

    private void deal() {
        for (int i = 0; i < playerCount; i++) {
            playerList.get(i).draw(cardPile.pop());
        }
        playerList.get(currPlayerIndex).draw(cardPile.pop());
    }

    private void gameStart() {
        showHands();
        // game starts here
        System.out.println("Current player is: " + currPlayer.getName());
        System.out.println("Hand of player is: " + currPlayer.getHand().toString());
        System.out.print("Enter card index to play: ");
        chosenCardIndex = inputReader.nextInt();
        // Card stages:
        // has targets? t/f
        // how many targets? 1/2
        // can target self? t/f
        // can it guess? t/f
        System.out.println("Chosen card index: " + chosenCardIndex);
        playedCard = currPlayer.getHand().get(chosenCardIndex);
        // fill data to play a card
        if (playedCard.getTargetCount() > 0) {
            // must target at least 1 player
            System.out.print("Enter index of one player: ");
            chosenPlayer1 = playerList.get(inputReader.nextInt());
            // TODO: check if this card can target self

            // check if this card can guess
            if (playedCard.isCanGuess()) {
                System.out.print("Enter a number to guess: ");
                chosenNumber = inputReader.nextInt();
            }

            // check if this card can target 2 players
            if (playedCard.getTargetCount() == 2) {
                do {
                    System.out.print("Enter index of another player: ");
                    chosenPlayer2 = playerList.get(inputReader.nextInt());
                } while (chosenPlayer2 == chosenPlayer1);
            }

        }
        playCard(playedCard, chosenPlayer1, chosenPlayer2, chosenNumber);
        playedCard = null;
        chosenPlayer1 = null;
        chosenPlayer2 = null;
        chosenNumber = -1;
    }

    public void playCard(Card playedCard, Player chosenPlayer1, Player chosenPlayer2, int chosenNumber) {
        if (chosenPlayer1 == null) {
            chosenPlayer1 = new Player("");
        }
        if (chosenPlayer2 == null) {
            chosenPlayer2 = new Player("");
        }
        System.out.println(currPlayer.getName() + " playing card " + playedCard.getName() + " on "
                + chosenPlayer1.getName() + " and " + chosenPlayer2.getName() + " guessed " + chosenNumber);

        // what to do based on the card
        switch (playedCard.getName()) {
            case "Jester" -> {
                betOn(chosenPlayer1);
            }
            case "Guard" -> {
                eliminate(chosenPlayer1, chosenNumber);
            }
            case "Priest" -> {
                peek(chosenPlayer1, null);
            }
            case "Cardinal" -> {
                swap(chosenPlayer1, chosenPlayer2);
                System.out.print("Enter index of one player to peek: ");
                chosenPlayer1 = playerList.get(inputReader.nextInt());
                peek(chosenPlayer1, null);
            }
            case "Baron" -> {
                compare(chosenPlayer1);
            }
            case "Baroness" -> {
                peek(chosenPlayer1, chosenPlayer2);
            }
            case "Handmaid" -> {
                protect();
            }
            case "Sycophant" -> {
                force(chosenPlayer1);
            }
            case "Prince" -> {
                discard(chosenPlayer1);
            }
            case "King" -> {
                swap(currPlayer, chosenPlayer1);
            }
            case "Queen" -> {
                compareRev(chosenPlayer1);
            }
            case "Princess" -> {
                knockOut(currPlayer);
            }
            case "Bishop" -> {
                guess(chosenPlayer1);
            }
            // the effects of other cards should be calculated within class Player
            default -> {
            }
        }

    }

    // Cards logic
    private void betOn(Player chosenPlayer1) {
        jesterPair.add(currPlayer);
        jesterPair.add(chosenPlayer1);
    }

    private void eliminate(Player chosenPlayer1, int chosenNumber) {
        if (chosenNumber == chosenPlayer1.getHand().get(0).getVal()) {
            knockOut(chosenPlayer1);
        }
    }

    private void peek(Player chosenPlayer1, Player chosenPlayer2) {
        if (chosenPlayer2 == null) {
            chosenPlayer2 = new Player("");
        }
        System.out.println("Player 1 has: " + chosenPlayer1.getHand().get(0).getName());
        System.out.println("Player 2 has: " + chosenPlayer2.getHand().get(0).getName());
    }

    private void swap(Player chosenPlayer1, Player chosenPlayer2) {
        Card temp = chosenPlayer1.getHand().get(0);
        chosenPlayer1.getHand().set(0, chosenPlayer2.getHand().get(0));
        chosenPlayer2.getHand().set(0, temp);
    }

    private void compare(Player chosenPlayer1) {
        if (currPlayer.getHand().get(0).getVal() > chosenPlayer1.getHand().get(0).getVal()) {
            knockOut(chosenPlayer1);
        } else if (chosenPlayer1.getHand().get(0).getVal() > currPlayer.getHand().get(0).getVal()) {
            knockOut(currPlayer);
        }
    }

    private void protect() {
        currPlayer.setIsProtected(true);
    }

    private void force(Player chosenPlayer1) {
        sycophantForced = chosenPlayer1;
    }

    private void discard(Player chosenPlayer1) {
        chosenPlayer1.discard();
        if (chosenPlayer1.isIsAlive()) {
            chosenPlayer1.draw(cardPile.pop());
        }
    }

    private void compareRev(Player chosenPlayer1) {
        if (currPlayer.getHand().get(0).getVal() > chosenPlayer1.getHand().get(0).getVal()) {
            knockOut(currPlayer);
        } else if (chosenPlayer1.getHand().get(0).getVal() > currPlayer.getHand().get(0).getVal()) {
            knockOut(chosenPlayer1);
        }
    }

    private void knockOut(Player currPlayer1) {
        currPlayer1.setIsAlive(false);
    }

    public void showHands() {
        String s = "";
        for (Player player : this.playerList) {
            s += player.getName() + ": " + player.getHand().toString() + "\n";
        }
        System.out.println(s);
    }

    public static void main(String[] args) {
        String[] playerList = { "A", "B", "C" };
        GameInstance newGame = new GameInstance(playerList);
    }

    private void guess(Player chosenPlayer1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
