package spider.myspider.controller;

import com.spider.myspider.services.MySpider;
import commoncore.exceptionHandle.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-24-14:39
 */
@RestController
public class SpiderController {
    @Autowired
    private MySpider mySpider;

    @GetMapping(value = "/spider")
    public Boolean start() {
        new Thread(() -> {
            try {
                mySpider.startSpider();
            } catch (MyException e) {
                e.printStackTrace();
            }
        }, "startSpider--").start();
        return true;
    }

    @GetMapping(value = "/stop")
    public boolean stopSpider() {
        new Thread(() -> mySpider.stopSpider(), "startSpider--").start();
        return true;
    }
}
