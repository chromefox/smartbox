package appspot.smartboxsmu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import appspot.smartboxsmu.network.URL;

public class HomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    public void onClickHandler(View view) {
    	Intent intent;
    	switch(view.getId()) {
    	case R.id.test:
    		//Go to demo page for now
    		intent = new Intent(this, DemoActivity.class);
    		startActivity(intent);
    		break;
    	case R.id.home_create_group:
    		intent = new Intent(this, AddGroupActivity.class);
    		startActivity(intent);
    		break;
    	case R.id.home_send_contact:
            //Send a contact Check 
            ContactCheckPOSTRequest post = new ContactCheckPOSTRequest(this);
            post.execute(URL.CONTACT_CHECK);
    		break;
    	}
    }
}
