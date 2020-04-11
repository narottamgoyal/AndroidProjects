package ng.portfoliomanager.ui.CsvFileList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ng.portfoliomanager.R;
import ng.portfoliomanager.ui.common.StockInfo;

public class ThreeColumn_ListAdapter extends ArrayAdapter<StockInfo> {

    private LayoutInflater mInflater;
    private ArrayList<StockInfo> users;
    private int mViewResourceId;

    public ThreeColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<StockInfo> users) {
        super(context, textViewResourceId, users);
        this.users = users;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        StockInfo stockInfo = users.get(position);

        if (stockInfo != null) {
            TextView stockName = (TextView) convertView.findViewById(R.id.stockNameTextView);
            TextView stockCount = (TextView) convertView.findViewById(R.id.stockCounttextView);
            if (stockName != null) {
                stockName.setText(stockInfo.getName());
            }
            if (stockCount != null) {
                stockCount.setText(stockInfo.getCount() + "");
            }
        }
        return convertView;
    }
}
