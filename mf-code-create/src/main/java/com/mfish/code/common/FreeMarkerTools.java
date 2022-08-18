package com.mfish.code.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author ：qiufeng
 * @description：freemark工具类
 * @date ：2022/8/18 16:38
 */
@Component
public class FreeMarkerTools {
    /**
     * 判断包路径是否存在
     */
    private void pathJudgeExist(String path){
        File file = new File(path);
        if(!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 输出到文件
     */
    public  void printFile(Map<String, Object> root, Template template, String filePath, String fileName) throws Exception  {
        pathJudgeExist(filePath);
        File file = new File(filePath, fileName );
        if(!file.exists()) {
            file.createNewFile();
        }
        // 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
        // 第七步：调用模板对象的process方法输出文件
        // root参数是模板使用的数据集，out参数是生成的文件
        template.process(root,  out);
        // 第八步：关闭流
        out.close();
    }

    /**
     * 首字母大写
     */
    public  String capFirst(String str){
        return str.substring(0,1).toUpperCase()+str.substring(1).toLowerCase();
    }

    /**
     * 下划线命名转为驼峰命名
     */
    public String underlineToHump(String para){
        StringBuilder result=new StringBuilder();
        String a[]=para.split("_");
        for(String s:a){
            if(result.length()==0){
                result.append(s);
            }else{
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 获取类名
     */
    public String getEntityName(String tableName){
        return underlineToHump(capFirst(tableName.toLowerCase()));
    }

    /**
     * 获取首字母小写类名
     */
    public String getEntityNameLower(String tableName){
        return underlineToHump(tableName.toLowerCase());
    }

    /**
     * 将[数据库类型]转换成[Java类型],如果遇到没有写的类型,会出现Undefine,在后面补充即可
     */
    public  String convertToJava(String columnType){
        String result;
        switch (columnType){
            case "VARCHAR":{
                result = "String";
                break;
            }
            case "INT":{
                result = "Integer";
                break;
            }
            case "BIGINT":{
                result = "Long";
                break;
            }
            case "FLOAT":{
                result = "Float";
                break;
            }
            case "DOUBLE":{
                result = "Double";
                break;
            }
            case "DATETIME":{
                result = "Date";
                break;
            }
            case "BIT":{
                result = "Boolean";
                break;
            }
            default:{
                result = "Undefine";
                break;
            }
        }
        return result;
    }

    /**
     * 匹配字符串中的英文字符
     */
    public String matchResult(String str) {
        String regEx2 = "[a-z||A-Z]";
        Pattern pattern = Pattern.compile(regEx2);
        StringBuilder sb = new StringBuilder();
        Matcher m = pattern.matcher(str);
        while (m.find()){
            for (int i = 0; i <= m.groupCount(); i++)
            {
                sb.append(m.group());
            }
        }
        return sb.toString();
    }

    /**
     * 获取表信息
     */
    public List<Map<String, String>> getDataInfo(String tableName){
        // mysql查询表结构的语句,如果是其他数据库,修改此处查询语句
        String sql = "show columns from "+tableName;
        List<Map<String, Object>> sqlToMap = new ArrayList<>();

        List<Map<String, String>> columns = new LinkedList<>();
        for (Map<String, Object> map : sqlToMap) {
            Map<String, String> columnMap = new HashMap<>();
            // 字段名称
            String columnName = map.get("Field").toString();
            columnMap.put("columnName", columnName);
            // 字段类型
            String columnType = map.get("Type").toString().toUpperCase();
            columnType = matchResult(columnType).trim();
            columnType = convertToJava(columnType);
            columnMap.put("columnType", columnType);
            // 成员名称
            columnMap.put("entityColumnNo", underlineToHump(columnName));
            columns.add(columnMap);
        }
        return columns;
    }

    /**
     * 生成代码
     */
    public void generate(Map<String, Object> root,String templateName,String saveUrl,String entityName) throws Exception {

        // 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是FreeMarker对于的版本号
        Configuration freeMarker = new Configuration(Configuration.getVersion());

        // 获取classes路径下的templates
        final String TEMPLATE_PATH = ResourceUtils.getURL("classpath:").getPath()+"templates";

        // 第二步：设置模板文件所在的路径
        freeMarker.setDirectoryForTemplateLoading(new File(TEMPLATE_PATH));

        // 第三步：设置模板文件使用的字符集。一般就是utf-8
        freeMarker.setDefaultEncoding("utf-8");

        // 第四步：加载一个模板，创建一个模板对象
        Template template = freeMarker.getTemplate(templateName);
        //输出文件
        printFile(root, template, saveUrl, entityName);
    }

}
