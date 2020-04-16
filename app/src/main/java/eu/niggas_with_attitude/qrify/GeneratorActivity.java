package eu.niggas_with_attitude.qrify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GeneratorActivity extends AppCompatActivity {

    private String inputText;

    private Bitmap qrcode;

    private Button generateButton;
    private Button saveButton;
    private EditText inputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        generateButton = findViewById(R.id.generateButton);
        inputField = findViewById(R.id.generatorInput);
        saveButton = findViewById(R.id.qrSaveButton);

        generateButton.setOnClickListener(view -> getInputValue());
        saveButton.setOnClickListener(view -> shareImage());
    }

    // Gets the input value from input field
    private void getInputValue() {
        inputText = inputField.getText().toString();
        generateCode();
    }

    // Displays message TODO: Remove when unnecessary
    private void displayMessage(String title, String msg) {
        new AlertDialog.Builder(GeneratorActivity.this)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("ok", (dialog, which) -> {
                }).show();
    }

    // Generates the QRcode and displays it for the user
    private void generateCode() {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qrcode = barcodeEncoder.encodeBitmap(inputText, BarcodeFormat.QR_CODE, 400, 400);
            ImageView imageViewQrCode = findViewById(R.id.qrCode);
            imageViewQrCode.setImageBitmap(qrcode);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Saves a temp image in folder inaccessible to the user
    private Uri saveImageExternal() {
        Uri uri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png");
            FileOutputStream stream = new FileOutputStream(file);
            qrcode.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = FileProvider.getUriForFile(GeneratorActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    // Shares the temp image that was saved
    private void shareImage() {
        if(qrcode != null) {
            Uri toSend = saveImageExternal();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");
            // Grants temporary read permission
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, toSend);
            startActivity(Intent.createChooser(intent , "Share"));
        } else {
            displayMessage("Error", "No QRcode generated");
        }

    }

}
