package logic;

import entities.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GameManager {
    private ArrayList<Player> playerList;
    private CardLogic cardLogic;
    private CardPile cardPile;
    private Player currPlayer;
    private int currPlayerIndex;
    private int playerCount;
    private final Scanner inputReader = new Scanner(System.in);
    private int alivePlayerCount;
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
        alivePlayerCount = playerCount;
        currPlayerIndex = (int) Math.floor(Math.random() * this.playerList.size());
        currPlayer = this.playerList.get(currPlayerIndex);
        cardPile = new CardPile();
        cardLogic = new CardLogic(jesterPair, cardPile, inputReader, alivePlayerCount);
        gameStart();
    }

    private void gameStart() {
        alivePlayerCount = playerCount;
        // each player gets a card
        for (int i = 0; i < playerCount; i++) {
            playerList.get(i).draw(cardPile.pop());
        }
        while (!isEndGame()) {
            nextRound();
        }
        calculateWinners();
    }

    public void nextRound() {
        System.out.println("----------------");
        System.out.println("Current player is: " + currPlayer.getName());
        currPlayer.setIsProtected(false);
        currPlayer.draw(cardPile.pop());
        showHands();
        System.out.println("Hand of player is: " + currPlayer.getHand().toString());
        do {
            System.out.print("Enter card index to play: ");
            chosenCardIndex = inputReader.nextInt();
        } while (chosenCardIndex != 0 && chosenCardIndex != 1);
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
        playCard();
        // next player turn
        do {
            currPlayerIndex = (currPlayerIndex + 1) % playerCount;
            // System.out.println("curr player index = " + currPlayerIndex);
            currPlayer = playerList.get(currPlayerIndex);
        } while (!currPlayer.isIsAlive());
    }

    public void playCard() {
        if (chosenPlayer1 == null) {
            chosenPlayer1 = new Player("");
        }
        if (chosenPlayer2 == null) {
            chosenPlayer2 = new Player("");
        }
        System.out.println(currPlayer.getName() + " played card " + playedCard.getName() + " on "
                + chosenPlayer1.getName() + " and " + chosenPlayer2.getName() + " guessed " + chosenNumber);
        // what to do based on the card
        switch (playedCard.getName()) {
            case "Jester" -> {
                cardLogic.betOn(currPlayer, chosenPlayer1);
            }
            case "Guard" -> {
                cardLogic.eliminate(currPlayer, chosenPlayer1, chosenNumber);
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
                cardLogic.compare(currPlayer, chosenPlayer1);
            }
            case "Baroness" -> {
                cardLogic.peek(chosenPlayer1, chosenPlayer2);
            }
            case "Handmaid" -> {
                cardLogic.protect(currPlayer);
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
                cardLogic.compareRev(currPlayer, chosenPlayer1);
            }
            case "Princess" -> {
                cardLogic.knockOut(currPlayer);
            }
            case "Bishop" -> {
                cardLogic.guess(currPlayer, chosenPlayer1, chosenNumber);
            }
            // the effects of other cards should be calculated within class Player
            default -> {
            }
        }
        alivePlayerCount = cardLogic.getAlivePlayerCount();
        this.playedCard = null;
        this.chosenPlayer1 = null;
        this.chosenPlayer2 = null;
        this.chosenNumber = -1;
    }

    public boolean isEndGame() {
        System.out.println("alive player = " + alivePlayerCount);
        return alivePlayerCount == 1 || cardPile.size() <= 1;
    }

    public void calculateWinners() {
        if (alivePlayerCount == 1) {
            for (Player player : playerList) {
                if (player.isIsAlive()) {
                    System.out.println("Player " + player.getName() + " is the winner!");
                    player.earnWinningToken();
                    System.out.println(
                            "Player " + player.getName() + " now has " + player.getWinningTokenCount() + " tokens.");
                }
            }
        } else {

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
