package amitdohan.johnbryce.finalproject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Objects needed for using all the methods in the class
    private DataBase dataBase;
    private LinearLayout mainLayout;
    private List<MovieSample> names;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar));
        App.setContext(this); //Using it will help to declare startActivityForResult from another Activity.
        dataBase = new DataBase(this);
        mainLayout = findViewById(R.id.linearLayoutMain);;
            loadMovies();
    }

    // Inflating the menus
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);


        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.menuItemSettings:

                return true;
            case R.id.addInternet:
                Intent net = new Intent(this,InternetActivity.class);
                startActivity(net);
                return true;
            case R.id.addManuall:
                Intent add = new Intent(this,EditActivity.class);
                add.putExtra("id",-1);
                startActivityForResult(add,1);
                return true;
            case R.id.exit:
                finish();
                return true;
            case R.id.deleteAll:
                deleteAll();
                return true;

        }

        return false;
    }

    // Loading the data base
    public void loadMovies(){
        names =dataBase.getAllMovieList();//getting the list of movies from the data base
        int i =0;
            if (names.size() == i) {
               TextView empty = findViewById(R.id.empty);
                empty.setVisibility(View.VISIBLE);
            } else {
                while(i<names.size()) {
                String s = names.get(i).getMovieName();
                String u = names.get(i).getMoviePic();
                String d = names.get(i).getMovieDes();
                int id =names.get(i).getId();
                int No = names.get(i).getOrderNumber();
                int watched = names.get(i).getWatched();
                addMovie(s,d,u,id,No,watched);

                    i++;
            }
        }
    }

    // The method gets information and adds the movie accordingly
    public void addMovie(String name, String description , String url, int id, int orderNumber, int watched) {

        ImageView moviePic = new ImageView(this);
        TextView movieName = new TextView(this);
        TextView movieDes = new TextView(this);
        TextView hint = new TextView(this);
        TextView watchNumberTextView = new TextView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout innerLayout = new LinearLayout(this);
        LinearLayout movieImage = new LinearLayout(this);
        final Button goPageButton = new Button(this);
        Button watchButton = new Button(this);

        // Resizing the elements
        linearLayoutImageInsideResize(movieImage);
        buttonResize(goPageButton);
        linearLayoutInsideResize(innerLayout);
        textDesResize(movieDes);
        textViewWatchNumberResize(hint);
        hint.setText(getString(R.string.watch));
        linearLayoutResize(linearLayout);
        imageViewResize(moviePic);
        textViewResize(movieName);

        // Tick button and writing in the database
        watchButton.setTag(id);
        setAddWatch(watchButton,watched,watchNumberTextView);
        addPicture(moviePic, url);
        moviePic.setTag(name);
        goPageButton.setTag(orderNumber);
        goPageButton.setText(R.string.movie_page);
        goPageButton.setTextSize(13);
        goPageButton.setBackground(getDrawable(R.drawable.buttons));
        goPageButton.setTextColor(getColor(R.color.black));
        movieName.setText(name);
        movieDes.setText(description);

        // Calling the custom dialog
        final CustomDialogClass cdc = new CustomDialogClass(MainActivity.this, name, id);
        moviePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEdit(view);
            }
        });
        moviePic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                cdc.show();
                return false;
            }
        });

        goPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int orderNumber = (int)goPageButton.getTag();
                goToMoviePage(orderNumber);
            }
        });

        innerLayout.addView(movieName);
        innerLayout.addView(movieDes);

        if(orderNumber!=0) {
            innerLayout.addView(goPageButton);
        }

        movieImage.addView(moviePic);
        movieImage.addView(watchNumberTextView);
        movieImage.addView(watchButton);
        movieImage.addView(hint);
        linearLayout.addView(movieImage);
        linearLayout.addView(innerLayout);
        mainLayout.addView(linearLayout);

    }


    // Setting the movie picture (or a default picture if the movie doesn't have one)
    public void addPicture(ImageView b,String u) {
        if (u.equals("")) {
            b.setBackgroundResource(R.drawable.default_movie_pic);
            b.getBackground().setAlpha(150);
        } else {
                                 new DownloadImageTask(this, mainLayout,this, b, u).execute();
        }
    }

    // Starting the activity in which the movie details is shown
    public void goToMoviePage(int No){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Intent moviePage = new Intent(this,MoviePage.class);
            moviePage.putExtra("No",No);
            moviePage.putExtra("switch",0);
            startActivity(moviePage);
        }else{
            Toast.makeText(this,"there is no internet connection",Toast.LENGTH_SHORT).show();
        }



    }

    // Starting the activity that the user can edit the movie details in
    public void goToEdit(View v){
        String movieTitle = v.getTag().toString();

        //Sending the movie info. to the edit activity
        for(int i=0;i<names.size();i++){
            if(movieTitle.equals(names.get(i).getMovieName())){
                String title =names.get(i).getMovieName();
                String des =names.get(i).getMovieDes();
                String url =names.get(i).getMoviePic();
                int id = names.get(i).getId();

                Intent editActivity = new Intent(this,EditActivity.class);
                editActivity.putExtra("name",title);
                editActivity.putExtra("des",des);
                editActivity.putExtra("url",url);
                editActivity.putExtra("id",id);
                this.startActivityForResult(editActivity,1);

            }
        }
    }

    // Deleting all movies
    public void deleteAll(){
        dataBase.clear();
        restart();
    }
    public void restart(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finishAffinity();
    }


    // Getting the results of the activityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// getting the extras
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                int No=data.getIntExtra("No",0);
                String name=data.getStringExtra("name");
                String des=data.getStringExtra("des");
                String url1=data.getStringExtra("url");

                if(des.equals("")) {
                    if ( url1.equals("")) {
                        MovieSample movieSample = new MovieSample(name,"","",No);
                        movieSample.setWatched(0);
                        dataBase.addMovie(movieSample);
                    } else {
                        MovieSample movieSample = new MovieSample(name, "", url1,No);
                        movieSample.setWatched(0);
                        dataBase.addMovie(movieSample);
                    }
                }else if(url1.equals("")){
                        MovieSample movieSample = new MovieSample(name,des,"",No);
                    movieSample.setWatched(0);
                    dataBase.addMovie(movieSample);
                }else{
                    MovieSample movieSample = new MovieSample(name,des,url1,No);
                    movieSample.setWatched(0);
                    dataBase.addMovie(movieSample);
                }


                this.recreate();



            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    // Setting the appearance of the tick number and logo
    public void setAddWatch(final Button btn ,int watched,final TextView watchNumberTextView){

        textViewWatchNumberResize(watchNumberTextView);
        watchButtonResize(btn);
        if(watched!=0){
            btn.setBackground(getDrawable(R.drawable.ic_check_box_black_24dp));
        }
        String watchString = watched+"";
        watchNumberTextView.setText(watchString);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = Integer.parseInt(view.getTag().toString());
                addWatch(btn,watchNumberTextView,id);

            }
        });

        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int id = Integer.parseInt(view.getTag().toString());
                resetWatch(btn,watchNumberTextView,id);
                return false;
            }
        });

    }
    public void addWatch(Button b,TextView watchNumberTextView,int id){
        int watched=0;
        for(int p=0;p<names.size();p++){
            if(id==names.get(p).getId()){
                names.get(p).setWatched(names.get(p).getWatched()+1);
                watched = names.get(p).getWatched();
                dataBase.updateWatch(id);
            }
        }
        if(watched>0){
            b.setBackground(getDrawable(R.drawable.ic_check_box_black_24dp));
        }
        String watch = watched+"";
        watchNumberTextView.setText(watch);
    }
    public void resetWatch(Button b,TextView watchNumberTextView,int id){
        int watched=0;
        for(int p=0;p<names.size();p++){
            if(id==names.get(p).getId()){
                names.get(p).setWatched(0);
                watched = names.get(p).getWatched();
                dataBase.resetWatch(id);
            }
        }
        b.setBackground(getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp));
        String watch = watched+"";
        watchNumberTextView.setText(watch);
    }

    //Methods that resize the different views
    public void buttonResize(Button sv){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sv.setLayoutParams(positionRules);
        positionRules.setMargins(10, 10, 10, 10);
    }
    public void linearLayoutImageInsideResize(LinearLayout layoutImage){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutImage.setLayoutParams(positionRules);
        layoutImage.setGravity(Gravity.CENTER);
        layoutImage.setOrientation(LinearLayout.VERTICAL);
    }
    public void linearLayoutInsideResize(LinearLayout innerLayout){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        innerLayout.setLayoutParams(positionRules);
        innerLayout.getLayoutParams().height = 1050;
        positionRules.setMargins(25,0, 0, 5);
        innerLayout.setOrientation(LinearLayout.VERTICAL);
    }
    public void textDesResize(TextView des){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        des.setLayoutParams(positionRules);
        des.setTextColor(Color.BLACK);
        des.setTextSize(13);
        positionRules.setMargins(5,5, 5, 0);
        des.getLayoutParams().height = 425;
    }
    public void linearLayoutResize(LinearLayout linearLayout){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(positionRules);
        positionRules.setMargins(25, 25, 25, 25);
        linearLayout.setLayoutParams(positionRules);
        linearLayout.getLayoutParams().height = 1050;
        linearLayout.setBackgroundResource(R.drawable.layoutstyle);
    }
    public void textViewResize(TextView b){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        b.setLayoutParams(positionRules);
        b.setTextColor(Color.BLACK);
        b.setTextSize(25);
        positionRules.setMargins(15,0, 15, 15);
    }
    public void textViewWatchNumberResize(TextView b){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        b.setLayoutParams(positionRules);
        b.setTextSize(15);
        b.setTextColor(Color.BLACK);

    }
    public void watchButtonResize(Button sv){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sv.setLayoutParams(positionRules);
        sv.getLayoutParams().height = 80;
        sv.getLayoutParams().width = 80;
        sv.setBackground(getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp));
    }
    public void imageViewResize(ImageView b){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        b.setLayoutParams(positionRules);
        positionRules.setMargins(15, 15, 25, 0);
        b.getLayoutParams().height = 710;
        b.getLayoutParams().width = 400;
    }

}