package amitdohan.johnbryce.finalproject;

import android.app.Activity;
import android.app.ProgressDialog;

public class TrailerController implements HttpRequest.Callbacks {
    protected Activity activity;
    protected ProgressDialog progressDialog;

    public TrailerController(Activity activity) {
        this.activity = activity;

        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Downloading...");
        progressDialog.setMessage("Please Wait...");

    }

    public void onAboutToStart() {
        progressDialog.show();
    }

    public void onSuccess(String downloadedText) {

    }

    public void onError(String errorMessage) {

    }
}
