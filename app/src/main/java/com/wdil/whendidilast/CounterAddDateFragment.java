package com.wdil.whendidilast;

import org.joda.time.DateTime;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class CounterAddDateFragment extends Fragment {

	private CounterDataSource dataSource;
	private Counter initialCounter;
	
	public CounterDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(CounterDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public Counter getInitialCounter() {
		return initialCounter;
	}

	public void setInitialCounter(Counter initialCounter) {
		this.initialCounter = initialCounter;
	}

	public CounterAddDateFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.counter_add_date_fragment, container,
				false);
		
		Button addButton = (Button) rootView.findViewById(R.id.addButton);
		addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addEvent(v);
            }
        });
		
		ArrayAdapter<Counter> adapter = new ArrayAdapter<Counter>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, dataSource.getAllCounters());
		
		AutoCompleteTextView textView = (AutoCompleteTextView) rootView.findViewById(R.id.eventName);
		textView.setAdapter(adapter);
		
		TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO) {
					// hide virtual keyboard
			           InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			           imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			           return true;
				}
				return false;
			}
		};
		
		textView.setOnEditorActionListener(editorListener);
		
		if(initialCounter != null){
			final TextView topTextView = (TextView) rootView.findViewById(R.id.topTextView);
			String counterInfo = initialCounter.getCounterInfo();
			topTextView.setText(counterInfo);
			textView.setText(initialCounter.getName());
			textView.setEnabled(false);
			textView.dismissDropDown();
			
			rootView.setOnTouchListener(new OnSwipeTouchListener(rootView.getContext()) {
				@Override
				public void onSwipeLeft() {
			        CounterViewDatesFragment viewDatesFragment = new CounterViewDatesFragment();
			        viewDatesFragment.setDataSource(dataSource);
			        viewDatesFragment.setCounter(initialCounter);
			        getFragmentManager().beginTransaction().replace(R.id.container, viewDatesFragment).commit();
			    }
			});
		}
		
		return rootView;
		
	}
	
	public void addEvent(View v) {
		View root = (View) v.getRootView();
		AutoCompleteTextView editText = ((AutoCompleteTextView)root.findViewById(R.id.eventName));
		String eventName = editText.getText().toString();
		if(eventName.equals("") | eventName == null) {
			return;
		}
		TimePicker timePicker = (TimePicker)root.findViewById(R.id.timePicker1);
		int hour = timePicker.getCurrentHour();
		int minute = timePicker.getCurrentMinute();
		DatePicker datePicker = (DatePicker)root.findViewById(R.id.datePicker1);
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth() + 1; //Datepicker is 0 indexed while DateTime months are 1 indexed
		int year = datePicker.getYear();
		DateTime eventDate = new DateTime(year, month, day, hour, minute);
		if(Utils.countersContainsEventName(dataSource.getAllCounters(), eventName)) {//Existing counter
			Counter counter = Utils.findCounterWithNameFromList(dataSource.getAllCounters(), eventName);
			dataSource.addDateToCounter(counter, eventDate);
			if(initialCounter != null){
				TextView textView = (TextView) this.getView().findViewById(R.id.topTextView);
				textView.setText(counter.getCounterInfo());
			}
			Toast.makeText(getActivity(), "Added Date to existing event", Toast.LENGTH_SHORT).show();
		}
		else {//New counter
			Counter counter = dataSource.createCounter(eventName);
			dataSource.addDateToCounter(counter, eventDate);
			ArrayAdapter<Counter> adapter = (ArrayAdapter<Counter>) editText.getAdapter();//Possibly unsafe. Look in to.
			adapter.notifyDataSetChanged();
			if(initialCounter == null) {
				TextView textView = (TextView) this.getView().findViewById(R.id.eventName);
				textView.setText("");
			}
			Toast.makeText(getActivity(), "Created new event", Toast.LENGTH_SHORT).show();
		}
		
	}
	
}