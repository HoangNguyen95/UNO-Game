package au.edu.federation.unogame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button quitButton = (Button) findViewById(R.id.quitButton);//access the quit button
        Button playUnoButton = (Button) findViewById(R.id.playUnoButton);//access the play button

        //go to PlayUnoActivity to start the game
        playUnoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playUno = new Intent(MainActivity.this, PlayUnoActivity.class);
                startActivity(playUno);
                //ensuring user can resume this activity when user press home button
                //if user press back button, the game will restart itself
                playUno.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityIfNeeded(playUno, 0);

            }
        });

        //quit the application if the user don't want to play anymore
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}