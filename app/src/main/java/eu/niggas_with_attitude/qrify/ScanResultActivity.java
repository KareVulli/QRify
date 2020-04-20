package eu.niggas_with_attitude.qrify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ClipboardManager;

import java.util.List;

import eu.niggas_with_attitude.qrify.database.CodeDatabase;
import eu.niggas_with_attitude.qrify.database.dao.SavedCodeDao;
import eu.niggas_with_attitude.qrify.database.model.SavedCode;


public class ScanResultActivity extends AppCompatActivity {

    public static final String EXTRA_RESULT_TEXT = "result_text";
    public static final String EXTRA_RESULT_IMAGE = "result_image";

    private TextView resultText;
    private Button copyButton;
    private Button shareButton;
    private Button openPageButton;
    private ClipboardManager clipboardManager;

    private SavedCodeDao savedCodeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        CodeDatabase codeDatabase = Room.databaseBuilder(getApplicationContext(),
                CodeDatabase.class, "code-db").allowMainThreadQueries().build();
        savedCodeDao = codeDatabase.getSavedCodeDao();

        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_RESULT_TEXT);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        resultText = findViewById(R.id.resultText);
        copyButton = findViewById(R.id.copyButton);
        shareButton = findViewById(R.id.shareButton);
        openPageButton = findViewById(R.id.openPageButton);
        
        resultText.setText(message);
        inputCodeToDatabase(message);

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
    }

    // Inserts the scanned code into the database
    private void inputCodeToDatabase(String msg) {
        SavedCode savedCode = new SavedCode();
        savedCode.setCode(msg);
        savedCode.setSource(0);
        savedCodeDao.insert(savedCode);

        List<SavedCode> savedCodeList = savedCodeDao.getAll();
        for(SavedCode savedCode1 :  savedCodeList) {
            System.out.println(savedCode1.getId() + " : " + savedCode1.getCode() + " : " + savedCode1.getSource());
        }
    }
}
