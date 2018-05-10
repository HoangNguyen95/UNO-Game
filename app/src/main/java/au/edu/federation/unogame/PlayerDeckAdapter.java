package au.edu.federation.unogame;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class PlayerDeckAdapter extends ArrayAdapter<UnoCard> {

    //keep track of context and list of player card
    private Context context;
    private ArrayList<UnoCard> playerCards;

    //constructor
    public PlayerDeckAdapter(Context context, ArrayList<UnoCard> playerCards) {
        //calling superclass constructor
        super(context, R.layout.activity_playuno, playerCards);
        //update these properties to keep track of them
        this.context = context;
        this.playerCards = playerCards;
    }

    //this method inflates each cell of the grid view then return the inflated view
    public View getView(int position, View convertView, ViewGroup parentViewGroup) {

        //create layout inflater
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get a string containing the card detail
        final String cardColour = playerCards.get(position).getCardColour();
        final String cardNumber = String.valueOf(playerCards.get(position).getCardNumber());

        //inflate a cell with the card layout
        View rowView = inflater.inflate(R.layout.card_layout, parentViewGroup, false);

        //get access to my new Square Text View
        SquareTextView squareTextView = (SquareTextView)rowView.findViewById(R.id.playerSelectedCard);

        //set the card number on square text view
        squareTextView.setText(cardNumber);

        //purpose is to change the cell colour depend on the card number it
        switch (cardColour) {
            case "Red": squareTextView.setBackgroundColor(Color.RED);
                break;
            case "Blue":
                squareTextView.setBackgroundColor(Color.BLUE);
                squareTextView.setTextColor(Color.WHITE);
                break;
            case "Yellow": squareTextView.setBackgroundColor(Color.YELLOW);
                break;
            case "Green": squareTextView.setBackgroundColor(Color.GREEN);
                break;
        }
        //return the inflated row with its contained square text view
        return rowView;
    }
}
