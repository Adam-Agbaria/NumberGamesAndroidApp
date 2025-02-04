package dev.adamag.numbersgamehost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView thankYouText = findViewById(R.id.thank_you_text);
        Button backToMenuButton = findViewById(R.id.back_to_menu_button);

        // Button Click: Return to Main Menu
        backToMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
