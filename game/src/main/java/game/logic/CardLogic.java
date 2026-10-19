package logic;

import entities.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CardLogic {
    private ArrayList<Player> jesterPair;
    private CardPile cardPile;
    private Scanner inputReader;
    private Player sycophantForced;
    private int alivePlayerCount;

    public CardLogic(ArrayList<Player> jesterPair, CardPile cardPile, Scanner inputReader, int alivePlayerCount) {
        this.jesterPair = jesterPair;
        this.cardPile = cardPile;
        this.inputReader = inputReader;
        this.alivePlayerCount = alivePlayerCount;
    }

    // Cards logic
    public void betOn(Player currPlayer, Player chosenPlayer1) {
        jesterPair.add(currPlayer);
        jesterPair.add(chosenPlayer1);
    }

    public void eliminate(Player currPlayer, Player chosenPlayer1, int chosenNumber) {
        if (chosenPlayer1.getHasAssassin() > 0) {
            knockOut(currPlayer);
            discard(chosenPlayer1);
        } else if (chosenNumber == chosenPlayer1.getHand().get(0).getVal()) {
            knockOut(chosenPlayer1);
        }
    }

    public void peek(Player chosenPlayer1, Player chosenPlayer2) {
        System.out.println("Player 1 has: " + chosenPlayer1.getHand().get(0).getName());
        if (chosenPlayer2 != null) {
            System.out.println("Player 2 has: " + chosenPlayer2.getHand().get(0).getName());
        }
    }

    public void swap(Player chosenPlayer1, Player chosenPlayer2) {
        Card temp = chosenPlayer1.getHand().get(0);
        chosenPlayer1.getHand().set(0, chosenPlayer2.getHand().get(0));
        chosenPlayer2.getHand().set(0, temp);
        chosenPlayer1.syncHand();
        chosenPlayer2.syncHand();

    }

    public void compare(Player currPlayer, Player chosenPlayer1) {
        if (currPlayer.getHand().get(0).getVal() > chosenPlayer1.getHand().get(0).getVal()) {
            knockOut(chosenPlayer1);
        } else if (chosenPlayer1.getHand().get(0).getVal() > currPlayer.getHand().get(0).getVal()) {
            knockOut(currPlayer);
        }
    }

    public void protect(Player currPlayer) {
        currPlayer.setIsProtected(true);
    }

    public void force(Player chosenPlayer1) {
        this.sycophantForced = chosenPlayer1;
    }

    public void discard(Player chosenPlayer1) {
        chosenPlayer1.discard();
        if (chosenPlayer1.isIsAlive()) {
            chosenPlayer1.draw(cardPile.pop());
        }
    }

    public void compareRev(Player currPlayer, Player chosenPlayer1) {
        if (currPlayer.getHand().get(0).getVal() > chosenPlayer1.getHand().get(0).getVal()) {
            knockOut(currPlayer);
        } else if (chosenPlayer1.getHand().get(0).getVal() > currPlayer.getHand().get(0).getVal()) {
            knockOut(chosenPlayer1);
        }
    }

    public void guess(Player currPlayer, Player chosenPlayer1, int chosenNumber) {
        if (chosenNumber == chosenPlayer1.getHand().get(0).getVal()) {
            currPlayer.earnWinningToken();
        }
        System.out.println("Do player " + chosenPlayer1.getName() + " want to discard?");
        String discard = inputReader.next();
        if (discard.toUpperCase() == "Y") {
            discard(chosenPlayer1);
        }
    }

    public void knockOut(Player player) {
        player.knockedOut();
        alivePlayerCount--;
    }

    public int getAlivePlayerCount() {
        return alivePlayerCount;
    }
}
