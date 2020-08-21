package com.numerojyotish.Api;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.numerojyotish.Activity.BasicInfoActivity;
import com.numerojyotish.Interface.ApiInterface;
import com.numerojyotish.Utils.EndPoints;
import com.numerojyotish.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiClass implements ApiInterface
{
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private SessionManager session;




    public void getHomeData(Context context, final String from) {
        session = new SessionManager(context);
        progressDialog = new ProgressDialog(context);
        requestQueue = Volley.newRequestQueue(context);

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url= EndPoints.LOAD_DATA+"?dob="+session.getBasicDetails().get(SessionManager.KEY_DOB)+
                "&gender="+session.getBasicDetails().get(SessionManager.KEY_GENDER)+
                "&name="+session.getBasicDetails().get(SessionManager.KEY_NAME)+
                "&userName="+session.getLoginDetail().get(SessionManager.KEY_MOBILE_NO);


     //   String url= EndPoints.LOAD_DATA+"?dob="+"07/24/2010"+"&gender="+"Female"+"&name="+"tk";

        StringRequest postRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("response", response + "");
                        progressDialog.dismiss();

                        // {"Id":1,"Status":"Success"}
                        // {"id":1,"status":"success","WalletAmount":542.64}
                        try {
                            JSONObject obj = new JSONObject(response);


                                BasicInfoActivity.getInstance().runThread(response);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
        );
//        {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                //  params.put("dob", session.getBasicDetails().get(SessionManager.KEY_DOB));
//                params.put("dob", "12-02-1990");
//
//                params.put("gender",session.getBasicDetails().get(SessionManager.KEY_GENDER));
//                params.put("name", session.getBasicDetails().get(SessionManager.KEY_NAME));
//
//                Log.e("params", params + "");
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
 //       };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }


}
