package ng.portfoliomanager.ui.CsvFileList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ng.portfoliomanager.R;
import ng.portfoliomanager.ui.common.StockReport;

public class TwoColumn_ListAdapter extends ArrayAdapter<StockReport> {

    private LayoutInflater mInflater;
    private ArrayList<StockReport> users;
    private int mViewResourceId;

    public TwoColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<StockReport> users) {
        super(context, textViewResourceId, users);
        this.users = users;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        StockReport stockReport = users.get(position);

        if (stockReport != null) {
            TextView stockName = (TextView) convertView.findViewById(R.id.stockNameTextView);
            TextView stockCount = (TextView) convertView.findViewById(R.id.stockCounttextView);
            if (stockName != null) {
                stockName.setText(stockReport.getName());
            }
            if (stockCount != null) {
                stockCount.setText(stockReport.getCount() + "");
            }
        }
        return convertView;
    }
}
