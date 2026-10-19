package logic;

import java.util.ArrayList;
import java.util.Scanner;
import entities.*;

public class GameManager {
    private ArrayList<Player> playerList;
    private CardLogic cardLogic;
    private CardPile cardPile;
    private Player currPlayer;
    private int currPlayerIndex;
    private int playerCount;
    private final Scanner inputReader = new Scanner(System.in);
    // private int alivePlayerCount;
    private int chosenCardIndex = -1;
    private Card playedCard = null;
    private Player chosenPlayer1 = null;
    private Player chosenPlayer2 = null;
    private int chosenNumber = -1;
    private int validTargetCount;
    private ArrayList<Player> winners = new ArrayList<>();
    private Player sycophantForced;
    private ArrayList<Player> jesterPair = new ArrayList<>();

    public GameManager(ArrayList<Player> playerList) {
        this.playerList = playerList;
        playerCount = this.playerList.size();
        currPlayerIndex = (int) Math.floor(Math.random() * this.playerList.size());
        currPlayer = this.playerList.get(currPlayerIndex);
        cardPile = new CardPile();
        cardLogic = new CardLogic(jesterPair, currPlayer, sycophantForced, cardPile, chosenNumber, inputReader);
        deal();
        gameStart();
    }

    private void deal() {
        for (int i = 0; i < playerCount; i++) {
            playerList.get(i).draw(cardPile.pop());
        }
    }

    private void gameStart() {
        // game starts here
        while (true) {
            System.out.println("----------------");
            System.out.println("Current player is: " + currPlayer.getName());
            currPlayer.draw(cardPile.pop());
            showHands();
            System.out.println("Hand of player is: " + currPlayer.getHand().toString());
            do {
                System.out.print("Enter card index to play: ");
                chosenCardIndex = inputReader.nextInt();
            } while (chosenCardIndex != 0 && chosenCardIndex != 1);
            // Card stages:
            // has targets? t/f
            // how many targets6? 1/2
            // can target self? t/f
            // can it guess? t/f
            playedCard = currPlayer.getHand().get(chosenCardIndex);
            while (playedCard.getName() == "Countess"
                    && (currPlayer.getHasKing() > 0 || currPlayer.getHasPrince() > 0)) {
                System.out.println("You have to play Prince or King!");
                System.out.print("Enter card index to play: ");
                chosenCardIndex = inputReader.nextInt();
                playedCard = currPlayer.getHand().get(chosenCardIndex);
            }
            // played card get removed from hand
            currPlayer.getHand().remove(chosenCardIndex);
            // fill data to play a card
            if (playedCard.getTargetCount() > 0) {
                // must target at least 1 player
                do {
                    System.out.print("Enter index of one player: ");
                    chosenPlayer1 = playerList.get(inputReader.nextInt());
                } while (!chosenPlayer1.isIsAlive() || chosenPlayer1.isIsProtected());
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
                    } while (chosenPlayer2 == chosenPlayer1 || !chosenPlayer1.isIsAlive()
                            || chosenPlayer1.isIsProtected());
                }

            }
            playCard(playedCard, chosenPlayer1, chosenPlayer2, chosenNumber);
            playedCard = null;
            chosenPlayer1 = null;
            chosenPlayer2 = null;
            chosenNumber = -1;
            // next player turn
            do {
                currPlayerIndex = (currPlayerIndex + 1) % playerCount;
                System.out.println("curr player index = " + currPlayerIndex);
                currPlayer = playerList.get(currPlayerIndex);
            } while (!currPlayer.isIsAlive());
        }
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
                cardLogic.betOn(chosenPlayer1);
            }
            case "Guard" -> {
                cardLogic.eliminate(chosenPlayer1, chosenNumber);
            }
            case "Priest" -> {
                cardLogic.peek(chosenPlayer1, null);
            }
            case "Cardinal" -> {
                cardLogic.swap(chosenPlayer1, chosenPlayer2);
                System.out.print("Enter index of one player to peek: ");
                chosenPlayer1 = playerList.get(inputReader.nextInt());
                cardLogic.peek(chosenPlayer1, null);
            }
            case "Baron" -> {
                cardLogic.compare(chosenPlayer1);
            }
            case "Baroness" -> {
                cardLogic.peek(chosenPlayer1, chosenPlayer2);
            }
            case "Handmaid" -> {
                cardLogic.protect();
            }
            case "Sycophant" -> {
                cardLogic.force(chosenPlayer1);
            }
            case "Prince" -> {
                cardLogic.discard(chosenPlayer1);
            }
            case "King" -> {
                cardLogic.swap(currPlayer, chosenPlayer1);
            }
            case "Queen" -> {
                cardLogic.compareRev(chosenPlayer1);
            }
            case "Princess" -> {
                cardLogic.knockOut(currPlayer);
            }
            case "Bishop" -> {
                cardLogic.guess(chosenPlayer1);
            }
            // the effects of other cards should be calculated within class Player
            default -> {
            }
        }

    }

    public void showHands() {
        String s = "";
        for (Player player : this.playerList) {
            s += player.getName() + ": " + player.getHand().toString() + "\n";
        }
        System.out.println(s);
    }
}
