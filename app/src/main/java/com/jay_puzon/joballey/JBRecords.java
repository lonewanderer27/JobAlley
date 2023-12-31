package com.jay_puzon.joballey;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.R.layout;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class JBRecords extends ListActivity {
    JBSQLiteDB Conn;
    ArrayList<String> ItemList;


    void refreshData () {
        Conn = new JBSQLiteDB(this);
        ItemList = Conn.GetRecords();

        if (ItemList.size() > 0) {
            setListAdapter(new ArrayAdapter<>(this, layout.simple_list_item_1, ItemList));
        } else {
            Toast.makeText(this, "No Records Found!!!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);

        // Get the data of the selected record
        String[] recordData = Conn.GetRecord(Conn.ItemsId.get(position));

        // Create an intent to call the next activity
        Intent CallEdit = new Intent(".JobAlleyEditRecord");

        // Pass the data to the next activity
        CallEdit.putExtra(JBSQLiteDB.PROF_ID, Conn.ItemsId.get(position));
        CallEdit.putExtra(JBSQLiteDB.PROF_FNAME, recordData[0]);
        CallEdit.putExtra(JBSQLiteDB.PROF_MNAME, recordData[1]);
        CallEdit.putExtra(JBSQLiteDB.PROF_LNAME, recordData[2]);

        // Call the next activity
        startActivity(CallEdit);
    }
}
