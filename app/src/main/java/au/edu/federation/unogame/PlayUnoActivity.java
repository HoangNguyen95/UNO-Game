package au.edu.federation.unogame;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

public class PlayUnoActivity extends AppCompatActivity {

    private TextView totalCards, topCardOnPile, opponentCards, playerCards;
    private SquareTextView currentCardOnPile;
    GridView playerDeck;
    Button pickUpCard;

    private UnoCard currentCard, selectedCard, playerPickUpNewCard, computerPickNewCard, computerPlayNewCard;
    Deck deck;
    ArrayList<UnoCard> cardPile;//a list of card which have been played
    private Player player, computer;//player as user and player as computer
    private int pick = 0;
    Random random;

    PlayerDeckAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playuno);

        accessAllView();//access all the View for displaying UI

        prepareDeck();//preparing deck before game started

        updateCardUI();//Create UI first time

        //create my adapter so that  can pass the context and data to dispplay
        adapter = new PlayerDeckAdapter(this, player.playerCards());

        playerDeck.setAdapter(adapter);

        //setup onlclick for each data
        playerDeck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get the card which user pick
                selectedCard = player.playerCards().get(position);
                //notify the adapter that view has been changed
                adapter.notifyDataSetChanged();
                //start the game when user pick something from this view
                startTheGame();
            }
        });

        //setup onclick for player to pick up new card from the deck
        pickUpCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //before pickup the card, check if the deck is empty or not. If is empty, the deck will be refilled
                checkIfDeckEmpty();
                //get the top card on the deck pile
                playerPickUpNewCard = deck.getTopCardOnPile();
                //dialog will display when user pick up a card which can be play straight away, however, player can choose
                //to keep the card
                pickUpCardDialog(player, playerPickUpNewCard);
                //notify the adapter that view has been changed
                adapter.notifyDataSetChanged();
            }
        });
    }


    public void prepareDeck() {
        deck = new Deck();
        deck.shuffleDeck();
        cardPile = new ArrayList<UnoCard>();
        player = new Player("Player");
        computer = new Player("Computer");
        deck.distributedCard(player);
        deck.distributedCard(computer);

        //top card on the pile will be the top card from the deck
        currentCard = deck.getTopCardOnPile();
        cardPile.add(currentCard);//cardpile will add current card
    }



    public void startTheGame() {//start the game when user play a card
        userPlayUno(player, selectedCard);//user will always play first

        //ensure player hasn't won yet or player choose a valid card to play
        if (player.numberOfCards() > 0 && validCardChoice(player, selectedCard)) {
           computerTurn();//computer turn to play with a delay to improve user experience
        }
    }

    public void userPlayUno(Player p, UnoCard card) {
        //display toast message if player choose invalid card
        if(!p.comparingCard(card, currentCard)) {
            Toast.makeText(getApplicationContext(), "You can't play " + card.toString(), Toast.LENGTH_SHORT).show();
        }
        else {
            p.playCard(card);//user will play card first
            currentCard = card;//current card will become a card which user just played
            cardPile.add(currentCard);//add the new card to the card pile list
            updateCardUI();//update UI
        }
        gameOver(p);//check if player won yet
    }

    //computer behavior is different from user because it doesn't have option to choose from the list.
    //instead, a for loop will be used to select a card for computer so that computer can play it
    public void computerPlayUno(Player p) {

        //option for computer when computer pick card which can instantly play
        String [] array = {"Keep", "Play"};
        random = new Random();

        int select = random.nextInt(array.length);

        //for computer only, ensure computer deck have valid card to play
        if(!hasColourOrValue(p)) {//if computer have no card
            //before pickup the card, check if the deck is empty or not. If is empty, the deck will be refilled
            checkIfDeckEmpty();

            computerPickNewCard = deck.getTopCardOnPile();
            //if computer pick a card which can play straight away
            if(p.comparingCard(computerPickNewCard, currentCard)) {
                String computerChoice = array[select];
                if(computerChoice.equals("Play")) {
                    //if computer decide to play
                    currentCard = computerPickNewCard;
                    cardPile.add(currentCard);//add new card to the card pile
                    updateCardUI();//Update UI again
                }
                if(computerChoice.equals("Keep")) {
                    //if computer decide to keep it
                    p.pickUpCard(computerPickNewCard);
                    updateCardUI();
                }
            }

            else {
                //if computer pick up a card which can't play straight away
                p.pickUpCard(computerPickNewCard);
                updateCardUI();
            }
        }

        else {
            boolean playCard = false;//ensure computer hasn't play a card yet
            for (pick = 0; pick < p.playerCards().size() && !playCard; pick++) {
                if (p.comparingCard(p.playerCards().get(pick), currentCard)) {

                    computerPlayNewCard = p.getSpecificCard(pick);//get a specific card from arraylist
                    p.playCard(computerPlayNewCard);
                    currentCard = computerPlayNewCard;
                    cardPile.add(currentCard);//add current card to the card pile
                    updateCardUI();//update UI again
                    playCard = true;//stop the loop once computer have play new card
                }
            }
        }
        gameOver(p);//check if computer have won yet
    }

    public void gameOver(Player p) {//method to decide who won first
        if(p.playerWon()) {
            finish(p);
        }
    }

    //method to ensure deck always have card for player to pick during the game
    private void checkIfDeckEmpty() {
        if(deck.isEmpty()) {
            deck = new Deck(cardPile);//deck will automatically get all card from the card pile
            cardPile.clear();//clear the pile and start over again
            deck.shuffleDeck();//shuffle every time the deck get refilled
            //display friendly message to let user know that deck have been refiled
            Toast.makeText(getApplicationContext(), "Deck refilled", Toast.LENGTH_SHORT).show();
        }
    }

    //create a action dialog for user to choose either keep the card or play the card
    //this does not apply to computer
    private void pickUpCardDialog(final Player p, final UnoCard card) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        View view = View.inflate(this, R.layout.card_layout, null);

        //if the user pick up card which can play straight away
        if(p.comparingCard(card, currentCard)) {
            //display what card have user picked up
            alertDialogBuilder.setMessage("You pick up " + card.toString());

            alertDialogBuilder.setNegativeButton("Keep", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if player decide to keep it, player add the card to the deck and
                    //computer will play next
                    p.pickUpCard(card);
                    adapter.notifyDataSetChanged();
                    computerTurn();

                }
            });
            alertDialogBuilder.setPositiveButton("Play", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if computer decide to play it, player can play straight away
                    userPlayUno(player, card);
                    computerTurn();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setContentView(view);
            alertDialog.show();
            alertDialog.setCancelable(false);//ensure user can't click outside the dialog
        }
        else {
            //if user pick up a card which can't play instantly
            p.pickUpCard(card);
            updateCardUI();
            computerTurn();
        }
    }

    //method to access al the view in this activity
    private void accessAllView() {
        totalCards = (TextView) findViewById(R.id.totalCardOnDeck);
        topCardOnPile = (TextView) findViewById(R.id.topCardOnPile);
        opponentCards = (TextView) findViewById(R.id.opponent);
        playerCards = (TextView) findViewById(R.id.player);
        playerDeck = (GridView) findViewById(R.id.playerCard);
        pickUpCard = (Button) findViewById(R.id.pickCard);
        currentCardOnPile = (SquareTextView) findViewById(R.id.currentCardOnPile);
    }

    //add a delay by using countdown timer to improve user experience.
    //once user play the card, a small delay will happen for computer to play a card
    private void computerTurn() {
        new CountDownTimer(1100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                opponentCards.setText(getResources().getString(R.string.waitForOpponent));
                playerDeck.setEnabled(false);//prevent user choose a card during the countdown

                //hide button and text
                playerCards.setVisibility(View.INVISIBLE);
                pickUpCard.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFinish() {//once the countdown is done
                //enable button, text and allow user to click again
                playerCards.setVisibility(View.VISIBLE);
                playerDeck.setEnabled(true);
                pickUpCard.setVisibility(View.VISIBLE);

                opponentCards.setText(getString(R.string.opponentDeck, computer.numberOfCards()));
                computerPlayUno(computer);
            }
        }.start();
    }

    //this apply to computer only, ensure computer have card to play on their deck
    private boolean hasColourOrValue(Player p) {
        for (UnoCard card : p.playerCards()) {
            if(p.comparingCard(card, currentCard)) {
                return true;
            }
        }
        return false;
    }

    //this apply to user only, ensure user choose a valid card
    private boolean validCardChoice(Player p, UnoCard card) {
        return (p.comparingCard(card, currentCard));
    }

    public void updateCardUI() {//Method to update all textview
        totalCards.setText(getResources().getString(R.string.totalCardsOnDeck, deck.getDeck()));

        topCardOnPile.setText(getResources().getString(R.string.topCardOnPile));

        switch (currentCard.getCardColour()) {
            case "Red":
                currentCardOnPile.setBackgroundColor(Color.RED);
                currentCardOnPile.setTextColor(Color.BLACK);
                break;
            case "Blue":
                currentCardOnPile.setBackgroundColor(Color.BLUE);
                currentCardOnPile.setTextColor(Color.WHITE);
                break;
            case "Yellow":
                currentCardOnPile.setBackgroundColor(Color.YELLOW);
                currentCardOnPile.setTextColor(Color.BLACK);
                break;
            case "Green":
                currentCardOnPile.setBackgroundColor(Color.GREEN);
                currentCardOnPile.setTextColor(Color.BLACK);
                break;
        }
        currentCardOnPile.setText(String.valueOf(currentCard.getCardNumber()));
        opponentCards.setText(getString(R.string.opponentDeck, computer.numberOfCards()));
    }

    //Display a finish dialog where it announce who is the winner
    public void finish(Player p) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if(p.getPlayerName().equals("Player")) {
            alertDialogBuilder.setMessage("Congratulations, " +
                    "you win the game! You want to play again or quit?");
        }
        else { //compuer win :-(
            alertDialogBuilder.setMessage("Bad luck, you lost the game! " +
                    "but you still can take revenge, or you can give up");
        }
        alertDialogBuilder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();//close the app (not activity) if user decide to leave
            }
        });
        alertDialogBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recreate();//restart the activity
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
    }
}