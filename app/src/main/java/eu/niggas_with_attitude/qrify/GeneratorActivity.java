package eu.niggas_with_attitude.qrify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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
        //displayMessage();
        generateCode();
    }

    private void displayMessage() {
        new AlertDialog.Builder(GeneratorActivity.this)
                .setTitle("Input value")
                .setMessage(inputText)
                .setCancelable(false)
                .setPositiveButton("ok", (dialog, which) -> {
                }).show();
    }

    private void generateCode() {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(inputText, BarcodeFormat.QR_CODE, 400, 400);
            ImageView imageViewQrCode = (ImageView) findViewById(R.id.qrCode);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch(Exception e) {

        }
    }
}
