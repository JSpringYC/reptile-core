package com.jiangyc.reptile.core;

import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReptileTest {

    @Test
    public void testResolve() throws IOException {
        Reptile reptile = new Reptile();

        List<Map<String, String>> classifies = reptile.resolve("https://www.qidian.com", (doc) -> {
            List<Map<String, String>> classifyList = new ArrayList<>();

            reptile.selectAll(doc, "#classify-list>dl>dd>a", (ele) -> {
                Map<String, String> classifyMap = new HashMap<>();

                String id = reptile.attr(ele, "href", (it) -> it.replaceAll("\\\\", "").replaceAll("/", ""));
                classifyMap.put("id", id);

                String name = reptile.selectOne(ele, "cite>span>i", Element::text);
                classifyMap.put("name", name);

                String count = reptile.selectOne(ele, "cite>span>b", Element::text);
                classifyMap.put("count", count);

                classifyList.add(classifyMap);
                return null;
            });

            return classifyList;
        });

        classifies.forEach(System.out::println);
    }

    @Test
    public void testResolveWithCharset() throws IOException {
        Reptile reptile = new Reptile();

        List<Map<String, String>> classifies = reptile.resolve("https://www.qidian.com", StandardCharsets.UTF_8, (doc) -> {
            List<Map<String, String>> classifyList = new ArrayList<>();

            reptile.selectAll(doc, "#classify-list>dl>dd>a", (ele) -> {
                Map<String, String> classifyMap = new HashMap<>();

                String id = reptile.attr(ele, "href", (it) -> it.replaceAll("\\\\", "").replaceAll("/", ""));
                classifyMap.put("id", id);

                String name = reptile.selectOne(ele, "cite>span>i", Element::text);
                classifyMap.put("name", name);

                String count = reptile.selectOne(ele, "cite>span>b", Element::text);
                classifyMap.put("count", count);

                classifyList.add(classifyMap);
                return null;
            });

            return classifyList;
        });

        classifies.forEach(System.out::println);
    }
}
