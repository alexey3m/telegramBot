package com.ershov.telegram.aliexpressbot.http;

import com.ershov.telegram.aliexpressbot.common.ProductInfoRq;
import com.google.gson.Gson;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Client {

    private static Client client = new Client();
    private String secretKey = "HKXYXHSRHOOWXPHB";

    public static Client getInstance() {
        return client;
    }

    public String sendPostProductRq(String key) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new Gson();
        HttpPost post = new HttpPost("https://api.aliseeks.com/v1/products/details");
        StringEntity entity = new StringEntity(gson.toJson(new ProductInfoRq(key, "RUB", "ru_RU")));
        post.setEntity(entity);
        post.setHeader("Content-type", "application/json");
        post.setHeader("X-Api-Client-Id", secretKey);
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getStockTv() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("https://tmall.aliexpress.com/item/TV-50-Hisense-H50A6500-4k-SmartTV/32947123817.html?algo_pvid=26794890-378f-4049-8a6b-c12c884b937c?spm=a2g0r.search0104.3.19.4958c305u5fYkF&algo_expid=26794890-378f-4049-8a6b-c12c884b937c-2&priceBeautifyAB=0");
        get.setHeader(new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));
        get.setHeader(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br"));
        get.setHeader(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7"));
        get.setHeader(new BasicHeader("Cookie", "ali_apache_id=11.139.21.201.1545545304114.185457.4; XSRF-TOKEN=b1592742-58e9-4036-ab6d-a68501a705b1; acs_usuc_t=x_csrf=116dh6bv7j2kw&acs_rt=3c059450b301496aab8a7022baa3d8d5; xman_t=9oOrcdHS0df77Hq71+VbYmhbUa2vaJPrRvLTz+PUMnmzl2RnnRsrRVYiU+LmsTZ+; xman_f=Y+li1HvidLKD2ks4Z0VlnrMpfBwql4Y4gOOp4w832A4ETd4FN28WlFIKklPJKH99FRIPBMW+JyASFBn4wAhg2turP5su8u+f82rduaRQD2h2tPsxkTATyQ==; _ga=GA1.2.802822752.1545545307; _gid=GA1.2.74227033.1545545307; _gat_gtag_UA_17640202_26=1; _gat=1; _fbp=fb.1.1545545308002.1145833764; cna=WxSmFBP1qRsCAdSkQXLe6KYK; ali_apache_track=; ali_apache_tracktmp=; xman_us_f=x_locale=ru_RU&x_l=0; intl_locale=ru_RU; aep_usuc_f=site=rus&c_tp=RUB&region=RU&b_locale=ru_RU; _ym_uid=1545545326586094221; _ym_d=1545545326; _ym_isad=2; _ym_visorc_29739640=b; intl_common_forever=Y9jIRJBX7jc9T88knJiUXUUt3nDEeViW84uYXF9jMwh8pohMWwkETw==; JSESSIONID=51904D618A9ED1A8AE08D4ADB1B2F28C; isg=BCAgnW_6aHxwhtR4bH2l_9oA8S4ygQTwrui9YJoxjjvWlcK_Qjh8gprnKWWwb7zL"));
        get.setHeader(new BasicHeader("upgrade-insecure-requests", "1"));
        get.setHeader(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36"));
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        String resStr = result.toString();
        int start = resStr.indexOf("window.runParams.totalAvailQuantity");
        String temp = resStr.substring(start, start + 40).replace(';', ' ').trim();
        String[] strings = temp.split("=");
        return Integer.parseInt(strings[1]);
    }
}