package com.wdil.whendidilast;

import java.util.List;

public class Utils {
	
	public static String[] getNamesFromCounters(List<Counter> counters) {
		String[] names = new String[counters.size()];
		for(int  i = 0; i < counters.size(); i++) {
			names[i] = counters.get(i).getName();
		}
		return names;
	}
	
	public static boolean countersContainsEventName(List<Counter> counters, String name) {
		for(Counter counter: counters) {
			if(name.equals(counter.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public static Counter findCounterWithNameFromList(List<Counter> counters, String name) {
		for(Counter counter: counters) {
			if(name.equals(counter.getName())) {
				return counter;
			}
		}
		return null;
	}
}
