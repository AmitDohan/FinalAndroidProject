package amitdohan.johnbryce.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    EditText movieName, movieDes, moviePic;
    LinearLayout layout;
    ImageView imageView;
    int id, check, num= 0;
    DataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().hide();

        imageView = findViewById(R.id.imageView);
        movieName = findViewById(R.id.name);
        movieDes =  findViewById(R.id.description);
        moviePic =  findViewById(R.id.url);
        layout =  findViewById(R.id.outside);
        database = new DataBase(this);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null)
        {

            String NAME =(String) b.get("name");
            String d =(String) b.get("des");
            String u =(String) b.get("url");
            int id = (int) b.get("id");
            this.id =id;

            if(null!=b.get("No")){
                num = (int) b.get("No");
            }

            if(NAME==null){
                check=0;
                NAME=(String) b.get("title");
            }else{
                check=-1;
            }

            movieName.setText(NAME);
            movieDes.setText(d);
            moviePic.setText(u);
        }
    }

    public void show(View v){

        String url = moviePic.getText().toString();
        new DownloadImageTask(this, layout,this, imageView, url).execute();

    }

    public void onClick_ok(View v){

        DataBase db = new DataBase(this);
        if(check==-1){
            db.deleteMovie(id);
        }

        Intent returnIntent = new Intent();
        String name = movieName.getText().toString();
        List<MovieSample> m =db.getAllMovieList();
        boolean nameExist = true;

        for(int i=0;i<m.size();i++){
            if(name.equals(m.get(i).getMovieName())) {
                nameExist = false;
            }
        }

        if(nameExist) {
            String des = movieDes.getText().toString();
            String url = moviePic.getText().toString();
            if (name.equals("")) {
                Toast.makeText(this, getResources().getText(R.string.toast), Toast.LENGTH_SHORT).show();
            } else {
                returnIntent.putExtra("No", num);
                returnIntent.putExtra("name", name);
                returnIntent.putExtra("des", des);
                returnIntent.putExtra("url", url);
                setResult(Activity.RESULT_OK, returnIntent);
                num=0;
                finish();
            }
        }else{
            Toast.makeText(this,"This movie title is taken!",Toast.LENGTH_SHORT).show();
        }

    }

    public void onClick_Cancel(View v){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

}
