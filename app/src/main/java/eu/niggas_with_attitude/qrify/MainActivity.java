package eu.niggas_with_attitude.qrify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

public class MainActivity extends AppCompatActivity {

    private Button openScannerButton;
    private Button openGeneratorButton;
    private TextView helloText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloText = findViewById(R.id.helloText);

        openScannerButton = findViewById(R.id.openScannerButton);
        openGeneratorButton = findViewById(R.id.openGeneratorButton);

        openScannerButton.setOnClickListener(
                v -> new IntentIntegrator(MainActivity.this)
                        .setOrientationLocked(false)
                        .setCaptureActivity(ScannerActivity.class)
                        .setBeepEnabled(false)
                        .setPrompt("")
                        .initiateScan()
        );

        openGeneratorButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, GeneratorActivity.class)));
    }
}
