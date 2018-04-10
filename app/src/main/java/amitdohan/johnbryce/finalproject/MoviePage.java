package amitdohan.johnbryce.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.Locale;

public class MoviePage extends AppCompatActivity{


    public FullMoviesReaderController fullMoviesReaderController;
    public TrailerReaderController trailerReaderController;

    static LinearLayout layout, movieLinear;
    static TextView head, vote, date, budget, runtime, description;
    static ImageView image;
    static RatingBar rate;
    static WebView trailer;
    static Activity activity;
    static Context context;
    static String trUrl, movieName, movieScore;
    static Button trailBut;
    int aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_page);
        getSupportActionBar().hide();

        movieLinear=findViewById(R.id.fullMovie);
        trailBut=findViewById(R.id.trailbut);
        fullMoviesReaderController = new FullMoviesReaderController(this);
        trailerReaderController = new TrailerReaderController(this);
        Intent i = getIntent();
        int No =i.getIntExtra("No",0);
        aSwitch =i.getIntExtra("switch",0);

        fullMoviesReaderController.getFullMovie(No,aSwitch);
        trailerReaderController.getTrailer(No,aSwitch);

        layout =  findViewById(R.id.l);
        head = findViewById(R.id.title);
        description = findViewById(R.id.description);
        image = findViewById(R.id.imageView);
        vote = findViewById(R.id.vote_average);
        rate = findViewById(R.id.MyRating);
        date = findViewById(R.id.release_date);
        budget = findViewById(R.id.budget);
        runtime = findViewById(R.id.runtime);
        trailer = findViewById(R.id.trailer);
        activity = this;
        context = this;
        trailer.setBackground(context.getResources().getDrawable(R.drawable.default_movie_pic));

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(aSwitch==1) {
            finish();
        }
    }

    public static void setAllDetails(FullMovieSampleInfo movie){

        head.setText(movie.getMovieName());
        description.setText(movie.getMovieDes());

        if (movie.getMoviePic().equals("")) {
            image.setBackgroundResource(R.drawable.default_movie_pic);
            image.getBackground().setAlpha(150);
        } else {
            new DownloadImageTask(activity, layout,context, image, movie.getMoviePic()).execute();
        }

        String voteText;
        if(0==movie.getVote_average()){
            rate.setVisibility(View.GONE);
            vote.setVisibility(View.VISIBLE);
            voteText = "There's no information on this detail";
        }else {
            voteText = movie.getVote_average()+"";
            if(7<=movie.getVote_average()){
                movieLinear.setBackgroundResource(R.drawable.layoutstylegreen);
            }else{
                movieLinear.setBackgroundResource(R.drawable.layoutstylered);
            }
        }

        vote.setText("Score: "+voteText);
        rate.setNumStars(5);
        rate.setMax(5);
        float rating = (float) 0.5*movie.getVote_average();
        rate.setStepSize((float)0.05);
        rate.setRating(rating);
        Locale locale = Locale.getDefault();
        String locale2=locale.toString();
        if(locale2.equals("iw_IL")) {
            date.setText("תאריך הפצה: " + movie.getRelease_date());
        }else {
            date.setText("Release date: " + movie.getRelease_date());
        }
        String money;
        if(0==movie.getBudget()) {
            if (locale2.equals("iw_IL")) {
                money = "לא נימצא תקציב לסרט זה :";
            } else {
                money = "There's no information on this detail";
            }
        }
        else {
            money = movie.getBudget() + "";
        }
        if(locale2.equals("iw_IL")) {
            budget.setText("תקציב: " + money);
        }else {
            budget.setText("Budget: " + money);
        }
        if(0!=movie.getRuntime()) {
            int hours = movie.getRuntime() / 60;
            int minutes = movie.getRuntime() % 60;
            if(locale2.equals("iw_IL")) {
                runtime.setText("אורך הסרט: " + hours + " שעות ו" + minutes + " דקות");
            }else {
                runtime.setText("Movie length: " + hours + " hours and " + minutes + " minutes");
            }
        }else{
            if(locale2.equals("iw_IL")) {
                runtime.setText("אורך הסרט: לא נימצא אורך על סרט זה :");
            }else {
                runtime.setText("Movie length: there's no information on this detail");
            }
        }

        movieName = movie.getMovieName().toString();
        movieScore = movie.getVote_average()+"";
    }

    public void share(View v){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "You just have to see the movie "+movieName+", the movie's score is "+movieScore+"!!!");
        sendIntent.setType("text/plain");
        startActivity(sendIntent.createChooser(sendIntent, getResources().getText(R.string.app_name)));
    }

    public  static void setTrailer(final TrailerInfo trailerUrl){
        trUrl=trailerUrl.getTrailer();
        if(trUrl.equals(""))
            trailBut.setVisibility(View.GONE);
    }

    public void webViewClick(View v){
        trailer.loadUrl(trUrl);
    }

}
