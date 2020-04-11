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
import ng.portfoliomanager.ui.updatestockdetail.StockDetailActivity;
import ng.portfoliomanager.ui.common.StockDetail;

public class CustomAdaptor extends BaseAdapter {

    Context context;
    ArrayList<StockDetail> stockDetails;

    public CustomAdaptor(Context context, ArrayList<StockDetail> stockDetails) {
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
        convertView = inflater.inflate(R.layout.list_adapter_view, parent, false);
        StockDetail stockDetail = (StockDetail) this.getItem(position);

        if (stockDetail != null) {
            TextView stockName = (TextView) convertView.findViewById(R.id.stockNameTextView);
            TextView stockCount = (TextView) convertView.findViewById(R.id.stockCounttextView);
            if (stockName != null) {
                stockName.setText(stockDetail.getName());
            }
            if (stockCount != null) {
                stockCount.setText("NA");
            }

            // Row click event for update
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StockDetailActivity.class);
                    intent.putExtra("id", stockDetail.getId());
                    intent.putExtra("name", stockDetail.getName());
                    context.startActivity(intent);
                }
            });

        }

        return convertView;
    }
}
