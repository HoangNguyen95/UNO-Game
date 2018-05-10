package au.edu.federation.unogame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Deck{

    private ArrayList<UnoCard> deck;

    //constructor
    public Deck() {
        deck = new ArrayList<UnoCard>();

        //deck will contain 80 cards which include red, blue, green, yellow.
        String[] colours = {"Red", "Blue", "Green", "Yellow"};
        int[] numbers = {0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9};

        for(String colour : colours) {
            for (int number : numbers) {
                deck.add(new UnoCard(number, colour));//add totally 80 cards into a deck
            }
        }
    }
    //overload constructor to refill the deck if the deck have 0 card left. In case no player have won yet
    public Deck(ArrayList<UnoCard> refillDeck){
        deck = new ArrayList<UnoCard>();
        for (UnoCard card:refillDeck) {
            deck.add(card);
        }
    }

    //distribute card to each player
    public void distributedCard(Player p) {
        for (int i = 0; i < 7; i++) {
            p.pickUpCard(deck.remove(deck.size() - 1));
        }
    }

    //check if the deck is empty
    public boolean isEmpty() {
        if(deck.size() > 0) {
            return false;
        }
        return true;
    }

    //shuffle the deck when start the game, also shuffle when refill the deck
    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    //get the top card on the deck
    public UnoCard getTopCardOnPile() {
        return deck.remove(deck.size() - 1);
    }

    //return number of cards on the deck
    public int getDeck() {
        return deck.size();
    }
}
