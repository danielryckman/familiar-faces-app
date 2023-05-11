package com.example.proposal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity {

    TextView selectImage;
    private static final int PICK_IMAGE_REQUEST = 9544;
    ImageView image;
    Uri selectedImage;
    String part_image;

    EditText title;

    EditText personInPic;

    EditText description;

    Spinner albums;

    private UploadImage uploadimage;
    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_upload);
        selectImage = findViewById(R.id.select_img);
        image = findViewById(R.id.img);
        title = findViewById(R.id.title);
        personInPic = findViewById(R.id.personinpic);
        description = findViewById(R.id.description);
        albums = findViewById(R.id.album);

        String[] albumItems = new String[]{"test", "create a new album"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, albumItems);
        albums.setAdapter(adapter);
    }

    // Method for starting the activity for selecting image from phone storage
    public void pick(View view) {
        verifyStoragePermissions(UploadActivity.this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Open Gallery"), PICK_IMAGE_REQUEST);
    }

    // Method to get the absolute path of the selected image from its URI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedImage = data.getData();                                                         // Get the image file URI
                String[] imageProjection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, imageProjection, null, null, null);
                if(cursor != null) {
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
                    part_image = ImageFilePath.getPath(getApplicationContext( ),selectedImage);
                            //spart_image = getPath(getApplicationContext( ),selectedImage);
                    //selectImage.setText(part_image);                                                        // Get the image file absolute path
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image.setImageBitmap(bitmap);                                                       // Set the ImageView with the bitmap of the image
                }
            }
        }
    }
    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    // Upload the image to the remote database
    public void uploadImage(View view) {
        File imageFile = new File(part_image);
        //File imageFile = new File("/storage/emulated/0/Download/dan1.jpg");
        PhotoPOJO photo = new PhotoPOJO();
        photo.setName(String.valueOf(title.getText()).replaceAll("\\s","") + System.currentTimeMillis());
        photo.setTitle(String.valueOf(title.getText()));
        photo.setDescription(String.valueOf(description.getText()));
        photo.setPersoninpic(String.valueOf(personInPic.getText()));
        photo.setPtype("upload");
        Gson gson = new Gson();
        String photoJson = gson.toJson(photo);
        // Create a file using the absolute path of the image
        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/mixed"), imageFile);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("photo", photoJson,RequestBody.create(MediaType.parse("application/json"), photoJson));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(),RequestBody.create(MediaType.parse("image/*"), imageFile));
        //MultipartBody.Part partPhotoObj = MultipartBody.Part.createFormData("photo", imageFile.getName(), reqBody);
        //GetImage api = RetrofitClient.getInstance().getAPI();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        uploadimage = retrofit.create(UploadImage.class);
        Call<ResponseBody> upload = uploadimage.uploadImage(imagePart,photoPart,MainActivity.currentUser.getId(), "test");
        upload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(UploadActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    Intent landing = new Intent(UploadActivity.this, LandingActivity.class);
                    landing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(landing);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UploadActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}