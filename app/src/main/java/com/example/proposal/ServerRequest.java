package com.example.proposal;

import android.util.Base64;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerRequest implements UserApi{
    private UserApi userApi;

    public Call<User> createUser(String auth_token, User user){
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("https://familiar-faces-service.azurewebsites.net/")
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserApi.class);
        Call<User> call = userApi.createUser(auth_token, user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                User postResponse = response.body();
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("Failure", "failed to reach api");
            }
        });
        return call;
    }
    /*
    static String baseUrl = "https://familiar-faces-service.azurewebsites.net/";
    public static void postTask(Event event) throws IOException {
        String name = event.getName();
        String description = event.getDescription();
        long schedule = event.getTime();
        int repeat = event.getRepeat();
        URL url = new URL(baseUrl + "api/todo/task");
        //InputStream response  = new URL("http://stackoverflow.com").openStream();
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        String boundary = UUID.randomUUID().toString();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type","application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"name\": \""+ name + "\", \"description\": \"" + description + "\",\"schedule\": " + schedule + ",\"repeat\": " + repeat + "}";
        con.setRequestProperty("Content-Length",  Integer.toString(jsonInputString.length()));
        try(DataOutputStream os = new DataOutputStream(con.getOutputStream())) {
            //byte[] input = jsonInputString.getBytes("utf-8");
            os.writeBytes(jsonInputString);
            os.flush();
            os.close();
            con.disconnect();
        }
    }
    */
}
