# reptile-core

HTML网页解析及特定内容获取工具。

## 使用

### Gradle

```kotlin
implementation("com.jiangyc:reptile-core:1.2.0")
```

### Maven

```xml
<dependency>
    <groupId>com.jiangyc</groupId>
    <artifactId>reptile-core</artifactId>
    <version>1.2.0</version>
</dependency>
```

## 示例

```java
import javax.lang.model.element.Element;

public class ReptileTest {

    public static void main(String[] args) {
        Reptile reptile = new Reptile();

        // List<Map<String, String>> classifies = reptile.resolve("https://www.qidian.com", StandardCharsets.UTF_8, (doc) -> {
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
}
```