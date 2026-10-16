
import java.util.Collections;
import java.util.Stack;

public class CardPile {
    private Stack<Card> cardList = new Stack<>();

    public CardPile() {
        // add all cards to the pile
        for (int i = 0; i < 8; i++) {
            this.cardList.add(new Card("Guard"));
        }
        for (int i = 0; i < 2; i++) {
            this.cardList.add(new Card("Priest"));
            this.cardList.add(new Card("Cardinal"));
            this.cardList.add(new Card("Baron"));
            this.cardList.add(new Card("Baroness"));
            this.cardList.add(new Card("Handmaid"));
            this.cardList.add(new Card("Sycophant"));
            this.cardList.add(new Card("Prince"));
            this.cardList.add(new Card("Count"));
        }
        this.cardList.add(new Card("Jester"));
        this.cardList.add(new Card("Assassin"));
        this.cardList.add(new Card("King"));
        this.cardList.add(new Card("Constable"));
        this.cardList.add(new Card("Countess"));
        this.cardList.add(new Card("Queen"));
        this.cardList.add(new Card("Princess"));
        this.cardList.add(new Card("Bishop"));
        // shuffle
        Collections.shuffle(cardList);
        // READY
    }

    public Card pop() {
        return this.cardList.pop();
    }

    public String toString() {
        String s = "[";
        for (Card card : this.cardList) {
            s = s.concat(card.getName() + ", ");
        }
        s += "]";
        return s;
    }

    public static void main(String[] args) {
        CardPile cardPile = new CardPile();
        System.out.println("Card pile = " + cardPile.toString());
    }
}
