package amitdohan.johnbryce.finalproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

public abstract class FullMovieController  implements HttpRequest.Callbacks {

    protected Activity activity;
    protected ProgressDialog progressDialog;

    public FullMovieController(Activity activity) {

        this.activity = activity;

        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Downloading...");
        progressDialog.setMessage("Please Wait...");

    }


    public void onAboutToStart() {
       progressDialog.show();
    }


    public void onError(String errorMessage) {
        Toast.makeText(activity, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
    }
}