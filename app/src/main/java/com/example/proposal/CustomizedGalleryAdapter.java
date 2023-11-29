package com.example.proposal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomizedGalleryAdapter extends BaseAdapter {

    private final Context context;
    private final PhotoPOJO[] images;

    public CustomizedGalleryAdapter(Context c, PhotoPOJO[] images) {
        context = c;
        this.images = new PhotoPOJO[images.length];
        for(int i=0; i< images.length; i++){
            this.images[i] = images[i];
        }
        //this.images = images;
    }

    // returns the number of images, in our example it is 10
    public int getCount() {
        return images.length;
    }

    // returns the Item  of an item, i.e. for our example we can get the image
    public Object getItem(int position) {
        return position;
    }

    // returns the ID of an item
    public long getItemId(int position) {
        return position;
    }

    // returns an ImageView view
    public View getView(int position, View convertView, ViewGroup parent) {
        // position argument will indicate the location of image
        // create a ImageView programmatically
        ImageView imageView = new ImageView(context);

        // set image in ImageView
        //imageView.setImageResource(images[position]);
        imageView.setImageBitmap(getBitmapFromString(images[position].getImage(0, 999999999,MainActivity.userid)));

        // set ImageView param
        imageView.setLayoutParams(new Gallery.LayoutParams(200, 200));
        return imageView;
    }

    public static Bitmap getBitmapFromString(String image){
        byte[] decodedBytes = android.util.Base64.decode(image, Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, options);
        return bmp;
    }
    // This method will be generating the Bitmap Object from the URL of image.
    public static Bitmap getBitmapFromURL(String src) {
        final Bitmap[] bmp = new Bitmap[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bmp[0] = BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                    bmp[0] = null;
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bmp[0];
    }

}