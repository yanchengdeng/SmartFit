package com.smartfit.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.smartfit.R;
import com.smartfit.beans.CityBean;
import com.smartfit.beans.SelectedSort;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.beans.WorkPointAddress;
import com.smartfit.commons.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Util {
    /**
     * dpתpx
     */
    public static int dip2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 获取与本地距离
     *
     * @param endLat
     * @param endLong
     * @return
     */
    public static String getDistance(String endLat, String endLong) {

        String startLat = SharedPreferencesUtils.getInstance().getString(Constants.CITY_LAT, "");
        String startLng = SharedPreferencesUtils.getInstance().getString(Constants.CITY_LONGIT, "");

        if (TextUtils.isEmpty(startLat) || TextUtils.isEmpty(startLng)) {
            return "0km";
        }
        if (TextUtils.isEmpty(endLat) || TextUtils.isEmpty(endLong)) {
            return "0km";
        }
        LatLng startLatlng = new LatLng(Float.parseFloat(startLat), Float.parseFloat(endLong));
        LatLng endLatlng = new LatLng(Float.parseFloat(endLat), Float.parseFloat(endLong));
// 计算量坐标点距离
        float distance = AMapUtils.calculateLineDistance(startLatlng, endLatlng);

        return String.format("%.2fkm", distance / 1000);
    }

    /**
     * pxתdp
     */
    public static int px2dip(Context ctx, float pxValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static byte[] httpGet(final String url) {
        if (url == null || url.length() == 0) {
            return null;
        }

        HttpClient httpClient = getNewHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse resp = httpClient.execute(httpGet);
            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }

            return EntityUtils.toByteArray(resp.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] httpPost(String url, String entity) {
        if (url == null || url.length() == 0) {
            return null;
        }

        HttpClient httpClient = getNewHttpClient();

        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new StringEntity(entity));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");


            HttpResponse resp = httpClient.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }

            return EntityUtils.toByteArray(resp.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<SelectedSort> getSortList(Context context) {
        List<SelectedSort> selectedSorts = new ArrayList<>();
        String[] ids = new String[]{"0", "1", "2"};
        int[] names = new int[]{R.string.select_by_time, R.string.select_by_coach, R.string.select_by_left};
        for (int i = 0; i < ids.length; i++) {
            selectedSorts.add(new SelectedSort(ids[i], context.getString(names[i])));
        }
        return selectedSorts;
    }

    /**
     * 获取用户信息
     *
     * @param context
     */
    public static UserInfoDetail getUserInfo(Context context) {
        String userInfo = SharedPreferencesUtils.getInstance().getString(Constants.USER_INFO, "");
        if (!TextUtils.isEmpty(userInfo)) {
            UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(userInfo, UserInfoDetail.class);
            return userInfoDetail;
        }
        return null;
    }


    /**
     * 保存用户信息
     *
     * @param userInfoDetail
     */
    public static void saveUserInfo(UserInfoDetail userInfoDetail) {
        SharedPreferencesUtils.getInstance().putString(Constants.USER_INFO, JsonUtils.toJson(userInfoDetail));
    }

    public static void saveImageToPhoto(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "smart");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));

    }

    private static class SSLSocketFactoryEx extends SSLSocketFactory {

        SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    private static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    /**
     * 设置当前城市信息
     *
     * @param cityname
     */
    public static void setCityInfo(String cityname) {
        String cityinfos = SharedPreferencesUtils.getInstance().getString(Constants.CITY_LIST_INOF, "");
        if (!TextUtils.isEmpty(cityinfos)) {
            List<CityBean> cityBeans = JsonUtils.listFromJson(cityinfos, CityBean.class);
            for (CityBean item : cityBeans) {
                if (item.getDictionaryName().equals(cityname)) {
                    SharedPreferencesUtils.getInstance().putString(Constants.CITY_CODE, item.getDictionaryCode());
                    SharedPreferencesUtils.getInstance().putString(Constants.CITY_NAME, cityname + "市");
                    return;
                }
            }
        }
    }

    /**
     * 初始化场馆列表
     */
    public static List<WorkPointAddress> getVenueList() {
        String venuList = SharedPreferencesUtils.getInstance().getString(Constants.VENUE_LIST_INFO, "");
        if (!TextUtils.isEmpty(venuList)) {
            List<WorkPointAddress> workPointAddresses = JsonUtils.listFromJson(venuList, WorkPointAddress.class);
            if (workPointAddresses != null && workPointAddresses.size() > 0) {
                return workPointAddresses;
            }
        }

        return null;
    }
}