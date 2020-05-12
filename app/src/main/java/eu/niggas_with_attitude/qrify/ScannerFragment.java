package eu.niggas_with_attitude.qrify;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import eu.niggas_with_attitude.qrify.database.model.SavedCode;
import eu.niggas_with_attitude.qrify.viewmodels.HistoryViewModel;


public class ScannerFragment extends Fragment {

    private DecoratedBarcodeView barcodeScannerView;
    private CaptureManager capture;
    private Button generateButton;
    private HistoryViewModel viewModel;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null) {
                return;
            }
            SavedCode savedCode = insertCodeToDatabase(result.getText());
            Intent intent = new Intent(requireContext(), ScanResultActivity.class);
            intent.putExtra(ScanResultActivity.EXTRA_CODE, savedCode);
            startActivity(intent);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);

        barcodeScannerView = view.findViewById(R.id.barcode_scanner);
        generateButton = view.findViewById(R.id.generateButton);

        capture = new CaptureManager(requireActivity(), barcodeScannerView);
        barcodeScannerView.setStatusText("Point the camera to a QR Code to scan it");
        barcodeScannerView.decodeContinuous(callback);

        generateButton.setOnClickListener(v -> NavHostFragment
                .findNavController(ScannerFragment.this)
                .navigate(R.id.action_scannerFragment_to_generatorFragment));
    }

    @Override
    public void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private SavedCode insertCodeToDatabase(String msg) {
        SavedCode savedCode = new SavedCode();
        savedCode.setCode(msg);
        savedCode.setSource(0);
        viewModel.insertSavedCode(savedCode);
        return savedCode;
    }
}
