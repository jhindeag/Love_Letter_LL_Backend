import entities.*;
import java.util.ArrayList;
import logic.GameManager;

public class GameInstance {
    private ArrayList<Player> playerList = new ArrayList<>();
    private GameManager gameManager;

    public GameInstance(String[] playerList) {
        for (String name : playerList) {
            this.playerList.add(new Player(name));
        }
        gameManager = new GameManager(this.playerList);
    }

    public static void main(String[] args) {
        String[] playerList = { "A", "B", "C" };
        GameInstance newGame = new GameInstance(playerList);
    }
}
