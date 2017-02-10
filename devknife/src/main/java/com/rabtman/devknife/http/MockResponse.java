package com.rabtman.devknife.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockResponse {

    private static volatile MockResponse sInst = null;
    private HashMap<String, Object> mockDatas;

    //default value
    private Protocol protocol = Protocol.HTTP_1_0;
    private String[] defaultHeader = {"content-type", "application/json; charset=utf-8"};
    private HashMap<String, String> headerInfos = new HashMap<>();
    private Headers defaultHeaders;
    private MediaType mediaType = MediaType.parse("application/json");

    private MockResponse() {
        if (mockDatas == null) {
            mockDatas = new HashMap<>();
        }
    }

    public static MockResponse getInstance() {
        MockResponse inst = sInst;
        if (inst == null) {
            synchronized (MockResponse.class) {
                inst = sInst;
                if (inst == null) {
                    inst = new MockResponse();
                    sInst = inst;
                }
            }
        }
        return inst;
    }

    public void init(OkHttpClient client) {
        client.newBuilder()
                .addInterceptor(new MockResponseInterceptor())
                .build();
    }

    public void add(String url, String data) {
        mockDatas.put(url, data);
    }

    public void add(String url, ResponseBody data) {
        mockDatas.put(url, data);
    }

    public void add(String url, Response data) {
        mockDatas.put(url, data);
    }

    public void remove(String url) {
        mockDatas.remove(url);
    }

    public HashMap<String, Object> getMockDatas() {
        return mockDatas;
    }

    //build mock headers
    private Headers buildHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headerInfos.size() > 0) {
            for (Map.Entry<String, String> header : headerInfos.entrySet()) {
                headerBuilder.add(header.getKey(), header.getValue());
            }
        } else {//default content type : json
            headerBuilder.add(defaultHeader[0], defaultHeader[1]);
        }
        return headerBuilder.build();
    }

    public HashMap<String, String> getHeaders() {
        return headerInfos;
    }

    public void setHeaders(HashMap<String, String> headers) {
        defaultHeaders = null;
        this.headerInfos = headers;
    }

    private final class MockResponseInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            Response response = null;

            String url = request.url().url().toString();

            if (mockDatas.containsKey(url)) {
                Object mockData = mockDatas.get(url);

                if (defaultHeaders == null) {
                    defaultHeaders = buildHeaders();
                }

                if (mockData instanceof Response) {
                    return (Response) mockData;
                } else if (mockData instanceof ResponseBody) {
                    response = new Response.Builder()
                            .code(200)
                            .message("OK")
                            .protocol(protocol)
                            .headers(defaultHeaders)
                            .body((ResponseBody) mockData)
                            .build();
                    mockDatas.put(url, response);
                } else {
                    response = new Response.Builder()
                            .code(200)
                            .message("OK")
                            .protocol(protocol)
                            .headers(defaultHeaders)
                            .body(ResponseBody.create(mediaType, (String) mockData))
                            .build();
                    mockDatas.put(url, response);
                }
            } else {
                response = chain.proceed(request);
            }
            return response;
        }
    }
}
