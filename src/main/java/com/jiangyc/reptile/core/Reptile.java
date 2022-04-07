package com.jiangyc.reptile.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * 通用爬虫程序工具类
 *
 * Usage:
 * <pre>
 * {@code
 * Reptile reptile = new Reptile();
 * List<Map<String, String>> classifies = reptile.resolve("https://www.qidian.com", (doc) -> {
 *     List<Map<String, String>> classifyList = new ArrayList<>();
 *
 *     reptile.selectAll(doc, "#classify-list>dl>dd>a", (ele) -> {
 *         Map<String, String> classifyMap = new HashMap<>();
 *
 *         String id = reptile.attr(ele, "href", (it) -> it.replaceAll("\\\\", "").replaceAll("/", ""));
 *         classifyMap.put("id", id);
 *
 *         String name = reptile.selectOne(ele, "cite>span>i", Element::text);
 *         classifyMap.put("name", name);
 *
 *         String count = reptile.selectOne(ele, "cite>span>b", Element::text);
 *         classifyMap.put("count", count);
 *
 *         classifyList.add(classifyMap);
 *         return null;
 *     });
 *
 *     return classifyList;
 * });
 * }
 * </pre>
 */
public class Reptile {

    /**
     * 从指定的URL爬取网页某元素内容
     *
     * @param url 要爬取的网页的URL
     * @param processor Document解析器，将Document转换为给定的对象
     * @param <E> 解析网页Document，并获取需要的内容后生成的对象
     * @return 转换Document后的对象
     * @throws IOException 从URL获取网页内容失败时抛出此异常
     * @see Jsoup#connect(String)
     */
    public <E> E resolve(String url, Function<Document, E> processor) throws IOException {
        return processor.apply(Jsoup.connect(url).get());
    }

    /**
     * 从指定的URL并按照给定的编码爬取网页某元素内容
     *
     * @param url 要爬取的网页的URL
     * @param charset 网页所用的编码
     * @param processor Document解析器，将Document转换为给定的对象
     * @param <E> 解析网页Document，并获取需要的内容后生成的对象
     * @return 转换Document后的对象
     * @throws IOException 从URL获取网页内容失败时抛出此异常
     * @see Jsoup#parse(InputStream, String, String)
     */
    public <E> E resolve(String url, Charset charset, Function<Document, E> processor) throws IOException {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }

        return processor.apply(Jsoup.parse(new URL(url).openStream(), charset.name(), url));
    }

    /**
     * 在给定的文档对象中用给定的css选择器检索，如果检索到元素的话，用给定的处理方法处理首个元素。
     *
     * @param ele 文档元素
     * @param cssQuery css选择器
     * @param processor 对选中的首个{@code org.jsoup.nodes.Element}对象的处理方法
     * @param <E> 处理检索到的元素后的生成返回值
     * @return 处理检索到的元素后的生成返回值，如未检索到元素，为null
     */
    public <E> E selectOne(Element ele, String cssQuery, Function<Element, E> processor) {
        Elements elements = ele.select(cssQuery);

        if (!elements.isEmpty()) {
            return processor.apply(elements.first());
        }

        return null;
    }

    /**
     * 在给定的文档对象中用给定的css选择器检索，如果检索到元素的话，用给定的处理方法处理每个元素。
     *
     * @param ele 文档对象
     * @param cssQuery css选择器
     * @param processor 对选中的每个{@code org.jsoup.nodes.Element}对象的处理方法
     */
    public void selectAll(Element ele, String cssQuery, Function<Element, Void> processor) {
        Elements elements = ele.select(cssQuery);

        if (!elements.isEmpty()) {
            for (Element element : elements) {
                processor.apply(element);
            }
        }
    }

    /**
     * 在给定的文档获取元素的属性，并对属性的值进行一定的操作。
     *
     * @param ele 要操作的元素
     * @param attributeKey 要操作的元素的属性
     * @param processor 元素的属性值的额外操作方法，如为{@code null}，则不进行额外的操作
     * @return 元素的属性的值
     */
    public String attr(Element ele, String attributeKey, Function<String, String> processor) {
        String attributeValue = ele.attr(attributeKey);
        if (attributeKey.isBlank()) {
            return null;
        }

        return processor == null ? attributeValue : processor.apply(attributeValue);
    }

    /**
     * 在给定的文档获取元素的文本字符串，并对该文本进行一定的操作。
     *
     * @param ele 要操作的元素
     * @param processor 元素的文本字符串的额外操作方法，如为{@code null}，则不进行额外的操作
     * @return 元素的文本字符串
     */
    public String text(Element ele, Function<String, String> processor) {
        String text = ele.text();
        if (text.isBlank()) {
            return null;
        }

        return processor == null ? text : processor.apply(text);
    }

    /**
     * 从节点数中移除元素。当指定css选择器时，会从指定对象中检索，并移除检索到的元素。如果未指定css选择器时，将移除指定的元素。
     *
     * @param ele 要移除的元素或要移除的元素的父元素
     * @param cssQueries 要移初的元素的css选择器。当该参数未指定时，将移除ele参数指定的元素
     * @see Node#remove()
     */
    public void remove(Element ele, String... cssQueries) {
        if (cssQueries == null || cssQueries.length == 0) {
            ele.remove();
        } else {
            for (String cssQuery : cssQueries) {
                ele.select(cssQuery).forEach(Node::remove);
            }
        }
    }
}
