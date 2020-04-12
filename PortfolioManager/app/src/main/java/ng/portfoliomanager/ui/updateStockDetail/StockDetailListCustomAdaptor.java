package ng.portfoliomanager.ui.updateStockDetail;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import ng.portfoliomanager.R;
import ng.portfoliomanager.ui.common.StockDetail;
import ng.portfoliomanager.ui.common.StockReport;

public class StockDetailListCustomAdaptor extends BaseAdapter {

    Context context;
    ArrayList<StockDetail> stockDetails;

    public StockDetailListCustomAdaptor(Context context, ArrayList<StockDetail> stockDetails) {
        this.context = context;
        this.stockDetails = stockDetails;
    }

    @Override
    public int getCount() {
        return stockDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return stockDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.stock_detail_list, parent, false);
        StockDetail stockDetail = (StockDetail) this.getItem(position);

        if (stockDetail != null) {
            TextView stockName = (TextView) convertView.findViewById(R.id.stockNameId);
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxId);
            TextView sockPurchasedDate = (TextView) convertView.findViewById(R.id.stockPurchasedDateTextView);
            if (stockName != null) {
                stockName.setText(stockDetail.getName());
            }
            if (sockPurchasedDate != null) {
                sockPurchasedDate.setText(stockDetail.getDate());
            }
            checkBox.setChecked(stockDetail.isChecked());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    stockDetail.setChecked(isChecked);
                }
            });
        }

        return convertView;
    }
}