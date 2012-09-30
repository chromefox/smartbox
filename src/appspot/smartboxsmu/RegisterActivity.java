package appspot.smartboxsmu;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import appspot.smartboxsmu.network.CustomProgressDialog;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.URL;
import appspot.smartboxsmu.network.Util;

public class RegisterActivity extends Activity {
	CustomProgressDialog dialog;

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
			dialog  = CustomProgressDialog.show(this, "", "Loading");
			POSTRequest post = new POSTRequest(this, dialog);
			post.execute(URL.REGISTER);
			break;
		}

	}

	/*
	 * ==========================================================================
	 * ======= NESTED CLASSES
	 * ====================================================
	 * =============================
	 */
	private class POSTRequest extends NetworkRequestFactory {
		public POSTRequest(Context context, CustomProgressDialog dialog) {
			super(context, dialog, true);
		}

		@Override
		public HttpUriRequest sendDataToServer(String url)
				throws JSONException, UnsupportedEncodingException {
			HttpPost post = getHttpPost(url);
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.STRICT);
			entity.addPart("content", new StringBody("random test"));
			post.setEntity(entity);

			return post;
		}

		@Override
		public void parseResponseFromServer(String result) {
			if (Integer.valueOf(result) == HttpStatus.SC_OK) {
				Util.alertToast(context, "Created");
			}
		}

		@Override
		public void additionalExceptionHandling() {
			// Alert the user of the error and stay on the page
			Util.alertToast(context, "error");
		}
	}

}
