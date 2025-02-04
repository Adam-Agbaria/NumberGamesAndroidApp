package dev.adamag.numbersgamehost;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import dev.adamag.number_games_lib.GameManager;
import dev.adamag.number_games_lib.model.CreateGameResponse;

public class MainActivity extends AppCompatActivity {

    private GameManager gameManager;
    private ImageView qrCodeImageView;
    private Button createGameButton, startGameButton;
    private TextView gameIdTextView;
    private String gameId;  // Store the created game ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameManager = new GameManager();

        qrCodeImageView = findViewById(R.id.qr_code_image);
        createGameButton = findViewById(R.id.create_game_button);
        startGameButton = findViewById(R.id.start_game_button);
        gameIdTextView = findViewById(R.id.game_id_text);

        startGameButton.setVisibility(View.GONE);
        gameIdTextView.setVisibility(View.GONE);

        createGameButton.setOnClickListener(v -> createGame());
        startGameButton.setOnClickListener(v -> startGame());
    }

    private void createGame() {
        gameManager.createGame(3, new GameManager.GameCallback<CreateGameResponse>() {
            @Override
            public void onSuccess(CreateGameResponse response) {
                gameId = response.getGameId();
                generateQRCode("https://numbers-game-web-app.vercel.app//");

                // ✅ Show Game ID on screen
                gameIdTextView.setText("Game ID: " + gameId);
                gameIdTextView.setVisibility(View.VISIBLE);

                startGameButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateQRCode(String sessionUrl) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(sessionUrl, BarcodeFormat.QR_CODE, 600, 600);
            qrCodeImageView.setImageBitmap(bitmap);
            qrCodeImageView.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            Log.e("QR Code", "Error generating QR Code", e);
        }
    }

    private void startGame() {
        if (gameId == null) return;

        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("game_id", gameId);
        startActivity(intent);
    }
}
