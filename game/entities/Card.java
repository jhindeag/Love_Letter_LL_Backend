package entities;

public class Card {
    private String name;
    private int val;
    private int targetCount = 0;
    private boolean canTargetSelf = false;
    private boolean canGuess = false;
    private String img = "path/to/image";

    public Card(String name) {
        this.name = name;
        setupCard(name);
    }

    private void setupCard(String name) {
        switch (name) {
            case "Assassin" -> {
                val = 0;
            }
            case "Jester" -> {
                val = 0;
                targetCount = 1;
            }
            case "Guard" -> {
                val = 1;
                targetCount = 1;
                canGuess = true;
            }
            case "Priest" -> {
                val = 2;
                targetCount = 1;
            }
            case "Cardinal" -> {
                val = 2;
                targetCount = 2;
                canTargetSelf = true;
            }
            case "Baron" -> {
                val = 3;
                targetCount = 1;
            }
            case "Baroness" -> {
                val = 3;
                targetCount = 2;
            }
            case "Handmaid" -> {
                val = 4;
            }
            case "Sycophant" -> {
                val = 4;
                targetCount = 1;
                canTargetSelf = true;
            }
            case "Prince" -> {
                val = 5;
                targetCount = 1;
                canTargetSelf = true;
            }
            case "Count" -> {
                val = 5;
            }
            case "King" -> {
                val = 6;
                targetCount = 1;
            }
            case "Constable" -> {
                val = 6;
            }
            case "Countess" -> {
                val = 7;
            }
            case "Queen" -> {
                val = 7;
                targetCount = 1;
            }
            case "Princess" -> {
                val = 8;
            }
            case "Bishop" -> {
                val = 9;
                targetCount = 1;
                canGuess = true;
            }
            default -> {
                // throw new AssertionError("Illegal card name: " + name);
            }
        }
    }

    public static void main(String[] args) {

    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getVal() {
        return val;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public boolean isCanTargetSelf() {
        return canTargetSelf;
    }

    public boolean isCanGuess() {
        return canGuess;
    }
}
