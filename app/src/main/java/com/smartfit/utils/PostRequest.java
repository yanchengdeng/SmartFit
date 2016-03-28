/**
 * Copyright 2013 Mani Selvaraj
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartfit.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.smartfit.commons.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom implementation of Request<T> class which converts the HttpResponse obtained to Java class objects.
 * Uses GSON library, to parse the response obtained.
 * Ref - JsonRequest<T>
 *
 * @author Mani Selvaraj
 */

public class PostRequest extends Request<JsonObject> {

    /**
     * Charset for request.
     */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/x-www-form-urlencoded; charset=%s", PROTOCOL_CHARSET);

    private final Listener<JsonObject> mListener;

    private final Map<String, String> mRequestBody;

    private Gson mGson;

    public PostRequest(String method, Map<String, String> requestBody, Listener<JsonObject> listener,
                       ErrorListener errorListener) {
        super(Method.POST, Constants.Net.URL + method, errorListener);
        mGson = new Gson();
        mListener = listener;
        mRequestBody = requestBody;
        LogUtil.w("dyc", Constants.Net.URL+method);

        LogUtil.w("dyc", mRequestBody.toString());
    }

    @Override
    protected void deliverResponse(JsonObject response) {
        mListener.onResponse(response);
    }

    private Map<String, String> headers = new HashMap<>();

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }


    @Override
    protected Response<JsonObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            LogUtil.w("dyc", jsonString);
            try {
                JSONObject jb = new JSONObject(jsonString);
                if (jb.optString("stateCode").equals("1")) {//返回结果正确
                    JsonObject jsonObject = null;
                    if (jb.opt("data") instanceof JSONArray) {
                        jsonObject = new JsonObject();
                        jsonObject.addProperty("list", jb.optString("data"));
                    }

                    if (null != jsonObject) {
                        ResponseDataArray responseData = mGson.fromJson(jsonString, ResponseDataArray.class);
                        JsonObject object = new JsonObject();
                        object.add("list",responseData.getData());
                        return Response.success(object, HttpHeaderParser.parseCacheHeaders(response));
                    }else{
                        ResponseData responseData = mGson.fromJson(jsonString, ResponseData.class);
                        return Response.success(responseData.getData(), HttpHeaderParser.parseCacheHeaders(response));
                    }
                } else {
                    return Response.error(new VolleyError(jb.optString("stateMsg")));
                }
            } catch (JSONException e) {
                return Response.error(new VolleyError("稍后再试"));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException je) {
            return Response.error(new ParseError(je));
        }
    }

  /*  @Override
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded";
    }*/

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mRequestBody;
    }

    /* @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }*/

}
