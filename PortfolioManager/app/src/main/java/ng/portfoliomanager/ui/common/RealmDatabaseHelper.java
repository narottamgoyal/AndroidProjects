package ng.portfoliomanager.ui.common;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmDatabaseHelper {
    Realm realm;
    RealmResults<StockDetail> stockDetails;

    public RealmDatabaseHelper(Realm realm) {
        this.realm = realm;
        loadFromDb();
    }

    public void loadFromDb() {
        stockDetails = realm.where(StockDetail.class).findAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<StockReport> GetReport() {
        ArrayList<StockReport> stockList = new ArrayList<StockReport>();
        for (StockDetail stock : stockDetails) {
            StockReport obj = stockList.stream().filter(o -> o.getName().equals(stock.getName())).findFirst().orElse(null);
            if (obj == null) stockList.add(new StockReport(stock));
            else
                obj.updateStockReport(stock);
        }
        return stockList;
    }
}
