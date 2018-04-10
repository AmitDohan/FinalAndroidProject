package amitdohan.johnbryce.finalproject;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Locale;

public class FullMoviesReaderController extends FullMovieController{
    public Activity context;
    public int aSwitch;

    public FullMoviesReaderController(Activity activity) {
        super(activity);
        context = activity;
    }

    public void getFullMovie(int no, int num) {

        aSwitch = num;
        HttpRequest httpRequest = new HttpRequest(this);
        Locale locale = Locale.getDefault();
        String locale2=locale.toString();
        if(locale2.equals("iw_IL")) {
            httpRequest.execute("https://api.themoviedb.org/3/movie/" + no + "?api_key=fdbafdad226138d461dcb4c9b2d663f5&language=he");
        }else{
            httpRequest.execute("https://api.themoviedb.org/3/movie/" + no + "?api_key=fdbafdad226138d461dcb4c9b2d663f5");
        }
    }


    public void onSuccess(String downloadedText) {

        try {

            JSONObject jsonObject = new JSONObject(downloadedText);
            int uselessInt = 0;
            int No = jsonObject.getInt("id");
            String name = jsonObject.getString("title");
            String desc = jsonObject.getString("overview");
            String poster_path = jsonObject.getString("poster_path");
            String baseImageUrl = "http://image.tmdb.org/t/p/w185";
            String image = baseImageUrl + poster_path;
            int budget = jsonObject.getInt("budget");
            int runtime;
            try {
                runtime = jsonObject.getInt("runtime");
            }catch (Exception eee) {
                runtime = 0;
            }
            String release_date = jsonObject.getString("release_date");
            String va = jsonObject.getString("vote_average");
            float vote_average = Float.parseFloat(va);

            FullMovieSampleInfo movie = new FullMovieSampleInfo(uselessInt, name, desc, image, No, vote_average, release_date, budget, runtime);

            MoviePage.setAllDetails(movie);


        } catch (JSONException ex) {

            if (aSwitch == 1) {

                MainActivity mContext = (MainActivity) App.getContext();
                View v = App.getmView();

            }
            Toast.makeText(activity, "Give me a second", Toast.LENGTH_LONG).show();//for my own use to see if there is an error while running
        }

        progressDialog.dismiss();

    }
}