package my.academic.design.App;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import my.academic.design.Activities.MainActivity;
import my.academic.design.Activities.ReportActivity;
import my.academic.design.Models.User;

/**
 * Functionality class for handling the functionalitites like
 * login, registration, authentication, fingerprint registration and identifications
 * developed by Anuj Kumar on 19, December, 2016
 */

public class Functionality {

    /**
     * @param context
     * @param username
     * @param password
     */
    public static void Login(final Context context,String username,String password)
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put("username",username.toString());
        map.put("password",password.toString());
        JSONObject inputs = new JSONObject(map);
        String _url = Project.getInstance().getBaseUrl(true,false).appendEncodedPath(Environment.LOGIN).toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,_url, inputs, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    //if has status variable and it is true then the successfully got the result else
                    // there is some error occurred with request
                    if(response.has("success") && response.getBoolean("success"))
                    {
                        JSONObject data = response.getJSONObject("data");
                        try{
                            if(data.has("token") && data.has("user")) {

                                JSONObject u = data.getJSONObject("user");

                                User user = new User(data.getString("token"),u.getString("name"),u.getString("email"),u.optString("username"),u.optString("type"));

                                Project.getInstance().getPref().storeUser(user);

                                Intent intent = new Intent(context,MainActivity.class);

                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);

                                context.startActivity(intent);

                            }
                        } catch (JSONException exe)
                        {
                            exe.printStackTrace();
                            Toast.makeText(context, "Invalid Response Type, Inner Try", Toast.LENGTH_SHORT).show();

                        }
                    }

                    if(response.has("error") && response.getBoolean("error"))
                    {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex){
                    Log.d("Reposnse type",response.toString());
                    Toast.makeText(context, "Invalid Response Type, Please correct the response", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error response",error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        Project.getInstance().getRequestQueue().add(jsonObjectRequest);

    }

    /**
     * Method to trigger the registration functionality
     * It will take you to the main activity
     * @param context
     * @param username
     * @param password
     */
    public static void Register(final Context context,String username,String password,String email,String fname,String lname)
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put("username",username.toString());
        map.put("password",password.toString());
        map.put("email",email.toString());
        map.put("first_name",fname.toString());
        map.put("last_name",lname.toString());
        JSONObject inputs = new JSONObject(map);
        String _url = Project.getInstance().getBaseUrl(true,false).appendEncodedPath(Environment.REGISTER).toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,_url, inputs, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    //if has status variable and it is true then the successfully got the result else
                    // there is some error occurred with request
                    if(response.has("success") && response.getBoolean("success"))
                    {

                    }
                    Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException ex){
                    Log.d("Reposnse type",response.toString());
                    Toast.makeText(context, "Invalid Response Type, Please correct the response", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        Project.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * Method to trigger the fingerprint matching functionality
     * If fingerprint match it will trigger the authenticate url from environemnt
     * else if it will trigger the report url
     * @param context
     * @param bmp
     * @param up
     */
    public static void MatchFingerPrint(final Context context, final Bitmap bmp,final boolean up)
    {
        JSONObject matchResult;
        Toast.makeText(context, "I AM IN TESTING FUNCTIONALITY", Toast.LENGTH_SHORT).show();
        Map<String,String> map = new HashMap<String,String>();
        map.put("fingerprint",bmp.toString());
        JSONObject inputs = new JSONObject(map);
        String _url = Environment.IDENTIFICATIONURL;
        Toast.makeText(context, _url, Toast.LENGTH_SHORT).show();

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, _url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Match Response",resultResponse);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    if(result.has("success") && result.getBoolean("success") && result.has("collegeid") && result.getInt("collegeid") !=0)
                    {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("collegeid",result.getString("collegeid"));
                        JSONObject inputs = new JSONObject(map);
                        //one to be used for main project
                        //String _url = Project.getInstance().getBaseUrl(true,false).appendEncodedPath(Environment.REPORT).toString();

                        //to be used with only report url
                        String _url = Project.getInstance().getBaseUrl(true,false).appendEncodedPath(Environment.REPORT).toString();

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, null, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                Log.d("REPORT RESULT",response.toString());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {

                            }
                        });
                    }

                    if(result.has("error") && result.getBoolean("error"))
                    {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message+" Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message+ " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message+" Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                params.put("myfile", new DataPart("file_avatar.jpg", byteArrayOutputStream.toByteArray(), "image/jpeg"));

                return params;
            }
        };

        Project.getInstance().getRequestQueue().add(multipartRequest);
    }

    /**
     * Method to trigger the fingerprint entering functionality to the system
     * If fingerprint match it will trigger the authenticate url from environemnt
     * else if it will trigger the report url
     *
     * @param context
     * @param bmp
     * @param id
     */
    public static void SetFingerPrint(final Context context, final Bitmap bmp,final String id)
    {
        String _url = Environment.UPLOADFINGERPRINTURL;

        //volley multipart request for uploading fingerprint and setting
        //it will take appropriate actions

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, _url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Match Response",resultResponse);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message+" Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message+ " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message+" Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("collegeid", id.toString());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                params.put("myfile", new DataPart("file_avatar.jpg", byteArrayOutputStream.toByteArray(), "image/jpeg"));
                return params;
            }
        };
        Project.getInstance().getRequestQueue().add(multipartRequest);
    }
}