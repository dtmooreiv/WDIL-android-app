package com.wdil.whendidilast;


import java.security.Policy;
import java.util.List;

import org.joda.time.DateTime;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CounterViewDatesFragment extends ListFragment{

	private CounterDataSource dataSource;
	private Counter counter;
	
	public CounterDataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(CounterDataSource dataSource) {
		this.dataSource = dataSource;
	}
	public Counter getCounter() {
		return counter;
	}
	public void setCounter(Counter counter) {
		this.counter = counter;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.counter_view_dates_fragment,
				container, false);
		
		
		ArrayAdapter<DateTime> adapter = new ArrayAdapter<DateTime>(getActivity(),
		        android.R.layout.simple_list_item_1, counter.getDates());
		setListAdapter(adapter);

        rootView.setOnTouchListener(new OnSwipeTouchListener(rootView.getContext()){
            @Override
            public void onSwipeRight() {
                CounterAddDateFragment addDateFragment = new CounterAddDateFragment();
                addDateFragment.setDataSource(dataSource);
                addDateFragment.setInitialCounter(counter);
                getFragmentManager().beginTransaction().replace(R.id.container,addDateFragment).commit();
            }
        });
        return rootView;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {//TODO change to long click.
        super.onListItemClick(l, v, position, id);

        dataSource.deleteDateFromCounter(counter, (DateTime) l.getAdapter().getItem(position));
        getListView().invalidateViews();
        ArrayAdapter<DateTime> adapter = new ArrayAdapter<DateTime>(getActivity(),
                android.R.layout.simple_list_item_1, counter.getDates());
        adapter.notifyDataSetInvalidated();

    }


}
