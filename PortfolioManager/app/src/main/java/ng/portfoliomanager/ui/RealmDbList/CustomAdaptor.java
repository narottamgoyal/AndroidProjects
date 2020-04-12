package ng.portfoliomanager.ui.RealmDbList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ng.portfoliomanager.R;
import ng.portfoliomanager.ui.common.StockReport;
import ng.portfoliomanager.ui.updateStockDetail.StockDetailActivity;

public class CustomAdaptor extends BaseAdapter {

    Context context;
    ArrayList<StockReport> stockReports;

    public CustomAdaptor(Context context, ArrayList<StockReport> stockReports) {
        this.context = context;
        this.stockReports = stockReports;
    }

    @Override
    public int getCount() {
        return stockReports.size();
    }

    @Override
    public Object getItem(int position) {
        return stockReports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_adapter_view, parent, false);
        StockReport stockReport = (StockReport) this.getItem(position);

        if (stockReport != null) {
            TextView stockName = (TextView) convertView.findViewById(R.id.stockNameTextView);
            TextView stockCount = (TextView) convertView.findViewById(R.id.stockCounttextView);
            if (stockName != null) {
                stockName.setText(stockReport.getName());
            }
            if (stockCount != null) {
                stockCount.setText(stockReport.getCount() + "");
            }

            // Row click event for update
            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(context, StockDetailActivity.class);
                //intent.putExtra("id", stockReport.getId());
                intent.putExtra("stockName", stockReport.getName());
                context.startActivity(intent);
            });
        }

        return convertView;
    }
}
