package au.edu.federation.unogame;

public class UnoCard{

    private int cardNumber;
    private String cardColour;

    //constructor
    public UnoCard(int cardNumber, String cardColour) {
        this.cardNumber = cardNumber;
        this.cardColour = cardColour;
    }
    //return card value
    public int getCardNumber() {
        return this.cardNumber;
    }

    //return card Number
    public String getCardColour() {
        return cardColour;
    }

    //display full name of the card
    @Override
    public String toString() {
        return getCardColour() + " " + getCardNumber();
    }
}