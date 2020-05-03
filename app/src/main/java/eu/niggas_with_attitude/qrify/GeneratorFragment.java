package eu.niggas_with_attitude.qrify;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import eu.niggas_with_attitude.qrify.database.model.SavedCode;
import eu.niggas_with_attitude.qrify.viewmodels.HistoryViewModel;

public class GeneratorFragment extends Fragment {

    private String inputText;

    private HistoryViewModel viewModel;

    private Bitmap qrcode;

    private Button generateButton;
    private Button saveButton;
    private EditText inputField;
    private TextView contentInputText;
    private ImageView imageViewQrCode;

    private Button textTypeButton;
    private Button urlTypeButton;
    private Button emailTypeButton;
    private Button phoneTypeButton;

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_URL = 1;
    private static final int TYPE_EMAIL = 2;
    private static final int TYPE_PHONE = 3;

    private int selectedType = TYPE_TEXT;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_generator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);

        generateButton = view.findViewById(R.id.generateButton);
        inputField = view.findViewById(R.id.generatorInput);
        saveButton = view.findViewById(R.id.qrSaveButton);
        contentInputText = view.findViewById(R.id.contentInputText);
        imageViewQrCode = view.findViewById(R.id.qrCode);

        textTypeButton = view.findViewById(R.id.textTypeButton);
        urlTypeButton = view.findViewById(R.id.urlTypeButton);
        emailTypeButton = view.findViewById(R.id.emailTypeButton);
        phoneTypeButton = view.findViewById(R.id.phoneTypeButton);

        textTypeButton.setOnClickListener(v -> setSelectedType(TYPE_TEXT));
        urlTypeButton.setOnClickListener(v -> setSelectedType(TYPE_URL));
        emailTypeButton.setOnClickListener(v -> setSelectedType(TYPE_EMAIL));
        phoneTypeButton.setOnClickListener(v -> setSelectedType(TYPE_PHONE));

        generateButton.setOnClickListener(v -> generateInputHandler());
        saveButton.setOnClickListener(v -> shareClickHandler());

        setSelectedType(0);

        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputText = s.toString();
                if (!inputText.isEmpty()) {
                    generateCode();
                }
            }
        });
    }

    private void setSelectedType (int type) {
        this.selectedType = type;
        @ColorInt int defaultColor = Util.resolveColorAttr(requireContext(), android.R.attr.textColorPrimary);
        @ColorInt int activeColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary);
        textTypeButton.setTextColor(defaultColor);
        urlTypeButton.setTextColor(defaultColor);
        emailTypeButton.setTextColor(defaultColor);
        phoneTypeButton.setTextColor(defaultColor);
        switch (type) {
            case TYPE_TEXT:
                contentInputText.setText(R.string.generator_activity_input_title_text);
                textTypeButton.setTextColor(activeColor);
                break;
            case TYPE_URL:
                contentInputText.setText(R.string.generator_activity_input_title_url);
                urlTypeButton.setTextColor(activeColor);
                break;
            case TYPE_EMAIL:
                contentInputText.setText(R.string.generator_activity_input_title_email);
                emailTypeButton.setTextColor(activeColor);
                break;
            case TYPE_PHONE:
                contentInputText.setText(R.string.generator_activity_input_title_phone);
                phoneTypeButton.setTextColor(activeColor);
                break;
        }

    }

    private void generateInputHandler() {
        inputText = inputField.getText().toString();
        if(!inputText.equals("")) {
            generateCode();
            insertCodeToDatabase();
            Toast.makeText(requireContext(), "Saved QR Code to history", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Input text cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFormattedInputText() {
        switch (this.selectedType) {
            case TYPE_URL:
                if (!inputText.matches("^(?i)(https?)://.*$")) {
                    return "https://" + inputText;
                }
                return inputText;
            case TYPE_EMAIL:
                return "mailto:" + inputText;
            case TYPE_PHONE:
                return "tel:" + inputText;
            default:
                return inputText;
        }
    }

    // Generates the QRcode and displays it for the user
    private void generateCode() {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qrcode = barcodeEncoder.encodeBitmap(getFormattedInputText(), BarcodeFormat.QR_CODE, 400, 400);
            imageViewQrCode.setImageBitmap(qrcode);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Saves a temp image in folder inaccessible to the user
    private Uri saveImageExternal() {
        Uri uri = null;
        try {
            File file = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png");
            FileOutputStream stream = new FileOutputStream(file);
            qrcode.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = FileProvider.getUriForFile(requireContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void shareClickHandler() {
        inputText = inputField.getText().toString();
        if(!inputText.equals("")) {
            generateCode();
            insertCodeToDatabase();
            shareImage();
        } else {
            Toast.makeText(requireContext(), "Input text cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    // Shares the temp image that was saved
    private void shareImage() {
        Uri toSend = saveImageExternal();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        // Grants temporary read permission
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, toSend);
        startActivity(Intent.createChooser(intent , "Share"));
    }

    // Inserts the generated code into the database
    private void insertCodeToDatabase() {
        SavedCode savedCode = new SavedCode();
        savedCode.setCode(inputText);
        savedCode.setSource(1);
        viewModel.insertSavedCode(savedCode);
    }

}
