package ng.portfoliomanager.ui.updateStockDetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import ng.portfoliomanager.R;
import ng.portfoliomanager.ui.common.StockDetail;

public class StockDetailActivity extends AppCompatActivity {

    private Realm realm;
    EditText editText;
    ListView listView;
    RealmResults<StockDetail> realmResults;
    ArrayList<StockDetail> stockDetails;
    StockDetail stockDetail;
    StockDetailListCustomAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        realm = Realm.getDefaultInstance();
        final Button updateStockButton = findViewById(R.id.updateStockButton);
        editText = findViewById(R.id.editStockNameTextView);

        Intent intent = getIntent();
        String stockId = intent.getStringExtra("id");
        String stockName = intent.getStringExtra("stockName");
        realmResults = realm.where(StockDetail.class).equalTo("Name", stockName).findAll();
        LoadStockDetail();
        listView = (ListView) findViewById(R.id.stockListView);
        // List static Header
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.stock_detail_list_header, listView, false);
        listView.addHeaderView(headerView);

        // List Header create dynamically
        //ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.stock_detail_list, listView, false);
        //TextView stockNameHeader = (TextView) headerView.findViewById(R.id.stockNameId);
        //TextView dateHeader = (TextView) headerView.findViewById(R.id.stockPurchasedDateTextView);
        //stockNameHeader.setText(R.string.stockNameHeader);
        //stockNameHeader.setTextColor(ContextCompat.getColor(this, R.color.colorHeader));
        //dateHeader.setText(R.string.dateHeader);
        //dateHeader.setTextColor(ContextCompat.getColor(this, R.color.colorHeader));
        //listView.addHeaderView(headerView);

        adapter = new StockDetailListCustomAdaptor(this, stockDetails);
        listView.setAdapter(adapter);
        editText.setText(stockName);

        final ImageButton deleteStockButton = headerView.findViewById(R.id.deleteStockButton);
        deleteStockButton.setOnClickListener(v -> {
            boolean anyRecordNeedToBeDeleted = false;
            for (StockDetail stockDetail : stockDetails)
                if (stockDetail.isChecked()) {
                    anyRecordNeedToBeDeleted = true;
                    break;
                }
            if (anyRecordNeedToBeDeleted == false) return;
            AlertDialog.Builder alert = new AlertDialog.Builder(StockDetailActivity.this);
            alert.setMessage("Do you want delete the record(s)")
                    .setPositiveButton("Yes", (dialog, which) -> deleteRecord())
                    .setNegativeButton("No", null)
                    .setCancelable(false)
                    .show();
        });

        updateStockButton.setOnClickListener(v -> {
            updateRecord();
            onBackPressed();
        });
    }

    private void deleteRecord() {
        realm.executeTransaction(realm -> {
            try {
                for (StockDetail stockDetail : stockDetails) {
                    if (stockDetail.isChecked()) {
                        stockDetail.deleteFromRealm();
                    }
                }
                Toast.makeText(StockDetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            } finally {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
