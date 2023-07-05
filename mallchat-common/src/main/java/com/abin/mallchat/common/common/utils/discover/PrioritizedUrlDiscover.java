package com.abin.mallchat.common.common.utils.discover;

import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.Nullable;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 具有优先级的title查询器
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-05-27
 */
public class PrioritizedUrlDiscover extends AbstractUrlDiscover {

    private final List<UrlDiscover> urlTitleDiscovers = new ArrayList<>(2);

    public PrioritizedUrlDiscover() {
        urlTitleDiscovers.add(new CommonUrlDiscover());
        urlTitleDiscovers.add(new WxUrlDiscover());
    }


    @Nullable
    @Override
    public String getTitle(Document document) {
        for (UrlDiscover urlDiscover : urlTitleDiscovers) {
            String urlTitle = urlDiscover.getTitle(document);
            if (StrUtil.isNotBlank(urlTitle)) {
                return urlTitle;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String getDescription(Document document) {
        for (UrlDiscover urlDiscover : urlTitleDiscovers) {
            String urlDescription = urlDiscover.getDescription(document);
            if (StrUtil.isNotBlank(urlDescription)) {
                return urlDescription;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String getImage(String url, Document document) {
        for (UrlDiscover urlDiscover : urlTitleDiscovers) {
            String urlImage = urlDiscover.getImage(url,document);
            if (StrUtil.isNotBlank(urlImage)) {
                return urlImage;
            }
        }
        return null;
    }
}