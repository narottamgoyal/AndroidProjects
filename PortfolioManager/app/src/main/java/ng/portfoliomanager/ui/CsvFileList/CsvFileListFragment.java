package ng.portfoliomanager.ui.CsvFileList;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;

import ng.portfoliomanager.R;

public class CsvFileListFragment extends Fragment {

    private CsvFileListViewModel csvFileListViewModel;
    ListView listView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        csvFileListViewModel = ViewModelProviders.of(this).get(CsvFileListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_csv_file_list, container, false);

        String f = this.getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + File.separator + "portfolioManagerData.csv";
        csvFileListViewModel.setFileName(f);

        LoadStockList(root);
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void LoadStockList(View root) {
        csvFileListViewModel.readCsvFile();
        if (csvFileListViewModel.stockList.size() == 0)
            Toast.makeText(this.getContext(), "The file is empty.", Toast.LENGTH_LONG).show();
        else {
            ThreeColumn_ListAdapter adapter = new ThreeColumn_ListAdapter(this.getContext(), R.layout.list_adapter_view, csvFileListViewModel.stockList);
            listView = (ListView) root.findViewById(R.id.csvListView);
            listView.setAdapter(adapter);
        }
    }
}
