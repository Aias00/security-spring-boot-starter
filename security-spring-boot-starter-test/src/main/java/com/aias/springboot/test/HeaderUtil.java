package com.aias.springboot.test;

import org.springframework.http.HttpHeaders;

/**
 * Created by cooper on 2018/1/4.
 */
public class HeaderUtil {


    private HeaderUtil() {
    }

    /**
     * 提示信息
     * @param message
     * @param param
     * @return
     */
    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("feezu-alert", string2Unicode(message));
        headers.add("feezu-params", param);

        return headers;
    }

    /**
     * 创建成功提示信息
     * @param entityName
     * @param param
     * @return
     */
    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("A new " + entityName + " is created with identifier " + param, param);
    }

    /**
     * 更新成功提示信息
     * @param entityName
     * @param param
     * @return
     */
    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("A " + entityName + " is updated with identifier " + param, param);
    }

    /**
     * 删除成功提示信息
     * @param entityName
     * @param param
     * @return
     */
    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("A " + entityName + " is deleted with identifier " + param, param);
    }

    /**
     * 创建错误提示信息
     * @param entityName
     * @param errorKey
     * @param defaultMessage
     * @return
     */
    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("feezu-error", string2Unicode(defaultMessage));
        headers.add("feezu-params", entityName);
        return headers;
    }

    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }
}
