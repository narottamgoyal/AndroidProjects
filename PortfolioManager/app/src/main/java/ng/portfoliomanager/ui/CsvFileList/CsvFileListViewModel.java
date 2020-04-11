package ng.portfoliomanager.ui.CsvFileList;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import ng.portfoliomanager.ui.common.StockDetail;
import ng.portfoliomanager.ui.common.StockReport;

public class CsvFileListViewModel extends ViewModel {
    private String fileName = null; //(Environment.getExternalStorageDirectory().getAbsolutePath() + "/portfolioManagerData.csv");
    private String fileData = "";
    public ArrayList<StockReport> stockReports = new ArrayList<StockReport>();

    public CsvFileListViewModel() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void readCsvFile() {
        try {
            final File file = new File(fileName);
            fileData = readFile(fileName, StandardCharsets.UTF_8);
            if (fileData == null || fileData.equals(""))
                fileData = "Stock Name,Date Time";
            processCsvFileData();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fileData = "Stock Name,Date Time";
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void processCsvFileData() {
        String list[] = fileData.split("\n");
        if (list.length <= 1) return;
        for (int i = 1; i < list.length; i++) {
            String rowData[] = list[i].split(",");
            StockReport obj = stockReports.stream().filter(o -> o.getName().equals(rowData[0])).findFirst().orElse(null);
            if (obj == null)
                stockReports.add(new StockReport(new StockDetail(rowData[0], rowData[1])));
            else
                obj.updateStockReport(new StockDetail(rowData[0], rowData[1]));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}