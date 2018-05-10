package au.edu.federation.unogame;

public interface PlayerAction<T> {

    void pickUpCard(T otherCard);
    boolean playCard(T playCard);
    boolean comparingCard(T card1, T card2);

}
