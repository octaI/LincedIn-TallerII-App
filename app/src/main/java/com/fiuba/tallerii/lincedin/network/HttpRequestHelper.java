package com.fiuba.tallerii.lincedin.network;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

/**
 * The class wraps the instantiation of JsonObjectRequest{@link JsonObjectRequest}s and the way that Volley library uses them for sending asynchronous HTTP requests.
 */
public class HttpRequestHelper {

    private static RequestQueue mRequestQueue;

    /**
     * Creates RequestQueue{@link RequestQueue} for sending asynchronous HTTP requests with Volley.
     * The methods will use this queue as a Singleton.
     * @param appContext is the application context
     */
    public static void initialize(Context appContext) {
        if (mRequestQueue != null)
            mRequestQueue = Volley.newRequestQueue(appContext);
    }

    /**
     * Adds to the RequestQueue{@link RequestQueue} the request to perform.
     * @param method is the HTTP Method
     * @param url is the url pointed by the request
     * @param requestParams are the key-value parameters added to the url
     * @param jsonRequest is the JSON body of the request
     * @param listener is the listener that triggers the actions to perform when the response is successful
     * @param errorListener is the listener that triggers the actions to perform when the response fails
     * @param requestTag the tag associated to the request to enqueue
     * @return the enqueued Request{@link Request}
     */
    private static Request<JSONObject> enqueue(int method, String url, @Nullable final Map<String, String> requestParams, JSONObject jsonRequest,
                                               Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, String requestTag) {
        JsonObjectRequest request = new JsonObjectRequest(method, url, jsonRequest, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                return requestParams;
            }
        };
        request.setTag(requestTag);
        return mRequestQueue.add(request);
    }

    /**
     * Cancels all the pending requests with the specified tag.
     * @param tag is the tag associated to the requests that must be canceled
     */
    public static void cancelPendingRequests(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    /**
     * Enqueues a POST request.
     * @param url is the url pointed by the request
     * @param requestParams are the key-value parameters added to the url
     * @param jsonRequest is the JSON body of the request
     * @param listener is the listener that triggers the actions to perform when the response is successful
     * @param errorListener is the listener that triggers the actions to perform when the response fails
     * @param requestTag the tag associated to the request to enqueue
     * @return the enqueued Request{@link Request}
     */
    public static Request<JSONObject> post(String url, @Nullable final Map<String, String> requestParams, JSONObject jsonRequest,
                       Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, String requestTag) {
        return enqueue(Method.POST, url, requestParams, jsonRequest, listener, errorListener, requestTag);
    }

    /**
     * Enqueues a PUT request.
     * @param url is the url pointed by the request
     * @param requestParams are the key-value parameters added to the url
     * @param jsonRequest is the JSON body of the request
     * @param listener is the listener that triggers the actions to perform when the response is successful
     * @param errorListener is the listener that triggers the actions to perform when the response fails
     * @param requestTag the tag associated to the request to enqueue
     * @return the enqueued Request{@link Request}
     */
    public static Request<JSONObject> put(String url, @Nullable final Map<String, String> requestParams, JSONObject jsonRequest,
                                          Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, String requestTag) {
        return enqueue(Method.PUT, url, requestParams, jsonRequest, listener, errorListener, requestTag);
    }

    /**
     * Enqueues a GET request.
     * @param url is the url pointed by the request
     * @param requestParams are the key-value parameters added to the url
     * @param listener is the listener that triggers the actions to perform when the response is successful
     * @param errorListener is the listener that triggers the actions to perform when the response fails
     * @param requestTag the tag associated to the request to enqueue
     * @return the enqueued Request{@link Request}
     */
    public static Request<JSONObject> get(String url, @Nullable final Map<String, String> requestParams, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, String requestTag) {
        return enqueue(Method.GET, url, requestParams, null, listener, errorListener, requestTag);
    }

    /**
     * Enqueues a DELETE request.
     * @param url is the url pointed by the request
     * @param requestParams are the key-value parameters added to the url
     * @param listener is the listener that triggers the actions to perform when the response is successful
     * @param errorListener is the listener that triggers the actions to perform when the response fails
     * @param requestTag the tag associated to the request to enqueue
     * @return the enqueued Request{@link Request}
     */
    public static Request<JSONObject> delete(String url, @Nullable final Map<String, String> requestParams, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, String requestTag) {
        return enqueue(Method.DELETE, url, requestParams, null, listener, errorListener, requestTag);
    }
}
