package com.fiuba.tallerii.lincedin.network;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class Query {

    private String baseUrl;
    private String resource = "";
    private Map<String, String> params = new HashMap<>();

    public Query(String baseUrl, String resource, Map<String, String> params) {
        this.baseUrl = baseUrl;
        this.resource = resource;
        this.params = params;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getResource() {
        return resource;
    }

    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public String toString() {
        String endpoint = baseUrl;
        if (!TextUtils.isEmpty(resource))
            endpoint += "/" + resource;
        if (!params.isEmpty()) {
            endpoint += "?";
            int i = 0;
            for (String paramName : params.keySet()) {
                if (i > 0)
                    endpoint += "&";
                String paramValue = params.get(paramName);
                endpoint += paramName + "=" + paramValue;
                i++;
            }
        }

        return endpoint;
    }

    public static class QueryBuilder {
        private String baseUrl;
        private String resource = "";
        private Map<String, String> params = new HashMap<>();

        public QueryBuilder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public QueryBuilder setResource(String resource) {
            this.resource = resource;
            return this;
        }

        public QueryBuilder setParams(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Query build() {
            return new Query(baseUrl, resource, params);
        }
    }
}
