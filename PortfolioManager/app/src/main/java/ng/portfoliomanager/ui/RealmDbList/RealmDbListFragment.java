package ng.portfoliomanager.ui.RealmDbList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import ng.portfoliomanager.R;
import ng.portfoliomanager.ui.common.RealmDatabaseHelper;

public class RealmDbListFragment extends Fragment {

    private RealmDbListViewModel realmDbListViewModel;
    RealmDatabaseHelper realmDatabaseHelper;
    ListView listView;
    private Realm realm;
    RealmChangeListener realmChangeListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        realmDbListViewModel = ViewModelProviders.of(this).get(RealmDbListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_realm_db_list, container, false);
        realm = Realm.getDefaultInstance();
        realmDatabaseHelper = new RealmDatabaseHelper(realm);
        listView = (ListView) root.findViewById(R.id.realmListView);

        CustomAdaptor adapter = new CustomAdaptor(this.getContext(), realmDatabaseHelper.getAll());
        listView.setAdapter(adapter);

        Refresh(this.getContext());
        return root;
    }

    private void Refresh(Context context) {
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                CustomAdaptor adapter = new CustomAdaptor(context, realmDatabaseHelper.getAll());
                listView.setAdapter(adapter);
            }
        };
        realm.addChangeListener(realmChangeListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.removeChangeListener(realmChangeListener);
        realm.close();
    }
}
