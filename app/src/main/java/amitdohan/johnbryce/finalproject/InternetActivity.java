package amitdohan.johnbryce.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import java.util.List;

public class InternetActivity extends AppCompatActivity {

    public ListView lVMovies;
    public List<MovieSample> tempMovieSamples;
    public MoviesReaderController moviesReaderController;
    public Context mContext;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_from_internet);
        getSupportActionBar().hide();

        mContext = App.getContext();

        lVMovies = findViewById(R.id.listView);
        moviesReaderController = new MoviesReaderController(this);

        lVMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onclick_Send(i);
            }
        });

    }

    public void onclick_Search(View v){

        EditText nameText = findViewById(R.id.name);
        String name = nameText.getText().toString();
        moviesReaderController.readAllMovies(name);

    }

    public void onclick_Cancel(View v){
        finish();
    }

    public void onclick_Send(int i){

        String tempName = lVMovies.getAdapter().getItem(i).toString();
        String baseImageUrl = "http://image.tmdb.org/t/p/w185";

        tempMovieSamples = moviesReaderController.giveMovies();

        for(int j = 0; j< tempMovieSamples.size(); j++){
            if(tempName.equals(tempMovieSamples.get(j).getMovieName())){
                if(i==j){
                    String description = tempMovieSamples.get(j).getMovieDes();
                    String imageUrl = baseImageUrl + tempMovieSamples.get(j).getMoviePic();
                    int Num = tempMovieSamples.get(j).getOrderNumber();

                    Intent editActivity = new Intent(this,EditActivity.class);
                    editActivity.putExtra("No",Num);
                    editActivity.putExtra("title",tempName);
                    editActivity.putExtra("des",description);
                    editActivity.putExtra("url",imageUrl);
                    editActivity.putExtra("id",1);

                    if (mContext instanceof Activity) {
                        ((Activity) mContext).startActivityForResult(editActivity, 1);
                        finish();
                    }

                }
            }
        }
    }

}