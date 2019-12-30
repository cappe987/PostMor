package com.mdhgroup2.postmor.database.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utils {

    public static final String baseURL = "https://postmorwebserver20191230083106.azurewebsites.net";


    public static Date makeDate(int year, int month, int day){
        Calendar c = new GregorianCalendar();
        c.set(year, month, day);
        return c.getTime();
    }

    public static Date makeTime(int hour, int minute, int second){
        Calendar c = new GregorianCalendar();
        c.set(2019, 12, 19, hour, minute, second);
        return c.getTime();
    }

    public static Date makeDateTime(int year, int month, int day, int hour, int minute, int second){
        Calendar c = new GregorianCalendar();
        c.set(year, month, day, hour, minute, second);
        return c.getTime();
    }


    public static String getAuthToken(ManageDao dao){
        // Fetches and sets new auth token

        String email = dao.getUserEmail();
        String password = dao.getUserPassword();

        String data = String.format("{" +
                "\"email\" : \"%s\", " +
                "\"password\" : \"%s\"" +
                "}", email, password);

        try{
            JSONObject json = APIPost("Some URL", new JSONObject(data));
            String token = json.getJSONObject("json").getString("token"); // return new token
            dao.setAuthToken(token);
            return token;
        }
        catch (IOException e){
            return null;
        }
        catch (JSONException j){
            return null;
            // Failed to update key. Possibly offline.
        }

    }

    public static JSONObject APIPost(String url, JSONObject json) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String b = response.body().string();
            return new JSONObject(b);
        }
        catch (JSONException j){
            return null;
        }
    }

    public static JSONArray APIPostArray(String url, JSONObject json) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String b = response.body().string();
            return new JSONArray(b);
        }
        catch (JSONException j){
            return null;
        }
    }

    public class APIWorker extends Worker {

        public APIWorker(Context c, WorkerParameters params){
            super(c, params);
        }

        @NonNull
        @Override
        public Result doWork() {
            // Use Data.Builder() to pass in the json string.
            String url = getInputData().getString("url");
            String data = getInputData().getString("json");
            try {
                Utils.APIPost(url, new JSONObject(data));
            }
            catch (JSONException | IOException e){
                return Result.failure();
            }
            return Result.success();
        }
    }


}
