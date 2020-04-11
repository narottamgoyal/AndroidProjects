package ng.portfoliomanager.ui.common;

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

    public ArrayList<StockDetail> getAll() {
        ArrayList<StockDetail> list = new ArrayList<>();
        for (StockDetail stock : stockDetails)
            list.add(stock);
        return list;
    }
}
