package ng.portfoliomanager.ui.AddStock;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import ng.portfoliomanager.ui.common.StockDetail;

public class AddStockViewModel extends ViewModel {
    private Context context;
    private Realm realm;
    private String fileName = null;// (Environment.getExternalStorageDirectory().getAbsolutePath() + "/portfolioManagerData.csv");
    private String fileData = "";
    private List<StockDetail> stockDetails = new ArrayList<StockDetail>();
    private HashMap<String, Integer> stockList = new HashMap<String, Integer>();

    public AddStockViewModel() {
        realm = Realm.getDefaultInstance();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveRecord(final String name) {
        String date = DateTimeFormatter.ofPattern("dd/MM/yyy").format(LocalDate.now());
        saveInRealmDb(name, date);
        saveInCsvFile(name, date);
    }

    private void saveInRealmDb(String name, String date) {
        realm.executeTransactionAsync(bgRealm -> {
            String primaryKeyValue = UUID.randomUUID().toString();
            StockDetail stockDetail = bgRealm.createObject(StockDetail.class, primaryKeyValue);
            stockDetail.setName(name);
            stockDetail.setDate(date);
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Toast.makeText(context, "Failed to saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void saveInCsvFile(String data, String date) {
        String finalData = data + "," + date;
        new Thread() {
            public void run() {
                try {
                    fileData += "\n" + finalData;
                    FileWriter fw = new FileWriter(fileName);
                    fw.append(fileData);
                    fw.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }.start();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void processCsvFileData() {
        String list[] = fileData.split("\n");
        if (list.length <= 1) return;
        for (int i = 1; i < list.length; i++) {
            String rowData[] = list[i].split(",");
            stockDetails.add(new StockDetail(rowData[0], rowData[1]));
            int count = stockList.getOrDefault(rowData[0], 0) + 1;
            stockList.put(rowData[0], count);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}