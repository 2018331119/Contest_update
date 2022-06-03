package com.example.cpdiary;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class solve_api_request {
    Context context1;
    public  solve_api_request(Context context)
    {
        this.context1=context;
    }
    public interface VolleyResponseListener{
        void onError(String message);
        void onResponse(JSONObject object);

    }
    public void api_request(String url,VolleyResponseListener volleyResponseListener)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyResponseListener.onResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyResponseListener.onError("Something wrong in api call");
                    }
                });
        MySingleton.getInstance(context1).addToRequestQueue(jsonObjectRequest);
    }

    public interface VolleyResponseListener1{
        void onError(String message);
        void onResponse(JSONArray array);

    }
    public void api_request(String url,VolleyResponseListener1 volleyResponseListener)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        volleyResponseListener.onResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyResponseListener.onError("Something wrong in api call");
                    }
                });
        MySingleton.getInstance(context1).addToRequestQueue(jsonArrayRequest);
    }


}