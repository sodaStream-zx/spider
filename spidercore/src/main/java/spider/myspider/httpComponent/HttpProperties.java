package spider.myspider.httpComponent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 一杯咖啡
 * @desc ihttp 请求参数
 * @createTime 2018-12-28-13:23
 */
@Component
public class HttpProperties {
    private ArrayList<Proxy> proxies = new ArrayList<>();
    private MultiValueMap headerMap = new LinkedMultiValueMap<>();
    private MultiValueMap postBodyMap = new LinkedMultiValueMap();

    public static void main(String[] args) {
        HttpProperties httpProperties = new HttpProperties();

        httpProperties.postBodyMap.add("name", "zxx");
        httpProperties.postBodyMap.add("name", "zxx1");
        httpProperties.postBodyMap.add("name", "zxx2");
        httpProperties.postBodyMap.add("nihao", "2124");
        httpProperties.postBodyMap.add("jason", "s24");
        String json = JSON.toJSONString(httpProperties.postBodyMap, SerializerFeature.PrettyFormat);
        System.out.println("json == " + json);

        LinkedMultiValueMap<String, String> headersMap = JSON.parseObject(json, LinkedMultiValueMap.class);
        System.out.println("headerMap = " + headersMap);
        MultiValueMap mapva = httpProperties.getPostBodyMap();
        if (mapva.size() > 0) {
            Set<Map.Entry<String, List<String>>> keyAndVaule = mapva.entrySet();
            keyAndVaule.forEach(stringListEntry -> {
                int valueLen = stringListEntry.getValue().size();
                for (int i = 0; i < valueLen; i++) {
                    System.out.println(stringListEntry.getKey() + ":" + stringListEntry.getValue().get(i));
                }
            });
        }
    }

    @Override
    public String toString() {
        return "HttpProperties{" +
                "proxies=" + proxies +
                ", headerMap=" + headerMap +
                ", postBodyMap=" + postBodyMap +
                '}';
    }

    public ArrayList<Proxy> getProxies() {
        return proxies;
    }

    public void setProxies(ArrayList<Proxy> proxies) {
        this.proxies = proxies;
    }

    public MultiValueMap getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(MultiValueMap headerMap) {
        this.headerMap = headerMap;
    }

    public MultiValueMap getPostBodyMap() {
        return postBodyMap;
    }

    public void setPostBodyMap(MultiValueMap postBodyMap) {
        this.postBodyMap = postBodyMap;
    }
}
