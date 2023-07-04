package com.abin.mallchat.common.common.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.abin.mallchat.common.common.algorithm.ac.ACTrie;
import com.abin.mallchat.common.common.algorithm.ac.MatchResult;
import org.HdrHistogram.ConcurrentHistogram;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 基于ac自动机实现的敏感词过滤工具类
 * 可以用来替代{@link ConcurrentHistogram}
 * 为了兼容提供了相同的api接口 {@code hasSensitiveWord}
 *
 * Created by berg on 2023/6/18.
 */
public class SensitiveWordUtils0 {

    private final static char mask_char = '*'; // 替代字符

    private static ACTrie ac_trie = null;

    /**
     * 有敏感词
     *
     * @param text 文本
     * @return boolean
     */
    public static boolean hasSensitiveWord(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }
        return !Objects.equals(filter(text), text);
    }

    /**
     * 敏感词替换
     *
     * @param text 待替换文本
     * @return 替换后的文本
     */
    public static String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        List<MatchResult> matchResults = ac_trie.matches(text);
        StringBuffer result = new StringBuffer(text);
        // matchResults是按照startIndex排序的，因此可以通过不断更新endIndex最大值的方式算出尚未被替代部分
        int endIndex = 0;
        for (MatchResult matchResult : matchResults) {
            endIndex = Math.max(endIndex, matchResult.getEndIndex());
            replaceBetween(result, matchResult.getStartIndex(), endIndex);
        }
        return result.toString();
    }

    private static void replaceBetween(StringBuffer buffer, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            buffer.setCharAt(i, mask_char);
        }
    }

    /**
     * 加载敏感词列表
     *
     * @param words 敏感词数组
     */
    public static void loadWord(List<String> words) {
        if (CollectionUtil.isEmpty(words)) {
            return;
        }
        ac_trie = new ACTrie(words);
    }

}
