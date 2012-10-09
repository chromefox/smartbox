package appspot.smartboxsmu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

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
    		
    		break;
    	}
    }
}
