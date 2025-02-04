package dev.adamag.numbersgamehost;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dev.adamag.number_games_lib.GameManager;
import dev.adamag.number_games_lib.model.GameResponse;
import dev.adamag.number_games_lib.model.GameStatusResponse;

public class GameActivity extends AppCompatActivity {

    private GameManager gameManager;
    private String gameId;
    private TextView gameStatusTextView;
    private Button actionButton; // This button will toggle between "End Round" & "Start Round"
    private Handler handler;
    private boolean isRoundEnded = false; // Track whether a round is ended
    private Runnable statusPollingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameManager = new GameManager();
        gameId = getIntent().getStringExtra("game_id");

        gameStatusTextView = findViewById(R.id.game_status);
        actionButton = findViewById(R.id.action_button);

        handler = new Handler(Looper.getMainLooper());

        // Start the game when entering this activity
        startGame();

        // Poll game status every 3 seconds to check if it's finished
        startGameStatusPolling();

        actionButton.setOnClickListener(v -> {
            if (isRoundEnded) {
                startRound();
            } else {
                endRound();
            }
        });
    }

    private void startGame() {
        gameManager.startGame(gameId, new GameManager.GameCallback<GameResponse>() {
            @Override
            public void onSuccess(GameResponse response) {
                gameStatusTextView.setText("Game Started. Waiting for round to finish...");
                actionButton.setText("End Round");
                isRoundEnded = false;
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(GameActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void endRound() {
        gameManager.endRound(gameId, new GameManager.GameCallback<GameResponse>() {
            @Override
            public void onSuccess(GameResponse response) {
                Toast.makeText(GameActivity.this, "Round Ended!", Toast.LENGTH_SHORT).show();
                actionButton.setText("Start Round");
                isRoundEnded = true;
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(GameActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startRound() {
        gameManager.nextRound(gameId, new GameManager.GameCallback<GameResponse>() {
            @Override
            public void onSuccess(GameResponse response) {
                Toast.makeText(GameActivity.this, "Next Round Started!", Toast.LENGTH_SHORT).show();
                gameStatusTextView.setText("Round Started. Waiting for players...");
                actionButton.setText("End Round");
                isRoundEnded = false;
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(GameActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startGameStatusPolling() {
        statusPollingRunnable = new Runnable() {
            @Override
            public void run() {
                gameManager.getGameStatus(gameId, new GameManager.GameCallback<GameStatusResponse>() {
                    @Override
                    public void onSuccess(GameStatusResponse response) {
                        if ("finished".equalsIgnoreCase(response.getStatus())) {
                            Toast.makeText(GameActivity.this, "Game Finished! Redirecting...", Toast.LENGTH_SHORT).show();
                            goToResults();
                        } else {
                            handler.postDelayed(statusPollingRunnable, 3000);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e("GameStatusPolling", "Error checking game status: " + errorMessage);
                        handler.postDelayed(statusPollingRunnable, 3000);
                    }
                });
            }
        };

        handler.postDelayed(statusPollingRunnable, 3000);
    }

    private void goToResults() {
        Intent intent = new Intent(GameActivity.this, ResultsActivity.class);
        intent.putExtra("game_id", gameId);
        startActivity(intent);
        finish();
    }
}
