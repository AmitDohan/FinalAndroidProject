package amitdohan.johnbryce.finalproject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HttpRequest extends AsyncTask<String, Void, String> {

    public final static int GET = 1;
    public final static int POST = 2;

    private Callbacks callbacks;
    private String errorMessage = null;
    private int httpMethod;
    private MainActivity mContext;
    private View v;


    public HttpRequest(Callbacks callbacks) {
        this.callbacks = callbacks;
        this.httpMethod = GET;
    }


    protected void onPreExecute() {
        callbacks.onAboutToStart();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //Opening the connection to set requests and to get responds
            if (httpMethod == POST) {
                String parameters = params[1]; // Url Parameters
                byte[] parameterBytes = parameters.getBytes();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Length", Integer.toString(parameterBytes.length));
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.write(parameterBytes);
            }

            int httpStatusCode = connection.getResponseCode();

            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                //If the http is not ok the random repeats itself and gives another http URL
                mContext=(MainActivity)App.getContext();
                v = App.getmView();
                errorMessage = "a second";
                return null;
            }

            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String downloadedText = "";

            String oneLine = bufferedReader.readLine();

            while (oneLine != null) {
                downloadedText += oneLine + " ";
                oneLine = bufferedReader.readLine();
            }

            return downloadedText;
        } catch (IOException ex) {
            return "a second";
        } finally {
            if (bufferedReader != null) try {
                bufferedReader.close();
            } catch (Exception e) {
            }
            if (inputStreamReader != null) try {
                inputStreamReader.close();
            } catch (Exception e) {
            }
            if (inputStream != null) try {
                inputStream.close();
            } catch (Exception e) {
            }
        }
    }

    protected void onPostExecute(String downloadedText) {
        if (errorMessage == null)
            callbacks.onSuccess(downloadedText);
        else

            callbacks.onError(errorMessage);
    }

    public interface Callbacks {
        void onAboutToStart();

        void onSuccess(String downloadedText);

        void onError(String errorMessage);
    }
}