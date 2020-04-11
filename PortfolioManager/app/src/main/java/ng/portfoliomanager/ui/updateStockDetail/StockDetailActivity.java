package ng.portfoliomanager.ui.updateStockDetail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import ng.portfoliomanager.R;
import ng.portfoliomanager.ui.RealmDbList.CustomAdaptor;
import ng.portfoliomanager.ui.common.StockDetail;

public class StockDetailActivity extends AppCompatActivity {

    private Realm realm;
    EditText editText;
    RealmResults<StockDetail> realmResults;
    ArrayList<StockDetail> stockDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        realm = Realm.getDefaultInstance();
        final Button updateStockButton = findViewById(R.id.updateStockButton);
        editText = findViewById(R.id.editStockNameTextView);

        Intent intent = getIntent();
        //String stockId = intent.getStringExtra("id");
        String stockName = intent.getStringExtra("stockName");

        realmResults = realm.where(StockDetail.class).equalTo("Name", stockName).findAll();
        LoadStockDetail();

        ListView listView = (ListView) findViewById(R.id.stockListView);

        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.stock_detail_list_header, listView, false);
        listView.addHeaderView(headerView);

        StockDetailListCustomAdaptor adapter = new StockDetailListCustomAdaptor(this, stockDetails);
        listView.setAdapter(adapter);

        editText.setText(stockName);

        updateStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRecord();
                onBackPressed();
            }
        });
    }

    private void LoadStockDetail() {
        stockDetails = new ArrayList<>();
        for (StockDetail stockDetail : realmResults)
            stockDetails.add(stockDetail);
    }

    private void updateRecord() {
        realm.beginTransaction();
        for (StockDetail stockDetail : realmResults)
            stockDetail.setName(editText.getText().toString().toUpperCase());
        realm.commitTransaction();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
