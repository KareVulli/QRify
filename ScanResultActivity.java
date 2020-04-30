package eu.niggas_with_attitude.qrify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ClipboardManager;

public class ScanResultActivity extends AppCompatActivity {

    public static final String EXTRA_RESULT_TEXT = "result_text";
    public static final String EXTRA_RESULT_IMAGE = "result_image";

    private TextView resultText;
    private Button copyButton;
    private Button shareButton;
    private Button openPageButton;
    private ClipboardManager clipboardManager;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        Intent intent = getIntent();
        message = intent.getStringExtra(EXTRA_RESULT_TEXT);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        resultText = findViewById(R.id.resultText);
        copyButton = findViewById(R.id.copyButton);
        shareButton = findViewById(R.id.shareButton);
        openPageButton = findViewById(R.id.openPageButton);
        
        resultText.setText(message);

        if (URLUtil.isValidUrl(message)) {
            openPageButton.setVisibility(View.VISIBLE);
            openPageButton.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(message));
                startActivity(i);
            });
        }

        copyButton.setOnClickListener(
            view -> {
                ClipData clipData = ClipData.newPlainText("text", message);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();
            }
        );
        shareButton.setOnClickListener(
                view -> {
                    shareText(message);
                }
        );
    }
    private void shareText(String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, message);
        startActivity(Intent.createChooser(intent , "Share"));
    }
}
