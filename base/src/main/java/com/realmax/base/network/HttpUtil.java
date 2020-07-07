package com.realmax.base.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.realmax.base.utils.L;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 基于okhttp的网络请求工具类
 *
 * @author ayuan
 */
public class HttpUtil {
    /**
     * URL地址采用注解的方式传递，必须基于JavaBean来配合使用此方法
     *
     * @param map    需要传递的参数
     * @param tClass 需要解析的javaBean(此参数不能为空)
     * @param result okhttp中callback回调中调用此接口的实现
     * @param <T>    类型
     */
    public static <T> void doPost(Map<String, Object> map, Class<T> tClass, Result<? super T> result) {

        POST annotation = tClass.getAnnotation(POST.class);
        String url = annotation != null ? annotation.value() : null;
        if (url == null) {
            L.e("没有地址，请求取消," + tClass.getSimpleName() + "类没有添加HttpUtil.POST注解");
            return;
        }

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.add(entry.getKey(), String.valueOf(entry.getValue()));
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String string = Objects.requireNonNull(response.body()).string();
                L.e(string);
                T t = new Gson().fromJson(string, tClass);
                result.getData(t, string, call, response);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                result.error(call, e);
            }
        });
    }

    /**
     * 获取图片
     *
     * @param url         图片的URL地址
     * @param imageResult 请求回调
     */
    public static void getImage(String url, ImageResult imageResult) {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                ResponseBody body = response.body();
                if (body != null) {
                    InputStream inputStream = body.byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageResult.getImage(bitmap);
                } else {
                    L.e("请求出现问题");
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                L.e("图片请求出现问题" + e.getMessage());
            }
        });
    }

    /**
     *
     */
    public interface ImageResult {
        void getImage(Bitmap bitmap);
    }

    /**
     * @param <T> 范型，需要解析json数据所对应的类型
     */
    public interface Result<T> {
        /**
         * 请求成功
         *
         * @param t        范型
         * @param data     获取到的数据
         * @param call     单个请求/响应对（流）
         * @param response 响应
         */
        void getData(T t, String data, Call call, Response response);

        /**
         * 请求错误
         *
         * @param call 单个请求/响应对（流）
         * @param e    异常
         */
        void error(Call call, IOException e);
    }

    /**
     * 注解接口，用于HttpUtil识别JavaBean上的地址
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface POST {
        String value();
    }
}
