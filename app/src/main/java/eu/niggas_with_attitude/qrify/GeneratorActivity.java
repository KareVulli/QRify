package eu.niggas_with_attitude.qrify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class GeneratorActivity extends AppCompatActivity {

    private String inputText;

    private Button generateButton;
    private EditText inputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        generateButton = findViewById(R.id.generateButton);
        inputField = findViewById(R.id.generatorInput);

        generateButton.setOnClickListener(view -> getInputValue());
    }

    private void getInputValue() {
        inputText = inputField.getText().toString();
        displayMessage();
    }

    private void displayMessage() {
        new AlertDialog.Builder(GeneratorActivity.this)
                .setTitle("Input value")
                .setMessage(inputText)
                .setCancelable(false)
                .setPositiveButton("ok", (dialog, which) -> {
                }).show();
    }
}
