package au.edu.federation.unogame;

import java.util.ArrayList;

public class Player implements PlayerAction<UnoCard> { //interface included all player action needed

    private ArrayList<UnoCard> playerDeck;//create an arraylist for cards

    private String playerName;

    //constructor
    public Player(String playerName) {
        playerDeck = new ArrayList<UnoCard>();//each player will have their own card
        this.playerName = playerName;
    }

    //return player name
    public String getPlayerName() {
        return playerName;
    }

    //return number of card which player hold
    public int numberOfCards() {
        return playerDeck.size();
    }

    //return a list of card which player hold
    public ArrayList<UnoCard> playerCards() {
        return playerDeck;
    }

    //making sure the player can play a valid card, including computer and user.
    @Override
    public boolean comparingCard (UnoCard card1, UnoCard card2) {
        if(!card1.getCardColour().equals(card2.getCardColour())
                && card1.getCardNumber() != card2.getCardNumber()) {
            return false;
        }
        else {
            return true;
        }
    }

    //player can pick up any card from the deck
    @Override
    public void pickUpCard(UnoCard unoCard) {
        playerDeck.add(unoCard);
    }

    //this is for computer side, they have to pick up the card from a specific index of the arraylist
    public UnoCard getSpecificCard(int i) {
        return playerDeck.remove(i);
    }

    //player can play any card
    @Override
    public boolean playCard(UnoCard card) {
        return playerDeck.remove(card);
    }

    //if player don't hold any card, they will get instant win.
    public boolean playerWon() {
        if(playerDeck.size() == 0) {
            return true;
        }
        return false;
    }
}
