package spider.myspider.httpComponent;


import com.spider.spiderCore.ihttp.IProxyGain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Random;

/**
 * 请求 IP 代理类 存有列表
 *
 * @author 一杯咖啡
 */
@Component
public class DefaultProxyGain extends ArrayList<Proxy> implements IProxyGain {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultProxyGain.class);

    private Random random = new Random();

    /**
     * desc:随机获取ip
     *
     * @Return: Proxy
     **/
    @Override
    public Proxy nextRandom() {
        if (this.size() > 0) {
            int r = random.nextInt(this.size());
            return this.get(r);
        } else {
            return null;
        }
    }

    public void addEmpty() {
        Proxy nullProxy = null;
        this.add(nullProxy);
    }

    /**
     * desc:ip 端口 参数添加
     *
     * @Return:
     **/
    @Override
    public void add(String ip, int port) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
        this.add(proxy);
    }

    /**
     * desc:0.0.0.0：8080 字符串格式添加
     *
     * @Return: void
     **/
    @Override
    public void add(String proxyStr) {
        String[] infos = proxyStr.split(":");
        String ip = infos[0];
        int port = Integer.parseInt(infos[1]);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
        this.add(proxy);
    }

    /**
     * desc: 加载代理 form mysql数据库
     **/
    @Override
    public void addProxysFromMysql() {

    }

    /**
     * desc: 加载代理 form file数据库
     *
     * @throws IOException
     **/
    @Override
    public void addProxysFromFile(File file) throws IOException {
        FileInputStream fis = null;
        BufferedReader br = null;
        fis = new FileInputStream(file);
        br = new BufferedReader(new InputStreamReader(fis));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            } else {
                this.add(line);
            }
        }
        fis.close();
        br.close();
    }
}
