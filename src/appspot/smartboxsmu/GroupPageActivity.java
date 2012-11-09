package appspot.smartboxsmu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import appspot.smartboxsmu.model.Group;

public class GroupPageActivity extends Activity {
	private Group group;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
        //Get the group parcel from the activity
        setGroup(getIntent());
    }
    
    private void setGroup(Intent intent) {
    	Bundle extra = intent.getExtras();
    	if(extra != null) {
    		group = extra.getParcelable("group");
    		//Set the view content depending on the group
    		setViewContent(group);
    	}
    }

    private void setViewContent(Group group) {
    	TextView tv;
    	StringBuilder sb = new StringBuilder();
    	tv = (TextView) findViewById(R.id.group_page_group_name);
    	tv.setText(group.getGroupName());
    	
    	tv = (TextView) findViewById(R.id.group_page_member_name);
    	for(int i = 0; i < group.getMemberNames().size(); i++) {
    		//if it is the last array, do not append comma
    		if(i + 1 != group.getMemberNames().size()) {
    			sb.append(group.getMemberNames().get(i) + ", ");
    		}
    	}
    	tv.setText(sb.toString());
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_group_page, menu);
        return true;
    }
}
