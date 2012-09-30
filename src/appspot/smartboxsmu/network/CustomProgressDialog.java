package appspot.smartboxsmu.network;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * This class defines the default setting not provided by the default ProgressDialog's constructors.
 * @author Ronny
 *
 */
public class CustomProgressDialog extends ProgressDialog {

	public CustomProgressDialog(Context context) {
		super(context);
	}
	
	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}
	
    public static CustomProgressDialog show(Context context, CharSequence title,
            CharSequence message) {
    	CustomProgressDialog dialog = new CustomProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(null);
        dialog.show();
        return dialog;
    }
}
