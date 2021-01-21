package com.mhao.mall;

//import org.junit.jupiter.api.Test;

import cn.hutool.core.util.StrUtil;
import com.mhao.mall.dao.UserMapper;
import com.mhao.mall.enums.TestSqlEnum;
import com.mhao.mall.utils.WordToHtml;
import com.mhao.mall.vo.UserVo02;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nfunk.jep.JEP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallApplicationTests {

    @Autowired
    UserMapper userMapper;

    //保证里面有方法即可，随便写
    @Test
    public void empty() throws IOException, TransformerException, ParserConfigurationException {
        String docPath = "C:\\Users\\FightingHao\\Desktop\\test";
        String pdfPath = "test";
        String res = WordToHtml.Word2003ToHtml(docPath, pdfPath, ".doc");
        System.out.println(res);

    }

    //保证里面有方法即可，随便写
    @Test
    public void empty02() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
        date = calendar.getTime();
        String accDate = format.format(date);
        Date lastDate = format.parse(accDate);
        System.out.println(accDate);
        System.out.println(lastDate);
        System.out.println(date);
    }


    @Test
    public void empty03() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String accDate = format.format(date);
        System.out.println(date);
        System.out.println(accDate);

        SimpleDateFormat format02 = new SimpleDateFormat("yyyy");
        Date yearTime = format02.parse("2020");
        String yearTime02 = format.format(yearTime);

        SimpleDateFormat format03 = new SimpleDateFormat("yyyy-MM");
        Date monthTime = format03.parse("2020-12");
        String monthTime02 = format.format(monthTime);


        System.out.println(yearTime);
        System.out.println(yearTime02);
        System.out.println(monthTime);
        System.out.println(monthTime02);

    }

    @Test
    public void test01() {
        char char1 = '(';
        char char2 = ')';
        char char3 = '+';
        char char4 = '-';
        char char5 = '*';
        char char6 = '/';
        char char7 = ']';
        char char8 = '[';
        char[] chars = {'(', ')', '+', '-', '*', '/', '[', ']'};

        JEP jep = new JEP(); //一个数学表达式
        String exp = "[(a+b)*(c+b))]/(c+a)/b+d-f"; //给变量赋值

//        String s = StrUtil.removeAll(exp, char1,char2,char3,char4,char5,char6,char7,char8);
        String s1 = StrUtil.replaceChars(exp, chars, ",");
        System.out.println(s1);

        String[] split = s1.split(",");
        List<String> resultValues = new ArrayList<>();
        for (String s2 : split) {
            System.out.println("s2=" + s2);
            if (!s2.equals("")) {
                resultValues.add(s2);
            }
        }
        System.out.println(resultValues);
        for (String resultValue : resultValues) {
            System.out.println(resultValue);
        }


//        jep.addVariable("a", 10);
//        jep.addVariable("b", 10);
//        jep.addVariable("c", 10);
//        jep.addVariable("d", 10);
//
//        try { //执行
//            jep.parseExpression(exp);
//            Object result = jep.getValueAsObject();
//            System.out.println("计算结果： " + result);
//        } catch (Throwable e) {
//            System.out.println("An error occured: " + e.getMessage());
//        }
    }

    @Test
    public void test02() {
        char[] chars = {'(', ')', '+', '-', '*', '/', '[', ']'};

        JEP jep = new JEP();//该对象用于公式计算
        //公式
        String exp = "[(PHONE+EMAIL)*(PHONE+EMAIL))]/(PHONE+EMAIL)/PHONE+EMAIL-PHONE"; //给变量赋值

        //将公式中的所有运算符替换成逗号“，”
        String s1 = StrUtil.replaceChars(exp, chars, ",");
        //用逗号来分割，取出每一个参数（不重复），存进set集合
        String[] split = s1.split(",");
        Set<String> paramsSet = new HashSet<>();
        for (String s2 : split) {
            if (!s2.equals("")) {
                paramsSet.add(s2);
            }
        }
        System.out.println(paramsSet);
        //遍历set集合取出每一个参数，去其相应的数据库表中取出对应的值
        for (String param : paramsSet) {
            UserVo02 userVo02 = new UserVo02();
            userVo02.setUsername("admin");
            userVo02.setSql(TestSqlEnum.valueOf(param).getDesc());//到参数对应的枚举中取对应的sql值
            String paramValue = userMapper.sqlTest(userVo02);
            Double aDouble = Double.valueOf(paramValue);
            jep.addVariable(param, aDouble);
            System.out.println(paramValue);
        }
        try { //执行公式进行计算
            jep.parseExpression(exp);
            Object result = jep.getValueAsObject();
            System.out.println("计算结果： " + result);
        } catch (Throwable e) {
            System.out.println("An error occured: " + e.getMessage());
        }
    }

    @Test
    public void test03() {
        List<String> values = new ArrayList<>();
        values.add("PHONE");
        values.add("EMAIL");

        for (String value : values) {

            UserVo02 userVo02 = new UserVo02();
            userVo02.setUsername("admin");
            String desc = TestSqlEnum.valueOf(value).getDesc();
            userVo02.setSql(desc);
            String Value = userMapper.sqlTest(userVo02);

            System.out.println(Value);
        }
    }

}
