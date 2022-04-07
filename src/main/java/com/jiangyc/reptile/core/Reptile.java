package com.jiangyc.reptile.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
 *     Elements elements = doc.select("#classify-list>dl>dd>a");
 *
 *     List<Map<String, String>> classifyList = new ArrayList<>();
 *
 *     for (Element ele : elements) {
 *         Map<String, String> classifyMap = new HashMap<>();
 *
 *         // id
 *         String id = ele.attr("href");
 *         if (!id.isBlank()) {
 *             id = id.replaceAll("\\\\", "");
 *             id = id.replaceAll("/", "");
 *         }
 *         classifyMap.put("id", id);
 *         // name
 *         Elements eleName = ele.select("cite>span>i");
 *         if (!eleName.isEmpty()) {
 *             classifyMap.put("name", eleName.get(0).text());
 *         }
 *         // count
 *         Elements eleCount = ele.select("cite>span>b");
 *         if (!eleCount.isEmpty()) {
 *             classifyMap.put("count", eleCount.get(0).text());
 *         }
 *
 *         classifyList.add(classifyMap);
 *     }
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
}
