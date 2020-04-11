package ng.portfoliomanager.ui.common;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class StockReport {
    private String Name;
    private ArrayList<StockDetail> stockDetails = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public StockReport(StockDetail stockDetail) {
        Name = stockDetail.getName();
        updateStockReport(stockDetail);
    }

    public String getName() {
        return Name;
    }

    public int getCount() {
        return stockDetails.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateStockReport(StockDetail stockDetail) {
        //StockDetail obj = stockDetails.stream().filter(o -> o.getId().equals(stockDetail.getId())).findFirst().orElse(null);
        //if (obj == null)
        stockDetails.add(stockDetail);
    }
}
