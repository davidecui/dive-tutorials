package com.hazam.softwarequotes.model;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hazam.softwarequotes.R;

import java.util.*;

public class ListQuotesAdapter extends BaseAdapter implements Comparator<Quote> {
	private static final int VIEW_TYPE_COUNT = 2;
	private static final int VIEW_TYPE_SEPARATOR = 1;
	private static final int VIEW_TYPE_QUOTE = 0;
	private static final String TAG = "QUOTE";

	private final List<Object> flatListData = new ArrayList<Object>();
	private final Set<Quote> quotesList = new TreeSet<Quote>(this);
	private final TreeMap<String, List<Quote>> quotesByAuthor = new TreeMap<String, List<Quote>>();

    public ListQuotesAdapter() {}

	public ListQuotesAdapter(List<Quote> data) {
		addAll(data);
	}
	
	private void buildFlatData() {
		quotesByAuthor.clear();
		flatListData.clear();
		for (Quote q: quotesList) {
			List<Quote> quoteOfAuthor = quotesByAuthor.get(q.getAuthor());
			if (quoteOfAuthor == null) {
				quoteOfAuthor = new ArrayList<Quote>();
				quotesByAuthor.put(q.getAuthor(), quoteOfAuthor);
			}
			quoteOfAuthor.add(q);
		}
		//flatten the list
		for (String auth : quotesByAuthor.keySet()) {
			flatListData.add(auth);
			List<Quote> related = quotesByAuthor.get(auth);
			for (Quote q : related) {
				flatListData.add(q);
			}
		}
        notifyDataSetChanged();
	}

    public void addAll(List<Quote> data) {
		quotesList.addAll(data);
		Log.i(TAG, "Unique quotes "+quotesList.size());
		buildFlatData();
    }
	
	public void addQuote(Quote q) {
		quotesList.add(q);
		buildFlatData();
	}
	
	public void removeQuote(Quote q) {
		quotesList.remove(q);
		buildFlatData();
	}
	
	
	public int getCount() {
		return flatListData.size();
	}

	public Object getItem(int i) {
		return flatListData.get(i);
	}

	public long getItemId(int i) {
		return System.identityHashCode(getItem(i));
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		return isEnabled(position) ? VIEW_TYPE_QUOTE : VIEW_TYPE_SEPARATOR;
	}

	private int getResourceForPosition(int position) {
		return isEnabled(position) ? R.layout.quote : R.layout.separator;
	}

	public View getView(int pos, View convertView, ViewGroup father) {
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) father.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(getResourceForPosition(pos), null);
		}
		TextView textView = ((TextView) convertView);
		Object item = getItem(pos);
		String s = item instanceof Quote ? ((Quote) item).getBody() : item.toString();
		textView.setText(s);
		return convertView;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return flatListData.get(position) instanceof Quote;
	}

    public int compare(Quote object1, Quote object2) {
		int comparison = object1.getAuthor().compareTo(object2.getAuthor());
		if (comparison == 0) {
			comparison = object1.getBody().compareTo(object2.getBody());
		}
		if (comparison == 0) {
			comparison = object1.getSource().compareTo(object2.getSource());
		}
		return comparison;
	}
}
