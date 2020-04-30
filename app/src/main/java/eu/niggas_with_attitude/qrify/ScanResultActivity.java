package eu.niggas_with_attitude.qrify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    private Button actionButton;
    private ClipboardManager clipboardManager;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        Intent intent = getIntent();
        message = intent.getStringExtra(EXTRA_RESULT_TEXT);
        if (message == null) {
            message = "";
        }

        resultText = findViewById(R.id.resultText);
        shareButton = findViewById(R.id.shareButton);
        actionButton = findViewById(R.id.actionButton);
        
        resultText.setText(message);

        ClipData clipData = ClipData.newPlainText("text", message);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getApplicationContext(), "Content copied to clipboard", Toast.LENGTH_SHORT).show();

        setupActionButton();

        shareButton.setOnClickListener(
            view -> {
                shareText(message);
            }
        );
    }

    void setupActionButton () {
        if (message.startsWith("tel:")) {
            actionButton.setText(R.string.scan_result_activity_call_button);
        } else if (message.startsWith("mailto:")) {
            actionButton.setText(R.string.scan_result_activity_send_email_button);
        } else if (URLUtil.isValidUrl(message)) {
            actionButton.setText(R.string.scan_result_activity_open_page_button);
        } else {
            actionButton.setText(R.string.scan_result_activity_search_button);
            actionButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, message);
                startActivity(intent);
            });
            return;
        }
        actionButton.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(message));
            startActivity(i);
        });
    }

    private void shareText(String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent , "Share"));
    }
}
