package com.noshadow.app.api;

import android.util.Log;

import com.noshadow.app.model.EmptyProxy;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Liga on 27/06/2017.
 */

public class ApiFactory {

    private static Integer timeOut = 50;

    public static <T> T createRetrofitService(final Class<T> clazz, final String baseUrl) {
        return createRetrofitService(clazz, baseUrl, timeOut);
    }

	public static <T> T createRetrofitService(final Class<T> clazz, final String baseUrl, Integer timeoutSeconds)
	{
		final Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(
				new NullOnEmptyConverterFactory()).addConverterFactory(
				GsonConverterFactory.create()).addCallAdapterFactory(
				RxJava2CallAdapterFactory.create()).client(
				getOkHttpClientWithInterceptor(timeOut)).build();

		T service = retrofit.create(clazz);
		return service;
	}


    private static OkHttpClient getOkHttpClientWithInterceptor(Integer timeoutSeconds) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

	    // TODO: Comment this JSON response interceptor
		httpClient.addInterceptor(new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				Request request = chain.request();

				Response response = chain.proceed(request);
				String rawJson = response.body().string();

				Log.d("APROVATION", String.format("raw JSON response is: %s", rawJson));

				// Re-create the response before returning it because body can be read only once
				return response.newBuilder()
						.body(ResponseBody.create(response.body().contentType(), rawJson)).build();
			}
		});

	    if(timeoutSeconds != null)
		    httpClient
				    .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
				    .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
				    .writeTimeout(timeoutSeconds, TimeUnit.SECONDS);

        OkHttpClient client = httpClient.build();
        return client;
    }

    public static class NullOnEmptyConverterFactory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                                Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this,
                    type, annotations);
            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0)
                        return new EmptyProxy();
                    return delegate.convert(body);
                }
            };
        }
    }
}


