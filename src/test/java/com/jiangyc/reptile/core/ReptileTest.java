package com.jiangyc.reptile.core;

import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ReptileTest {

    @Test
    public void testResolve() throws IOException {
        Reptile reptile = new Reptile();

        new Reptile().resolve("https://www.qidian.com", (doc) ->
                reptile.selectAll(doc, "#classify-list>dl>dd>a", (ele) -> {
                    Map<String, String> classifyMap = new HashMap<>();

                    String id = reptile.attr(ele, "href", (it) ->
                            it.replaceAll("\\\\", "").replaceAll("/", ""));
                    classifyMap.put("id", id);

                    String name = reptile.selectOne(ele, "cite>span>i", Element::text);
                    classifyMap.put("name", name);

                    String count = reptile.selectOne(ele, "cite>span>b", Element::text);
                    classifyMap.put("count", count);

                    return classifyMap;
                })).forEach(System.out::println);
    }

    @Test
    public void testResolveWithCharset() throws IOException {
        Reptile reptile = new Reptile();

        new Reptile().resolve("https://www.qidian.com", StandardCharsets.UTF_8, (doc) ->
                reptile.selectAll(doc, "#classify-list>dl>dd>a", (ele) -> {
                    Map<String, String> classifyMap = new HashMap<>();

                    String id = reptile.attr(ele, "href", (it) ->
                            it.replaceAll("\\\\", "").replaceAll("/", ""));
                    classifyMap.put("id", id);

                    String name = reptile.selectOne(ele, "cite>span>i", Element::text);
                    classifyMap.put("name", name);

                    String count = reptile.selectOne(ele, "cite>span>b", Element::text);
                    classifyMap.put("count", count);

                    return classifyMap;
                })).forEach(System.out::println);
    }
}
