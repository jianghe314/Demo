package com.kangzhan.student.zfbapi;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kangzhan011 on 2017/6/19.
 */

public class OrderInfoUtil {
    public static Map<String,String> builderOrderParamMap(AliPay aliPay){
        Map<String,String> keyValues=new HashMap<>();
        keyValues.put("app_id",aliPay.getApp_id());
        keyValues.put("biz_content",getBiz_Content(aliPay.getBiz_content()));
        keyValues.put("charset", aliPay.getCharset());
        keyValues.put("format",aliPay.getFormat());
        keyValues.put("method", aliPay.getMethod());
        keyValues.put("notify_url",aliPay.getNotify_url());
        keyValues.put("sign_type",aliPay.getSign_type());
        keyValues.put("timestamp",aliPay.getTimestamp());
        keyValues.put("version",aliPay.getVersion());
        return keyValues;
    }
    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }
    /**
     * 对支付参数信息进行签名
     *
     * @param map
     *            待签名授权信息
     *
     * @return
     */
    public static String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }




    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    private static String getBiz_Content(Biz_content data){
        StringBuilder builder=new StringBuilder();
        builder.append("{");
        builder.append("\"body\":"+"\""+data.getBody()+"\"");
        builder.append(",\"subject\":"+"\""+data.getSubject()+"\"");
        builder.append(",\"out_trade_no\":"+"\""+data.getOut_trade_no()+"\"");
        builder.append(",\"timeout_express\":"+"\""+data.getTimeout_express()+"\"");
        builder.append(",\"total_amount\":"+"\""+data.getTotal_amount()+"\"");
        builder.append(",\"product_code\":"+"\""+data.getProduct_code()+"\"");
        builder.append("}");
        //Log.e("Biz_content","->"+builder.toString());
        return builder.toString();
    }


}
