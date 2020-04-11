package ng.portfoliomanager.ui.AddStock;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;

import ng.portfoliomanager.R;

public class AddStockFragment extends Fragment {

    private AddStockViewModel addStockViewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addStockViewModel = ViewModelProviders.of(this).get(AddStockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_stock, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);

        String f = this.getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + File.separator + "portfolioManagerData.csv";
        addStockViewModel.setFileName(f);
        textView.setText("Please add you stocks here");
        final Button addStockButton = root.findViewById(R.id.addStock);
        final EditText inputEditText = root.findViewById(R.id.addStockInputText);
        addStockViewModel.setContext(this.getContext());
        addStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stockName = inputEditText.getText().toString();
                addStockViewModel.saveRecord(stockName);
            }
        });
        addStockViewModel.readCsvFile();
        return root;
    }
}
