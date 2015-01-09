package com.wdil.whendidilast;


import android.support.v7.app.ActionBarActivity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
	//TODO app crashes if rotated. Currently avoiding issue by forcing to stay in portrait. Actually address problem at some point.
	CounterDataSource dataSource;
	
	public CounterDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(CounterDataSource dataSource) {
		this.dataSource = dataSource;
	}

	ExistingCountersFragment existingFragment;
	CounterAddDateFragment newFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Disables icon in action bar, with delay unfortunately
		//getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		existingFragment = new ExistingCountersFragment();
		newFragment = new CounterAddDateFragment();
		dataSource = new CounterDataSource(this);
		dataSource.open();
		existingFragment.setDataSource(dataSource);
		newFragment.setDataSource(dataSource);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, newFragment).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch(id){
		case R.id.new_counter:
			getSupportFragmentManager().beginTransaction().replace(R.id.container, newFragment).commit();
			return true;
		case R.id.existing_counter:
			getSupportFragmentManager().beginTransaction().replace(R.id.container, existingFragment).commit();
			return true;
		default: 
			return super.onOptionsItemSelected(item);
		
		}
		
	}

}
