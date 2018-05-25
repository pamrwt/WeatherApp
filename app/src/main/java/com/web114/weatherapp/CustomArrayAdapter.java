package com.web114.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user on 5/16/2018.
 */

public class CustomArrayAdapter extends ArrayAdapter<ListJsonObject> {
    private final String MY_DEBUG_TAG = "ListJsonObjectAdapter";
    private List<ListJsonObject> items;
    private List<ListJsonObject> itemsAll;
    private List<ListJsonObject> suggestions;
    private int viewResourceId;
    public CustomArrayAdapter(Context context, int viewResourceId, List<ListJsonObject> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = new ArrayList<>(this.items);
        this.suggestions = new ArrayList<ListJsonObject>();
        this.viewResourceId = viewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        ListJsonObject listJsonObject = items.get(position);
        if (listJsonObject != null) {
            TextView listJsonObjectNameLabel = (TextView) v.findViewById(R.id.text_suggestion);
            if (listJsonObjectNameLabel != null) {
                listJsonObjectNameLabel.setText(listJsonObject.getName() + "," + listJsonObject.getCountry());
            }
        }
        return v;
    }
    @Override
    public Filter getFilter() {
        return nameFilter;
    }
    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((ListJsonObject)(resultValue)).getName();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (null != constraint) {
                if (constraint.length() == 3) {
                    for (ListJsonObject listJsonObject : itemsAll) {
                        if (listJsonObject.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(listJsonObject);
                        }
                    }
                }
                if (constraint.length() >= 3) {
                    List<ListJsonObject> newData = new ArrayList<>(suggestions);
                    suggestions.clear();
                    for (ListJsonObject newList : newData) {
                        if (newList.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(newList);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                System.out.println("Counting new " + suggestions.size());
                return filterResults;
            } else {
                suggestions.clear();
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<ListJsonObject> filteredList = (List<ListJsonObject>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for(Iterator<ListJsonObject> data = filteredList.iterator(); data.hasNext();){
                    ListJsonObject obtainedJsonObject = data.next();
                    add(obtainedJsonObject);
                }
                notifyDataSetChanged();
            }
        }
    };
}
