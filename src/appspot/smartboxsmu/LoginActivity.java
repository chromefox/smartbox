package appspot.smartboxsmu;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import appspot.smartboxsmu.network.CustomProgressDialog;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.URL;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.User;

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
			POSTRequest post = new POSTRequest(this);
			post.execute(URL.SIGN_IN);
			break;
		case R.id.register_button:
			//Go to register page
			intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
		}
	}
    
	/*
	 * ==========================================================================
	 * ======= NESTED CLASSES
	 * ====================================================
	 * =============================
	 */
	public class POSTRequest extends NetworkRequestFactory {
		public POSTRequest(Context context) {
			super(context, null, true);
			dialog = CustomProgressDialog.show(context, "", "Signing in");
			super.setCustomProgressDialog(dialog);
		}
		
		@Override
		public HttpUriRequest sendDataToServer(String url)
				throws JSONException, UnsupportedEncodingException {
			HttpPost post = getHttpPost(url);
			JSONObject obj = new JSONObject();
			obj.put("email", ((EditText)LoginActivity.this.findViewById(R.id.login_email)).getText().toString() );
			obj.put("password", ((EditText)LoginActivity.this.findViewById(R.id.login_password)).getText().toString());

			StringEntity se = new StringEntity(obj.toString());
			post.setEntity(se);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");
			
			return post;
		}

		@Override
		public void parseResponseFromServer(String result) {
			if (statusCode == HttpStatus.SC_OK) {
				//Store User Data returned from the server on Main Application global variable
				User user = new User();
				user.mapObject(result);
				MainApplication.user = user;
				
				//Switch to other activity
				Activity act = (Activity) context;
				Intent intent = new Intent(act, HomeActivity.class);
				intent.putExtra("user", user);
				act.startActivity(intent);
				//Destroy current Activity
				act.finish();
			} else {
				//Display error messages
				// Expected {error: String} JSON
				JSONObject obj;
				try {
					obj = new JSONObject(result);
					Util.alertToast(context, obj.getString("error"));
				} catch (JSONException e) {
					Log.e("Util", "JSON Parsing Error");
				}
			}
		}

		@Override
		public void additionalExceptionHandling() {
			//EMPTY
		}
	}


    
}
