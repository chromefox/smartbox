package appspot.smartboxsmu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

public class TestWebActivity extends Activity {
	private WebView webView;

	@SuppressLint({"NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_web);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://elearn.smu.edu.sg");
		
		//Insert JS here to fill up form and execute clicks.
		
		

//		   String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";
//		   webView.loadData(customHtml, "text/html", "UTF-8");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_test_web, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClickHandler(View view) {
		switch (view.getId()) {
		case R.id.button1:
			// Get properties
			String name = ((EditText) findViewById(R.id.name)).getText()
					.toString();
			String password = ((EditText) findViewById(R.id.password))
					.getText().toString();

			// Try to inject property into webview and login

			break;
		}
	}

}
