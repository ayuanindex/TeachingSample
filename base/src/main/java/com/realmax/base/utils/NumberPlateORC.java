package com.realmax.base.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.realmax.base.network.HttpUtil;
import com.realmax.base.signature.NumberPlateResultBean;
import com.realmax.base.signature.TencentCloudAPIInitUtil;

import java.io.IOException;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Response;

public class NumberPlateORC {
    public static void getNumberPlate(Bitmap bitmap, NumberPlateResult numberPlateResult) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                TreeMap<String, Object> params = new TreeMap<>();
                params.put("Action", "LicensePlateOCR");
                params.put("Version", "2018-11-19");
                params.put("Region", "ap-shanghai");
                params.put("ImageBase64", EncodeAndDecode.bitmapToBase64(bitmap));
                TreeMap<String, Object> init = TencentCloudAPIInitUtil.init(params);
                HttpUtil.doPost(init, NumberPlateResultBean.class, new HttpUtil.Result<NumberPlateResultBean>() {
                    @Override
                    public void getData(NumberPlateResultBean numberPlateResultBean, Call call, Response response) {
                        if (numberPlateResult != null) {
                            L.e("请求成功--------" + numberPlateResultBean.toString());
                            numberPlateResult.resultNumberPlate(numberPlateResultBean.getResponse().getNumber());
                        }
                    }

                    @Override
                    public void error(Call call, IOException e) {
                        e.printStackTrace();
                        L.e("请求发生错误------------" + e.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 压缩图片到腾讯云可使用大小
     *
     * @param bitmap 需要压缩的图片
     * @return 返回压缩后的图片
     */
    private static Bitmap compressMatrix(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public interface NumberPlateResult {
        void resultNumberPlate(String numberPlate);
    }
}
