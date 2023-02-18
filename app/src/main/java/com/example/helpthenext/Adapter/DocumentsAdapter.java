package com.example.helpthenext.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.helpthenext.MapActivity;
import com.example.helpthenext.R;

import java.util.List;
import java.util.Map;

public class DocumentsAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private Map<String, List<String>> _listDataChild;

    public DocumentsAdapter(Context context, List<String> listDataHeader, Map<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).toString();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.document_desc);
        Button locationButton = (Button) convertView.findViewById(R.id.location_button);

        txtListChild.setText(childText);
        locationButton.setOnClickListener(view -> {
            Intent searchIntent = new Intent(parent.getContext(), MapActivity.class);
            if(_listDataChild.get("Claim asylum").get(0).contains("Portugal")){
                searchIntent.putExtra("search", "SEF");
            }
            else if(_listDataChild.get("Claim asylum").get(0).contains("Spain")){
                searchIntent.putExtra("search", "Oficina de Asilo y Refugio");
            }
            else if(_listDataChild.get("Claim asylum").get(0).contains("United Kingdom")){
                searchIntent.putExtra("search", "Lunar House");
            }
            parent.getContext().startActivity(searchIntent);
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.document);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
