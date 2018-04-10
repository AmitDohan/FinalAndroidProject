package amitdohan.johnbryce.finalproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
{
    public static final int ANIMATION_DURATION = 250;
    private final ImageView mDestination, mFakeForError;
    private final String mUrl;
    private final ProgressBar mProgressBar;
    public Activity A;
    private boolean isBitmap;

    private Animation.AnimationListener mOutAnimationListener = new Animation.AnimationListener()
    {
        @Override
        public void onAnimationStart(Animation animation){

        }
        @Override
        public void onAnimationEnd(Animation animation){
            mProgressBar.setVisibility(View.GONE);
        }
        @Override
        public void onAnimationRepeat(Animation animation){

        }

    };

    private Animation.AnimationListener mInAnimationListener = new Animation.AnimationListener()
    {
        @Override
        public void onAnimationStart(Animation animation){
            if (isBitmap)
                mDestination.setVisibility(View.VISIBLE);
            else
                mFakeForError.setVisibility(View.VISIBLE);
        }
        @Override
        public void onAnimationEnd(Animation animation){

        }
        @Override
        public void onAnimationRepeat(Animation animation){

        }

    };



    public DownloadImageTask(Activity c ,LinearLayout l,Context context, ImageView destination, String url)
    {

        mDestination = destination;
        mUrl = url;
        A=c;

        mFakeForError = new ImageView(context);
        destination.setVisibility(View.GONE);
        FrameLayout layout = new FrameLayout(context);
        mProgressBar = new ProgressBar(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mProgressBar.setLayoutParams(params);
        FrameLayout.LayoutParams copy = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        copy.gravity = Gravity.CENTER;
        copy.width = dpToPx(160);
        copy.height = dpToPx(285);
        mFakeForError.setLayoutParams(copy);
        mFakeForError.setVisibility(View.GONE);
        layout.addView(mProgressBar);
        layout.addView(mFakeForError);
        mProgressBar.setIndeterminate(true);
        l.addView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    protected Bitmap doInBackground(String... urls)
    {
        String urlDisplay = mUrl;
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result)
    {
        AlphaAnimation in = new AlphaAnimation(0f, 1f);
        AlphaAnimation out = new AlphaAnimation(1f, 0f);
        in.setDuration(ANIMATION_DURATION * 2);
        out.setDuration(ANIMATION_DURATION);
        out.setAnimationListener(mOutAnimationListener);
        in.setAnimationListener(mInAnimationListener);
        in.setStartOffset(ANIMATION_DURATION);
        if (result != null) {
            mDestination.setImageBitmap(result);
            isBitmap = true;
            mDestination.startAnimation(in);

        } else {


            mDestination.setVisibility(View.VISIBLE);
            mDestination.setBackgroundResource(R.drawable.default_movie_pic);

        }
        mProgressBar.startAnimation(out);
        mProgressBar.setVisibility(View.GONE);
        mFakeForError.setVisibility(View.GONE);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mDestination.getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

}