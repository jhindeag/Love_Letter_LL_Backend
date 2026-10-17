package entities;

import java.util.ArrayList;

public class Player {
    private String name;
    private boolean isAlive = true;
    private ArrayList<Card> hand = new ArrayList<>();
    private ArrayList<Card> discardPile = new ArrayList<>();
    private boolean isProtected = false;
    private int hasPrince = 0;
    private int hasKing = 0;
    private int hasAssassin = 0;
    private int hasCount = 0;
    private int hasConstable = 0;
    private int winningTokenCount = 0;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Card> getHand() {
        if (!isAlive) {
            hand.set(0, new Card("KNOCKED OUT!"));
        }
        return this.hand;
    }

    public void draw(Card card) {
        this.hand.add(card);
        switch (card.getName()) {
            case "Assassin" -> {
                hasAssassin += 1;
            }
            case "Prince" -> {
                hasPrince += 1;
            }
            case "King" -> {
                hasKing += 1;
            }
        }

    }

    public void discard() {
        switch (hand.get(0).getName()) {
            case "Assassin" -> {
                hasAssassin--;
            }
            case "Prince" -> {
                hasPrince--;
            }
            case "Count" -> {
                hasCount++;
            }
            case "King" -> {
                hasKing--;
            }
            case "Constable" -> {
                hasConstable++;
            }
            case "Princess" -> {
                isAlive = false;
            }
            default -> {
            }
        }
        hand.clear();
    }

    public boolean isIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public void setIsProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    public int getHasPrince() {
        return hasPrince;
    }

    public int getHasKing() {
        return hasKing;
    }

    public int getHasAssassin() {
        return hasAssassin;
    }

    public int getHasConstable() {
        return hasConstable;
    }

    public void earnWinningToken() {
        System.out.println("Player " + name + " has earned a winning token!");
        winningTokenCount++;
    }

    public boolean isIsProtected() {
        return isProtected;
    }
}
