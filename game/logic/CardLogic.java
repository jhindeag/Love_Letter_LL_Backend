package logic;

import entities.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CardLogic {
    private ArrayList<Player> jesterPair;
    private Player currPlayer;
    private Player sycophantForced;
    private CardPile cardPile;
    private int chosenNumber;
    private Scanner inputReader;

    public CardLogic(ArrayList<Player> jesterPair, Player currPlayer, Player sycophantForced, CardPile cardPile,
            int chosenNumber, Scanner inputReader) {
        this.jesterPair = jesterPair;
        this.currPlayer = currPlayer;
        this.sycophantForced = sycophantForced;
        this.cardPile = cardPile;
        this.chosenNumber = chosenNumber;
        this.inputReader = inputReader;
    }

    // Cards logic
    public void betOn(Player chosenPlayer1) {
        jesterPair.add(currPlayer);
        jesterPair.add(chosenPlayer1);
    }

    public void eliminate(Player chosenPlayer1, int chosenNumber) {
        if (chosenPlayer1.getHasAssassin() > 0) {
            knockOut(currPlayer);
        } else if (chosenNumber == chosenPlayer1.getHand().get(0).getVal()) {
            knockOut(chosenPlayer1);
        }
    }

    public void peek(Player chosenPlayer1, Player chosenPlayer2) {
        if (chosenPlayer2 == null) {
            chosenPlayer2 = new Player("");
        }
        System.out.println("Player 1 has: " + chosenPlayer1.getHand().get(0).getName());
        System.out.println("Player 2 has: " + chosenPlayer2.getHand().get(0).getName());
    }

    public void swap(Player chosenPlayer1, Player chosenPlayer2) {
        Card temp = chosenPlayer1.getHand().get(0);
        chosenPlayer1.getHand().set(0, chosenPlayer2.getHand().get(0));
        chosenPlayer2.getHand().set(0, temp);
    }

    public void compare(Player chosenPlayer1) {
        if (currPlayer.getHand().get(0).getVal() > chosenPlayer1.getHand().get(0).getVal()) {
            knockOut(chosenPlayer1);
        } else if (chosenPlayer1.getHand().get(0).getVal() > currPlayer.getHand().get(0).getVal()) {
            knockOut(currPlayer);
        }
    }

    public void protect() {
        currPlayer.setIsProtected(true);
    }

    public void force(Player chosenPlayer1) {
        sycophantForced = chosenPlayer1;
    }

    public void discard(Player chosenPlayer1) {
        chosenPlayer1.discard();
        if (chosenPlayer1.isIsAlive()) {
            chosenPlayer1.draw(cardPile.pop());
        }
    }

    public void compareRev(Player chosenPlayer1) {
        if (currPlayer.getHand().get(0).getVal() > chosenPlayer1.getHand().get(0).getVal()) {
            knockOut(currPlayer);
        } else if (chosenPlayer1.getHand().get(0).getVal() > currPlayer.getHand().get(0).getVal()) {
            knockOut(chosenPlayer1);
        }
    }

    public void guess(Player chosenPlayer1) {
        if (chosenNumber == chosenPlayer1.getHand().get(0).getVal()) {
            currPlayer.earnWinningToken();
        }
        System.out.println("Do player " + chosenPlayer1.getName() + " want to discard?");
        String discard = inputReader.next();
        if (discard.toUpperCase() == "Y") {
            chosenPlayer1.discard();
            chosenPlayer1.draw(cardPile.pop());
        }
    }

    public void knockOut(Player currPlayer) {
        System.out.println(currPlayer.getName() + " has been knocked out!");
        if (currPlayer.getHasConstable() > 0) {
            currPlayer.earnWinningToken();
        }
        currPlayer.setIsAlive(false);
    }
}
