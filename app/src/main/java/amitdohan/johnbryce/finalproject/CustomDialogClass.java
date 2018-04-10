package amitdohan.johnbryce.finalproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import java.util.List;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Button delete, edit;
    public String name;
    public int id;
    DataBase db;
    List<MovieSample> names;

    public CustomDialogClass(Activity a,String name,int id) {
        super(a);
        this.name =name;
        this.id =id;
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.edit_dialog);
        delete =  findViewById(R.id.btn_delete);
        edit = findViewById(R.id.btn_edit);
        delete.setOnClickListener(this);
        edit.setOnClickListener(this);
        db= new DataBase(c);
        names=db.getAllMovieList();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                db.deleteMovie(id);
                c.recreate();
                break;
            case R.id.btn_edit:

                for(int i=0;i<names.size();i++){
                    if(name.equals(names.get(i).getMovieName())){
                        String title =names.get(i).getMovieName();
                        String des =names.get(i).getMovieDes();
                        String url =names.get(i).getMoviePic();
                        int id = names.get(i).getId();
                        int No = names.get(i).getOrderNumber();

                        Intent editActivity = new Intent(c,EditActivity.class);
                        editActivity.putExtra("No",No);
                        editActivity.putExtra("name",title);
                        editActivity.putExtra("des",des);
                        editActivity.putExtra("url",url);
                        editActivity.putExtra("id",id);
                        c.startActivityForResult(editActivity,1);
                        break;
                    }
            }


            default:
                break;
        }
        dismiss();
    }
}