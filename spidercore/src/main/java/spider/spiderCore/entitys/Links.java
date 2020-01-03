/*
 * Copyright (C) 2014 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package spider.spiderCore.entitys;


import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 用于存储多个URL的数据结构，继承于ArrayList
 * 存储page中的所有链接
 *
 * @author hu
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Links implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Links.class);
    protected LinkedList<String> dataList = new LinkedList<>();

    public Links() {
    }

    public Links(Collection<String> urls) {
        add(urls);
    }

    public FetcherTasks toCrawlDatums(int deepPath) {
        return new FetcherTasks(this.dataList, deepPath);
    }

    public void add(String url) {
        dataList.add(url);
    }

    public Links add(Collection<String> urls) {
        dataList.addAll(urls);
        return this;
    }

    /**
     * desc: 通过正则过滤page中的链接
     **/
    public Links filterByRegex(RegexRule regexRule) {
        this.dataList.stream().filter(s ->
                !regexRule.satisfyPickRegex(s))
                .forEach(this::remove);
        return this;
    }

    /**
     * 解析网页中的链接
     */
    public Links addAllHref(Document doc) {
        this.add(doc.select("a[href]").eachAttr("abs:href"));
        return this;
    }

    /**
     * 添加ele中，满足选择器的元素中的链接 选择器cssSelector必须定位到具体的超链接
     * 例如我们想抽取id为content的div中的所有超链接，这里 就要将cssSelector定义为div[id=content] a
     */
    public Links addRangeHref(Document doc, String cssSelector) {
        Elements as = doc.select(cssSelector + " a");
        if (as.size() > 0) {
            as.stream().filter(x -> x.hasAttr("href"))
                    .map(a -> a.attr("abs:href"))
                    .forEach(x -> this.add(x));
        }
        return this;
    }

    //jsoup 匹配正则 提取urls
    public Links addHrefByRegx(Document doc, RegexRule regexRule) {
        List<String> es = doc.select("a[href]").eachAttr("abs:href");
        if (es.size() > 0) {
            es.stream().filter(regexRule::satisfyPickRegex).forEach(this::add);
        }
        return this;
    }

    public String get(int index) {
        return dataList.get(index);
    }

    public int size() {
        return dataList.size();
    }

    public String remove(int index) {
        return dataList.remove(index);
    }

    public boolean remove(String url) {
        return dataList.remove(url);
    }

    public void clear() {
        dataList.clear();
    }

    public boolean isEmpty() {
        return dataList.isEmpty();
    }

    public int indexOf(String url) {
        return dataList.indexOf(url);
    }
}
