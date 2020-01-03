package spider.spiderCore.entitys;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * 正则表达式 存储 判断
 *
 * @author Twilight
 */
@Component
public class RegexRule {
    private static final Logger log = LoggerFactory.getLogger(RegexRule.class);
    private ArrayList<String> pickRegexs = new ArrayList<>();
    private ArrayList<String> contentRegexRules = new ArrayList<>();

    public RegexRule() {
    }

    /**
     * desc: 添加正文url 正则
     *
     * @param str 正则表达式字符串
     * @return RegexRule
     **/
    public RegexRule addContentRegexRule(String str) {
        if (StringUtils.isBlank(str)) {
            log.error("正则表达式不能为空");
        } else {
            this.contentRegexRules.add(str);
        }
        return this;
    }

    /**
     * desc: 添加正文提取url 正则
     *
     * @param strs 正则表达式字符串数组
     * @return RegexRule
     **/
    public RegexRule addContentRegexRule(String[] strs) {
        if (strs.length > 0) {
            Arrays.asList(strs).forEach(s -> addContentRegexRule(s));
        }
        return this;
    }

    /**
     * 添加一个正则规则
     *
     * @param rule 正则规则
     * @return 自身
     */
    public RegexRule addPickReges(String rule) {
        if (StringUtils.isBlank(rule)) {
            return this;
        } else {
            this.pickRegexs.add(rule);
            return this;
        }
    }

    /**
     * 添加一个正则规则
     *
     * @param rules 正则规则
     * @return 自身
     */
    public RegexRule addPickReges(String[] rules) {
        if (rules.length > 0) {
            Arrays.asList(rules).forEach(s -> addPickReges(s));
        }
        return this;
    }

    /**
     * 判断输入字符串是否符合正则规则
     *
     * @param str 输入的字符串
     * @return 输入字符串是否符合链接提取的正则规则
     */
    public boolean satisfyPickRegex(String str) {
        boolean satisfy = pickRegexs.stream().anyMatch(x -> Pattern.matches(x, str));
        return satisfy;
    }

    /**
     * desc: 判断str 时候能匹配到某条正文提取 正则
     *
     * @param str 输入的正则字符串
     * @Return: boolean
     **/
    public boolean satisfyContentRegex(String str) {
        boolean satisfy = contentRegexRules.stream().anyMatch(x -> Pattern.matches(x, str));
        return satisfy;
    }

    public boolean isEmpty() {
        return pickRegexs.isEmpty();
    }

    public String info() {
        return this.pickRegexs.toString();
    }

    public ArrayList<String> getPickRegexs() {
        return pickRegexs;
    }

    public ArrayList<String> getContentRegexRules() {
        return contentRegexRules;
    }

}
