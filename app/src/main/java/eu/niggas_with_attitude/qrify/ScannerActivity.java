package eu.niggas_with_attitude.qrify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

public class ScannerActivity extends AppCompatActivity {

    private DecoratedBarcodeView barcodeScannerView;
    private CaptureManager capture;
    private SlidingUpPanelLayout slidingLayout;
    private LinearLayout openHeader;
    private Button generateButton;
    private Button closeSliderButton;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null) {
                return;
            }

            Intent intent = new Intent(ScannerActivity.this, ScanResultActivity.class);
            intent.putExtra(ScanResultActivity.EXTRA_RESULT_TEXT, result.getText());
            startActivity(intent);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        barcodeScannerView = findViewById(R.id.barcode_scanner);
        slidingLayout = findViewById(R.id.slidingLayout);
        openHeader = findViewById(R.id.openHeader);
        generateButton = findViewById(R.id.generateButton);
        closeSliderButton = findViewById(R.id.closeSliderButton);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.setShowMissingCameraPermissionDialog(false);
        barcodeScannerView.setStatusText("");
        barcodeScannerView.decodeContinuous(callback);
        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.SimplePanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                super.onPanelSlide(panel, slideOffset);
                openHeader.setAlpha(slideOffset);
            }
        });

        generateButton.setOnClickListener(v -> {
            Intent intent = new Intent(ScannerActivity.this, GeneratorActivity.class);
            startActivity(intent);
        });

        closeSliderButton.setOnClickListener(
                v -> slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED)
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
