package com.aigcfast.chat.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 工具类
 * @Author lcy
 * @Date 2021/1/12 15:29
 */
public class Tools {

    private static final Logger log = LoggerFactory.getLogger(Tools.class);

    /**
     * 判断字符串是否为空
     * @param cs 字符串
     * @return boolean
     * @author lcy
     * @date 2021/1/12 16:03
     **/
    public static boolean isEmpty(CharSequence cs){
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为空
     * @param charSequences 字符串数组
     * @return boolean
     * @author lcy
     * @date 2021/1/12 16:03
     **/
    public static boolean isEmpty(CharSequence... charSequences){
        for (CharSequence cs : charSequences) {
            if (isNotEmpty(cs)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断对象是否为空
     * @param object 对象
     * @return boolean
     * @author lcy
     * @date 2021/1/12 16:03
     **/
    public static <T> boolean isEmpty(T object){
        return object == null;
    }

    /**
     * 判断数组是否为空
     * @param array 数组
     * @return boolean
     * @author lcy
     * @date 2021/1/12 16:03
     **/
    public static <T> boolean isEmpty(T[] array){
        return array == null || array.length == 0;
    }

    /**
     * 判断集合是否为空
     * @param collection 集合
     * @return boolean
     * @author lcy
     * @date 2021/1/12 16:03
     **/
    public static <T> boolean isEmpty(Collection<T> collection){
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断map是否为空
     * @param map map
     * @return boolean
     * @author lcy
     * @date 2021/1/12 16:03
     **/
    public static <K,V> boolean isEmpty(Map<K,V> map){
        return map == null || map.isEmpty();
    }

    /**
     * 判断字符串是否不为空
     * @param cs 字符串
     * @return boolean
     * @author lcy
     * @date 2021/1/12 16:03
     **/
    public static boolean isNotEmpty(CharSequence cs){
        return !isEmpty(cs);
    }

    /**
     * 判断多个字符串是否不为空
     * @param charSequences 字符串数组
     * @return boolean
     * @author lcy
     * @date 2021/1/12 16:03
     **/
    public static boolean isNotEmpty(CharSequence... charSequences){
        for (CharSequence cs : charSequences) {
            if (isEmpty(cs)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断对象是否不为空
     * @param object 对象
     * @return boolean
     * @author lcy
     * @date 2021/1/12 16:03
     **/
    public static <T> boolean isNotEmpty(T object){
        return object != null;
    }

    /**
     * 判断数组是否不为空
     * @param array 数组
     * @return boolean
     * @author lcy
     * @date 2021/1/12 16:03
     **/
    public static <T> boolean isNotEmpty(T[] array){
        return !isEmpty(array);
    }

    /**
     * 判断集合是否不为空
     * @param collection 集合
     * @return boolean
     * @author lcy
     * @date 2021/1/12 16:03
     **/
    public static <T> boolean isNotEmpty(Collection<T> collection){
        return !isEmpty(collection);
    }

    /**
     * 判断map是否不为空
     * @param map map
     * @return boolean
     * @author lcy
     * @date 2021/1/12 16:03
     **/
    public static <K,V> boolean isNotEmpty(Map<K,V> map){
        return !isEmpty(map);
    }

    /**
     * 判断两个对象是否相等
     * @param first  对象1
     * @param second 对象2
     * @return boolean
     * @author lcy
     * @date 2021/1/12 16:19
     **/
    public static <F,S> boolean equals(F first,S second){
        return Objects.equals(first,second);
    }

    /**
     * utf-8 转换成 unicode
     * @param str 字符串
     * @return java.lang.String
     * @author lcy
     * @date 2021/12/8 19:43
     **/
    public static String utf8ToUnicode(String str){
        char[] myBuffer = str.toCharArray();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(myBuffer[i]);
            if (ub == Character.UnicodeBlock.BASIC_LATIN) {
                //英文及数字等
                sb.append(myBuffer[i]);
            } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                //全角半角字符
                int j = (int)myBuffer[i] - 65248;
                sb.append((char)j);
            } else {
                //汉字
                short s = (short)myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * unicode 转换成 utf-8
     * @param str 字符串
     * @return java.lang.String
     * @author lcy
     * @date 2021/12/8 19:43
     **/
    public static String unicodeToUtf8(String str){
        char aChar;
        int len = str.length();
        StringBuilder outBuffer = new StringBuilder(len);
        for (int x = 0; x < len; ) {
            aChar = str.charAt(x++);
            if (aChar == '\\') {
                aChar = str.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = str.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char)value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

}
