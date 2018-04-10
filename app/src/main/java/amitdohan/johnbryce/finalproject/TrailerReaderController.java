package amitdohan.johnbryce.finalproject;

import android.app.Activity;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrailerReaderController extends TrailerController {

    String trailer="";
    public Activity context;
    public int aSwitch;

    public TrailerReaderController(Activity activity) {
        super(activity);
        context = activity;
    }

    public void getTrailer(int no, int num) {

        aSwitch = num;
        HttpRequest httpRequest = new HttpRequest(this);
        httpRequest.execute("https://api.themoviedb.org/3/movie/"+no+"/videos?api_key=fdbafdad226138d461dcb4c9b2d663f5&language=en-US");

    }

    public void onSuccess(String downloadedText) {

        try {

            JSONObject jsonObject = new JSONObject(downloadedText);

            //Getting the info. from the json object
            int No = jsonObject.getInt("id");
            JSONArray results= jsonObject.getJSONArray("results");

            if(results.equals("")){
                trailer="";
            }else {
                JSONObject tr=results.getJSONObject(0);
                trailer = tr.getString("key");
            }
            String baseYouTube = "https://www.youtube.com/watch?v=";
            String video = baseYouTube + trailer;

            TrailerInfo trailerUrl = new TrailerInfo(video);
            MoviePage.setTrailer(trailerUrl);

        } catch (JSONException ex) {
            if (aSwitch == 1) {
                MainActivity mContext = (MainActivity) App.getContext();
                View v = App.getmView();

            }
        }
        progressDialog.dismiss();
    }

}