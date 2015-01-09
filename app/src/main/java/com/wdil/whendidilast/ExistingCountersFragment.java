package com.wdil.whendidilast;

import java.util.List;

import org.joda.time.DateTime;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExistingCountersFragment extends ListFragment {

	public ExistingCountersFragment(){
		
	}
	private CounterDataSource dataSource;

	public CounterDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(CounterDataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.existing_counters_fragment,
				container, false);
		
		List<Counter> counters = dataSource.getAllCounters();
		
		final ArrayAdapter<Counter> adapter = new ArrayAdapter<Counter>(getActivity(),
		        android.R.layout.simple_list_item_1, counters);
		setListAdapter(adapter);
		return rootView;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                getListView().invalidateViews();

                dataSource.deleteCounter((Counter) getListAdapter().getItem(position));
                final ArrayAdapter<Counter> adapter = new ArrayAdapter<Counter>(getActivity(),
                        android.R.layout.simple_list_item_1, dataSource.getAllCounters());
                setListAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();

                return true;
            }
        });
    }

    @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Counter counter = dataSource.getAllCounters().get(position);
		for(DateTime date: counter.getDates()){
			Log.d("Existing Counters", counter.getName() + " : " + date.getMillis() + "");
			Log.d("Existing Counters", counter.getName() + " : " + date.toString());
		}
		CounterAddDateFragment counterFragment = new CounterAddDateFragment();
		counterFragment.setDataSource(dataSource);
		counterFragment.setInitialCounter(counter);
		this.getFragmentManager().beginTransaction().replace(R.id.container, counterFragment).commit();
	}
	
}