package ng.portfoliomanager.ui.updatestockdetail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.RealmResults;
import ng.portfoliomanager.R;
import ng.portfoliomanager.ui.common.StockDetail;

public class StockDetailActivity extends AppCompatActivity {

    private Realm realm;
    EditText editText;
    RealmResults<StockDetail> realmResults;

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
        editText.setText(stockName);

        updateStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRecord();
                onBackPressed();
            }
        });
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
