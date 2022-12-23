package com.example.proposal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.content.Context;
import android.widget.ImageView;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.util.Base64;
import android.content.ContextWrapper;

public class MainActivity extends AppCompatActivity {
    private ImageView imageViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageViewResult = findViewById(R.id.image_view_result);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.4.173:7860/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        createPost();
    }
    private void createPost() {
        Post post = new Post("puppy cat", 5);
        Call<Post> call = jsonPlaceHolderApi.createPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                Post postResponse = response.body();
                String content = "";
                content += postResponse.getImages()[0];
                byte[] decodedBytes = android.util.Base64.decode(content, android.util.Base64.DEFAULT);
                writeToFile("file.jpg", decodedBytes);
                Log.i("write", "finished");
                viewImage("file.view");
                Log.i("view", "finished");
                int c = response.code();
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t){
                Log.i("Failure", "failedtocallpost");
            }
        });
    }
    public void writeToFile(String fileName, byte[] content){
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, fileName));
            writer.write(content);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
    public void viewImage(String fileName){
        Context context = this;
        String path = context.getFilesDir() + "/" + "file.jpg";
        Log.i("path", path);
        File newImage = new File(path);
        if (newImage.exists()){
            Bitmap decoded_bitmap = BitmapFactory.decodeFile(path);
            imageViewResult.setImageBitmap(decoded_bitmap);
            Log.i("image", "success");
        }
        else{
            Log.i("image", "failed");
        }
    }

}