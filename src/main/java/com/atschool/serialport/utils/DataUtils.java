package com.atschool.serialport.utils;

import java.nio.ByteBuffer;
import java.util.Locale;

/**
 * 数据工具，用来处理数组和数据转换
 *
 * @author whoo
 * @create 2022-05-27 20:31
 */
public class DataUtils {

    /**
     * 合并数组
     *
     * @param firstArray  第一个数组
     * @param secondArray 第二个数组
     * @return 合并后的数组
     */
    public static byte[] concatArray(byte[] firstArray, byte[] secondArray) {

        if (firstArray == null || secondArray == null) {
            return null;
        }

        byte[] bytes = new byte[firstArray.length + secondArray.length];
        System.arraycopy(firstArray, 0, bytes, 0, firstArray.length);
        System.arraycopy(secondArray, 0, bytes, firstArray.length, secondArray.length);
        return bytes;
    }

    /**
     * 十六进制字符串转byte[]
     *
     * @param hex 十六进制字符串
     * @return byte[]
     */
    public static byte[] hexStr2Byte(String hex) {

        if (hex == null) {
            return new byte[]{};
        }

        // 奇数位补0
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }

        int length = hex.length();
        ByteBuffer buffer = ByteBuffer.allocate(length / 2);
        for (int i = 0; i < length; i++) {
            String hexStr = hex.charAt(i) + "";
            i++;
            hexStr += hex.charAt(i);
            byte b = (byte) Integer.parseInt(hexStr, 16);
            buffer.put(b);
        }
        return buffer.array();
    }

    /**
     * byte[]转十六进制字符串
     *
     * @param array byte[]
     * @return 十六进制字符串
     */
    public static String byteArrayToHexString(byte[] array) {

        if (array == null) {
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            buffer.append(byteToHex(array[i]));
        }
        return buffer.toString();
    }

    /**
     * byte转十六进制字符
     *
     * @param b byte
     * @return 十六进制字符
     */
    public static String byteToHex(byte b) {

        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex.toUpperCase(Locale.getDefault());
    }
}
