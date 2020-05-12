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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eu.niggas_with_attitude.qrify.database.model.SavedCode;

public class ScanResultActivity extends AppCompatActivity {

    public static final String EXTRA_CODE = "code";

    private TextView contentTypeText;
    private TextView scannedOnText;
    private TextView scannedDateText;
    private TextView resultText;
    private Button shareButton;
    private Button actionButton;
    private ClipboardManager clipboardManager;
    private SavedCode savedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        Intent intent = getIntent();
        savedCode = intent.getParcelableExtra(EXTRA_CODE);

        contentTypeText = findViewById(R.id.contentTypeText);
        scannedOnText = findViewById(R.id.scannedOnText);
        scannedDateText = findViewById(R.id.scannedDateText);
        resultText = findViewById(R.id.resultText);
        shareButton = findViewById(R.id.shareButton);
        actionButton = findViewById(R.id.actionButton);

        if (savedCode != null) {
            String displayCode = savedCode.getDisplayCode();

            resultText.setText(displayCode);
            shareButton.setOnClickListener(view -> shareText(displayCode));

            copyMessage(displayCode);
            setupActionButton(savedCode.getCode());

            if (savedCode.getSource() == 1) {
                scannedOnText.setText(R.string.scan_result_activity_generated_on);
            }

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            scannedDateText.setText(sf.format(new Date(savedCode.getTimestamp())));
        }
    }

    private void copyMessage(String message) {
        ClipData clipData = ClipData.newPlainText("text", message);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getApplicationContext(), "Content copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    void setupActionButton (String message) {
        if (message.startsWith("tel:")) {
            actionButton.setText(R.string.scan_result_activity_call_button);
            contentTypeText.setText(R.string.scan_result_activity_content_phone);
        } else if (message.startsWith("mailto:")) {
            actionButton.setText(R.string.scan_result_activity_send_email_button);
            contentTypeText.setText(R.string.scan_result_activity_content_email);
        } else if (URLUtil.isValidUrl(message)) {
            actionButton.setText(R.string.scan_result_activity_open_page_button);
            contentTypeText.setText(R.string.scan_result_activity_content_url);
        } else {
            actionButton.setText(R.string.scan_result_activity_search_button);
            actionButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, message);
                startActivity(intent);
            });
            contentTypeText.setText(R.string.scan_result_activity_content_text);
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
