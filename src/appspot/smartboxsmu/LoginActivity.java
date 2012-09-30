package appspot.smartboxsmu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
    
    public void onClickHandler(View view) {
    	Intent intent;
		switch (view.getId()) {
		case R.id.login_button:
			//Make a call to the server to authenticate user
			
			break;
		case R.id.register_button:
			//Go to register page
			intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
		}
	}

    
}
