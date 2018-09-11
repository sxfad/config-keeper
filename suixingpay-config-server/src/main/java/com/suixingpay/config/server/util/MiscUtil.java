package com.suixingpay.config.server.util;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author jiayu.qiu
 */
public class MiscUtil {

    /**
     * 匹配带括号的字符串
     **/
    private static final Pattern BRACKETS_PATTERN = Pattern.compile("^.*[(|)].*$");

    private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z'};

    private static final char[] SPECIAL_CHARS = new char[]{'~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
            '-', '_', '+', '=', '{', '}', '[', ']', '|', '\\', ';', ':', '"', '<', '.', '>', '/', '?'};

    private static final int RADIX = 36;

    private static final int LIMIT = (CHARS.length * CHARS.length - 1);

    /**
     * 获取随机字符串
     *
     * @param destLen          目标字符串长度
     * @param whitSpecialChars 是否包含特殊字符
     * @return 随机字符串
     */
    public static String getRandomStr(int destLen, boolean whitSpecialChars) {
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        int range = CHARS.length;
        if (whitSpecialChars) {
            range += SPECIAL_CHARS.length;
        }
        for (int i = 0; i < destLen; i++) {
            // 生成指定范围类的随机数0—字符串长度(包括0、不包括字符串长度)
            int rand = r.nextInt(range);
            if (rand < CHARS.length) {
                sb.append(CHARS[rand]);
            } else {
                sb.append(SPECIAL_CHARS[rand - CHARS.length]);
            }
        }
        return sb.toString();
    }

    private static final ReentrantLock LOCK = new ReentrantLock();

    private static int serialNum = 0;

    /**
     * 生成唯一字符串（相对的，重复概率非常底）
     *
     * @return 串一字符串
     */
    public static String genUniqueStr() {
        StringBuilder sb = new StringBuilder();
        try {
            LOCK.lock();
            int m = serialNum / CHARS.length;
            int n = serialNum % CHARS.length;

            sb.append(CHARS[m]);
            sb.append(CHARS[n]);
            if (serialNum >= LIMIT) {
                serialNum = 0;
            } else {
                serialNum += 1;
            }
            sb.append(Long.toString(System.currentTimeMillis(), RADIX));
            sb.append(Integer.toString((new Random().nextInt(RADIX)), RADIX));
            sb.append(Integer.toString((new Random().nextInt(RADIX)), RADIX));
        } finally {
            LOCK.unlock();
        }
        return sb.toString();
    }

    /**
     * 检查字符串是否带有括号
     *
     * @param str
     * @return
     */
    public static final boolean isHashBrackets(String str) {
        return BRACKETS_PATTERN.matcher(str).find();
    }

}
