package appspot.smartboxsmu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import org.joda.time.DateTime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import appspot.adapter.ChatMessageAdapter;
import appspot.adapter.FindDateResultAdapter;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.parcelable.ChatMessage;
import appspot.smartboxsmu.parcelable.FindSlotResult;

public class FindDateResultActivity extends Activity {
	private ListView findDateResultLV;
	private ArrayList<FindSlotResult> resultList;
	private ArrayList<DateTime> dateTimelist;
	private FindDateResultAdapter adapter;
	private TreeMap<DateTime, ArrayList<FindSlotResult>> map;
	private Group group;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_date_result);
        //Fetch the required objects
        findDateResultLV = (ListView) findViewById(R.id.find_date_result_listview);
        //Fetch the intent data
        setResultList(getIntent());
        //SetAdapter and events
        map = mapResult(resultList);
        dateTimelist = new ArrayList(map.keySet());
        adapter = new FindDateResultAdapter(this, R.id.add_group_button, R.id.add_group_button, dateTimelist);
        findDateResultLV.setAdapter(adapter);
        
        //Send the listview behaviour
        setupListView();
    }

	private void setupListView() {
		// Setting up onItemClickHandler
		findDateResultLV
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View item,
							int position, long id) {
						DateTime date = adapter.getItem(position);
						
						//send you to find slot time activity
						Intent intent = new Intent(FindDateResultActivity.this,
								FindSlotTimeActivity.class);
						intent.putExtra("slotResults", FindDateResultActivity.this.map.get(date));
						intent.putExtra("group", group);
						FindDateResultActivity.this.startActivity(intent);
					}
				});
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_find_date_result, menu);
        return true;
    }
    
    
    private void setResultList(Intent intent) {
    	Bundle extra = intent.getExtras();
    	if(extra != null) {
    		resultList = extra.getParcelableArrayList("findResults");
    		group = extra.getParcelable("group");
    	}
    }
    
  //Map each day to the corresponding FindSlotResult Object
  	public TreeMap<DateTime, ArrayList<FindSlotResult>> mapResult(List<FindSlotResult> results) {
  		TreeMap<DateTime, ArrayList<FindSlotResult>> map = new TreeMap<DateTime, ArrayList<FindSlotResult>>();
  		for (FindSlotResult result : results) {
  			// Contains
  			if (!map.containsKey((result.getDateTimeDay()))) {
  				ArrayList<FindSlotResult> sub = new ArrayList<FindSlotResult>();
  				// Implement sorting capability based on moment's date:
  				Collections.sort(sub, Collections
  						.reverseOrder(new Comparator<FindSlotResult>() {
  							public int compare(FindSlotResult m1,
  									FindSlotResult m2) {
  								return m1.getDateTimeDay().compareTo(
  										m2.getDateTimeDay());
  							}
  						}));
  				map.put(result.getDateTimeDay(), sub);
  			}
  			map.get(result.getDateTimeDay()).add(result);
  		}
  		return map;
  	}
}
