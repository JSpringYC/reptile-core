package com.jiangyc.reptile.core;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
            Elements elements = doc.select("#classify-list>dl>dd>a");

            List<Map<String, String>> classifyList = new ArrayList<>();

            for (Element ele : elements) {
                Map<String, String> classifyMap = new HashMap<>();

                // id
                String id = ele.attr("href");
                if (!id.isBlank()) {
                    id = id.replaceAll("\\\\", "");
                    id = id.replaceAll("/", "");
                }
                classifyMap.put("id", id);
                // name
                Elements eleName = ele.select("cite>span>i");
                if (!eleName.isEmpty()) {
                    classifyMap.put("name", eleName.get(0).text());
                }
                // count
                Elements eleCount = ele.select("cite>span>b");
                if (!eleCount.isEmpty()) {
                    classifyMap.put("count", eleCount.get(0).text());
                }

                classifyList.add(classifyMap);
            }

            return classifyList;
        });

        classifies.forEach(System.out::println);
    }

    @Test
    public void testResolveWithCharset() throws IOException {
        Reptile reptile = new Reptile();

        List<Map<String, String>> classifies = reptile.resolve("https://www.qidian.com", StandardCharsets.UTF_8, (doc) -> {
            Elements elements = doc.select("#classify-list>dl>dd>a");

            List<Map<String, String>> classifyList = new ArrayList<>();

            for (Element ele : elements) {
                Map<String, String> classifyMap = new HashMap<>();

                // id
                String id = ele.attr("href");
                if (!id.isBlank()) {
                    id = id.replaceAll("\\\\", "");
                    id = id.replaceAll("/", "");
                }
                classifyMap.put("id", id);
                // name
                Elements eleName = ele.select("cite>span>i");
                if (!eleName.isEmpty()) {
                    classifyMap.put("name", eleName.get(0).text());
                }
                // count
                Elements eleCount = ele.select("cite>span>b");
                if (!eleCount.isEmpty()) {
                    classifyMap.put("count", eleCount.get(0).text());
                }

                classifyList.add(classifyMap);
            }

            return classifyList;
        });

        classifies.forEach(System.out::println);
    }
}
