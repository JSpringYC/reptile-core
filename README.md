# reptile-core

HTML网页解析及特定内容获取工具。

## 使用

### Gradle

```kotlin
implementation("com.jiangyc:reptile-core:1.1.1")
```

### Maven

```xml
<dependency>
    <groupId>com.jiangyc</groupId>
    <artifactId>reptile-core</artifactId>
    <version>1.1.1</version>
</dependency>
```

## 示例

```java
public class ReptileTest {
    
    public static void main(String[] args) {
        Reptile reptile = new Reptile();

        // List<Map<String, String>> classifies = reptile.resolve("https://www.qidian.com", StandardCharsets.UTF_8, (doc) -> {
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
}
```