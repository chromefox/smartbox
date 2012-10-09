package appspot.smartboxsmu;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import appspot.smartboxsmu.network.CustomProgressDialog;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.URL;
import appspot.smartboxsmu.network.Util;

public class RegisterActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	public void onClickHandler(View view) {
		switch (view.getId()) {
		case R.id.register_button:
			// Make a call to the server to create user			
			POSTRequest post = new POSTRequest(this);
			post.execute(URL.REGISTER);
			break;
		}
	}

	/*
	 * ==========================================================================
	 * NESTED CLASSES
	 * ====================================================
	 */
	private class POSTRequest extends NetworkRequestFactory {
		public POSTRequest(Context context) {
			super(context, null, true);
			dialog = CustomProgressDialog.show(context, "", "Registering");
			super.setCustomProgressDialog(dialog);
		}

		@Override
		public HttpUriRequest sendDataToServer(String url)
				throws JSONException, UnsupportedEncodingException {
			HttpPost post = getHttpPost(url);
			JSONObject obj = new JSONObject();
			obj.put("email", ((EditText)RegisterActivity.this.findViewById(R.id.register_email)).getText().toString() );
			obj.put("password", ((EditText)RegisterActivity.this.findViewById(R.id.register_password)).getText().toString());
			obj.put("mobileNumber", ((EditText)RegisterActivity.this.findViewById(R.id.register_mobileNo)).getText().toString());
			obj.put("name", ((EditText)RegisterActivity.this.findViewById(R.id.register_name)).getText().toString());

			StringEntity se = new StringEntity(obj.toString());
			post.setEntity(se);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");
			
			return post;
		}

		@Override
		public void parseResponseFromServer(String result) {
			if (super.statusCode == HttpStatus.SC_OK) {
				Util.alertToast(context, "Created");
			}
		}

		@Override
		public void additionalExceptionHandling() {
			// Alert the user of the error and stay on the page
			Util.alertToast(context, "Error");
		}
	}

}
