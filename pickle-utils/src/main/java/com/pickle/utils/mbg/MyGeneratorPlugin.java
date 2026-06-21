package com.pickle.utils.mbg;

import com.pickle.utils.date.DateUtils;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;


/**
 * 字段和类生成时，会调用PluginAdapter中的方法，所以可以按需求重写对应的方法
 */
public class MyGeneratorPlugin extends PluginAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MyGeneratorPlugin.class);

    private static String cjRyDm = "cjRyDm";
    private static String cjSj = "cjSj";
    private static String xgRyDm = "xgRyDm";
    private static String xgSj = "xgSj";
    private static String sjgsJgDm = "sjgsJgDm";
    private static String zfRyDm = "zfRyDm";
    private static String zfSj = "zfSj";

    private String targetProject;
    private String resourcesProject;

    private String controllerPackage;
    private String servicePackage;
    private String serviceImplPackage;
    private String mapperPackage;
    private String xmlPackage;

    private String superController;
    private String superServiceInterface;
    private String superServiceImpl;
    private String superMapper;

    private String controllerName;
    private String serviceName;
    private String serviceImplName;

    private String recordType;
    private String modelName;

    private String generatorUserName;

    private String flFlag;

    @Override
    public boolean validate(List<String> list) {
        targetProject = properties.getProperty("targetProject");
        resourcesProject = properties.getProperty("resourcesProject");

        controllerPackage = properties.getProperty("controllerPackage");
        servicePackage = properties.getProperty("servicePackage");
        serviceImplPackage = properties.getProperty("serviceImplPackage");
        mapperPackage = properties.getProperty("mapperPackage");
        xmlPackage = properties.getProperty("xmlPackage");

        superController = properties.getProperty("superController");
        superServiceInterface = properties.getProperty("superServiceInterface");
        superServiceImpl = properties.getProperty("superServiceImpl");
        superMapper = properties.getProperty("superMapper");

        generatorUserName = properties.getProperty("generatorUserName");
        flFlag = properties.getProperty("flFlag");
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        recordType = introspectedTable.getBaseRecordType();
        modelName = recordType.substring(recordType.lastIndexOf(".") + 1);

        controllerName = controllerPackage +"." +modelName +"Controller";
        serviceName = servicePackage + ".I" + modelName + "Service";
        serviceImplName = serviceImplPackage + "." + modelName + "Service";

        List<GeneratedJavaFile> answer = new ArrayList<>();

        File controllerFile = new File(targetProject +"\\" +controllerName.replace(".", "\\") +".java");
        if (!controllerFile.exists()) {
            GeneratedJavaFile controller = generateController(introspectedTable);
            answer.add(controller);
        }else {
            logger.info("controller已存在，跳过");
        }

        File serviceFile = new File(targetProject +"\\" +serviceName.replace(".", "\\") +".java");
        if (!serviceFile.exists()){
            GeneratedJavaFile serviceInterface = generateServiceInterface(introspectedTable);
            answer.add(serviceInterface);
        }else {
            logger.info("service已存在，跳过");
        }

        File serviceImplFile = new File(targetProject +"\\" +serviceImplName.replace(".", "\\") +".java");
        if (!serviceImplFile.exists()){
            GeneratedJavaFile serviceImpl = generateServiceImpl(introspectedTable);
            answer.add(serviceImpl);
        }else {
            logger.info("service实现类已存在，跳过");
        }

        return answer;
    }

    /**
     * 当 SQL Map (XML) 文件被生成后调用。
     * 可以在这里修改文件的目标路径。
     */
    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        try {
            java.lang.reflect.Field field = GeneratedXmlFile.class.getDeclaredField("targetPackage");
            field.setAccessible(true);
            field.set(sqlMap, xmlPackage);
            logger.info("已将文件目标包修改为: " + xmlPackage);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return true;
    }

    // 生成controller类
    private GeneratedJavaFile generateController(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType controller = new FullyQualifiedJavaType(controllerName);
        TopLevelClass clazz = new TopLevelClass(controller);
        //描述类的作用域修饰符
        clazz.setVisibility(JavaVisibility.PUBLIC);

        //添加@Controller注解，并引入相应的类
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RestController"));
        clazz.addAnnotation("@RestController");
        //添加@RequestMapping注解，并引入相应的类
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestMapping"));
        clazz.addAnnotation("@RequestMapping(\"/" + firstCharToLowCase(modelName) + "\")");
        clazz.addImportedType(new FullyQualifiedJavaType("lombok.RequiredArgsConstructor"));
        clazz.addAnnotation("@RequiredArgsConstructor");

        //引入controller的父类和model，并添加泛型
        if (stringHasValue(superController)) {
            clazz.addImportedType(superController);
            clazz.addImportedType(recordType);
            FullyQualifiedJavaType superInterfac = new FullyQualifiedJavaType(superController + "<" + modelName + ">");
            clazz.setSuperClass(superInterfac);
        }

        //引入Service
        FullyQualifiedJavaType service = new FullyQualifiedJavaType(serviceName);
        clazz.addImportedType(service);

        //添加Service成员变量
        String input = serviceName.substring(serviceName.lastIndexOf(".") + 1);
        String result = input.substring(1); // 从第二个字符开始截取
        result = result.substring(0, 1).toLowerCase() + result.substring(1); // 首字母小写

        Field daoField = new Field(result, new FullyQualifiedJavaType(serviceName));
        clazz.addImportedType(new FullyQualifiedJavaType(serviceName));

        //描述成员属性修饰符
        daoField.setVisibility(JavaVisibility.PRIVATE);
        // 添加 final 修饰符
        daoField.setFinal(true);
        clazz.addField(daoField);

        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestBody"));
        clazz.addImportedType(new FullyQualifiedJavaType("jakarta.validation.Valid"));
        //实体类变量名
        String record = modelName.substring(0, 1).toLowerCase() + modelName.substring(1); // 首字母小写
        //主键
        String javaProperty = introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty();
        String column = javaProperty.substring(0, 1).toUpperCase() + javaProperty.substring(1); // 首字母小写

        Method saveMethod = new Method("save");
        saveMethod.setVisibility(JavaVisibility.PUBLIC);
        // 添加 @RequestMapping 注解，表示这是一个处理 POST 请求的方法
        saveMethod.addAnnotation("@RequestMapping(\"/save\")");
        // 设置返回类型为 RestBean<String>
        saveMethod.setReturnType(new FullyQualifiedJavaType("void"));
        // 添加 @RequestBody 注解，告诉 Spring 从请求体中读取数据并映射到参数
        saveMethod.addParameter(new Parameter(new FullyQualifiedJavaType("@Valid @RequestBody " +modelName), record));
        // 方法体，调用 service 层的 create 方法
        saveMethod.addBodyLine(result +".save(" +record +");");
        //return返回
//        saveMethod.addBodyLine("return RestBean.success();");

        Method updateMethod = new Method("update");
        updateMethod.setVisibility(JavaVisibility.PUBLIC);
        updateMethod.addAnnotation("@RequestMapping(\"/update\")");
        updateMethod.setReturnType(new FullyQualifiedJavaType("void"));
        updateMethod.addParameter(new Parameter(new FullyQualifiedJavaType("@Valid @RequestBody " +modelName), record));
        updateMethod.addBodyLine(result +".update(" +record +");");
//        updateMethod.addBodyLine("return RestBean.success();");

        Method deleteMethod = new Method("delete");
        deleteMethod.setVisibility(JavaVisibility.PUBLIC);
        deleteMethod.addAnnotation("@RequestMapping(\"/delete\")");
        deleteMethod.setReturnType(new FullyQualifiedJavaType("void"));
        deleteMethod.addParameter(new Parameter(new FullyQualifiedJavaType("@RequestBody " +modelName), record));
        deleteMethod.addBodyLine(result +".deleteByPrimaryKey(" +record +".get" +column +"());");
//        deleteMethod.addBodyLine("return RestBean.success();");

        clazz.addMethod(saveMethod);
        clazz.addMethod(updateMethod);
        clazz.addMethod(deleteMethod);

        return new GeneratedJavaFile(clazz, targetProject, context.getJavaFormatter());
    }

    private GeneratedJavaFile generateServiceInterface(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType service = new FullyQualifiedJavaType(serviceName);
        Interface serviceInterface = new Interface(service);
        serviceInterface.setVisibility(JavaVisibility.PUBLIC);
        // 添加父接口
        if (stringHasValue(superServiceInterface)) {
            String superServiceInterfaceName = superServiceInterface.substring(superServiceInterface.lastIndexOf(".") + 1);
            serviceInterface.addImportedType(new FullyQualifiedJavaType(superServiceInterface));
            serviceInterface.addImportedType(new FullyQualifiedJavaType(recordType));
            serviceInterface.addSuperInterface(new FullyQualifiedJavaType(superServiceInterfaceName + "<" + modelName + ">"));
        }
        return new GeneratedJavaFile(serviceInterface, targetProject, context.getJavaFormatter());
    }

    private GeneratedJavaFile generateServiceImpl(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType service = new FullyQualifiedJavaType(serviceName);
        FullyQualifiedJavaType serviceImpl = new FullyQualifiedJavaType(serviceImplName);
        TopLevelClass clazz = new TopLevelClass(serviceImpl);
        //描述类的作用域修饰符
        clazz.setVisibility(JavaVisibility.PUBLIC);
        //描述类 引入的类
        clazz.addImportedType(service);
        //描述类 的实现接口类
        clazz.addSuperInterface(service);
        if (stringHasValue(superServiceImpl)) {
            String superServiceImplName = superServiceImpl.substring(superServiceImpl.lastIndexOf(".") + 1);
            clazz.addImportedType(superServiceImpl);
            clazz.addImportedType(recordType);
            clazz.setSuperClass(superServiceImplName + "<" + modelName + ">");
        }
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));
        clazz.addAnnotation("@Service");
        clazz.addImportedType(new FullyQualifiedJavaType("lombok.RequiredArgsConstructor"));
        clazz.addAnnotation("@RequiredArgsConstructor");

        String daoFieldType = introspectedTable.getMyBatis3JavaMapperType();
        String daoFieldName = firstCharToLowCase(daoFieldType.substring(daoFieldType.lastIndexOf(".") + 1));
        //描述类的成员属性
        Field daoField = new Field(daoFieldName, new FullyQualifiedJavaType(daoFieldType));
        clazz.addImportedType(new FullyQualifiedJavaType(daoFieldType));
        // 添加 final 修饰符
        daoField.setFinal(true);
        //描述成员属性修饰符
        daoField.setVisibility(JavaVisibility.PRIVATE);
        clazz.addField(daoField);

        return new GeneratedJavaFile(clazz, targetProject, context.getJavaFormatter());
    }

    private String firstCharToLowCase(String str) {
        char[] chars = new char[1];
        //String str="ABCDE1234";
        chars[0] = str.charAt(0);
        String temp = new String(chars);
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
            return str.replaceFirst(temp, temp.toLowerCase());
        }
        return str;
    }

    // 你可以在这里对生成的实体类进行修改，例如添加注释、修改类的名称等
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.generateCustomModel(topLevelClass, introspectedTable);

        this.generateCustomModelEntity(topLevelClass, introspectedTable);

        //搞一些前端的东西
        this.generateCustomModelVue(topLevelClass, introspectedTable);
        return true;
    }

    private void generateCustomModelVue(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String shortName = topLevelClass.getType().getShortName() + "Vue";
        String packageName = topLevelClass.getType().getPackageName() + ".entity";
        // 创建业务类

        TopLevelClass businessClass = new TopLevelClass(packageName + "." +shortName);

        // 获取目标路径
        String targetPath = context.getJavaModelGeneratorConfiguration()
                .getTargetProject() + "/"
                + businessClass.getType().getPackageName().replace('.', '/')
                + "/" + shortName + ".vue";

        // 创建父目录
        File file = new File(targetPath);
        file.getParentFile().mkdirs(); // 创建所有必要的父目录

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetPath))) {
            StringBuilder stringBuffer = new StringBuilder();
            StringBuilder builder = new StringBuilder();
            StringBuilder builder2 = new StringBuilder();
            StringBuilder builder3 = new StringBuilder();
            StringBuilder builder4 = new StringBuilder();

            // 添加字段的注释
            List<IntrospectedColumn> columns = introspectedTable.getAllColumns();

            // 你可以在这里定义一个排除字段的条件，例如按字段名或者其他属性来排除字段
            Set<String> excludedFields = isNotColumns();

            Map<String, String> map = new HashMap<>();
            List<Field> fields = topLevelClass.getFields();
            for (Field field : fields) {
                String[] split = field.getType().toString().split("\\.");
                map.put(field.getName(), split[split.length-1]);
            }

            IntrospectedColumn introspectedColumn = introspectedTable.getPrimaryKeyColumns().get(0);
            String actualColumnName = introspectedColumn.getActualColumnName();
            // 遍历所有字段，为每个字段添加注释
            int i = 1;
            for (IntrospectedColumn column : columns) {
                String columnName = column.getJavaProperty(); // 获取字段名称
                // 如果字段在排除列表中，则跳过此字段
                if (excludedFields.contains(columnName)) {
                    continue;
                }

                String columnComment = column.getRemarks();  // 获取字段的备注（注释）

                if (!actualColumnName.equals(column.getActualColumnName())){
                    String type = map.get(columnName);
                    if (type != null){

                        stringBuffer.append("<el-table-column prop=\"").append(columnName).append("\" label=\"").append(columnComment).append("\"/>\n");

                        if (i == 1){
                            this.getStringBuilder(builder, columnComment, columnName, type);
                        }
                        if (i == 2){
                            this.getStringBuilder(builder2, columnComment, columnName, type);
                        }
                        if (i == 3){
                            this.getStringBuilder(builder3, columnComment, columnName, type);
                        }
                        if (i == 4){
                            this.getStringBuilder(builder4, columnComment, columnName, type);
                        }
                        if (flFlag.equals(i +"")){
                            i = 0;
                        }
                        i++;
                    }
                }
            }

            String content = stringBuffer +"\n\n"
                    +builder +"\n"
                    +builder2 +"\n"
                    +builder3 +"\n"
                    +builder4 +"\n";
            writer.write(content);
        } catch (IOException e) {
            System.err.println("文件写入失败: " + e.getMessage());
        }
    }

    private void getStringBuilder(StringBuilder builder, String columnComment, String columnName, String str) {
        switch (str){
            case "BigDecimal":{
                builder.append("<el-form-item label=\"").append(columnComment).append("：\" label-width=\"40%\">\n")
                        .append("\t<el-input v-model=\"from.").append(columnName).append("\" autocomplete=\"off\" type=\"number\" style=\"width: 70%\"></el-input>\n")
                        .append("</el-form-item>\n");
                break;
            }
            case "Date":{
                builder.append("<el-form-item label=\" ").append(columnComment).append("+：\" label-width=\"40%\">\n")
                        .append("<el-date-picker v-model=\"from.").append(columnName).append("\" type=\"date\" placeholder=\"选择日期\" style=\"width: 70%\"></el-date-picker>\n")
                        .append("</el-form-item>\n");
                break;
            }
            default:{
                builder.append("<el-form-item label=\"").append(columnComment).append("：\" label-width=\"40%\">\n")
                        .append("\t<el-input v-model=\"from.").append(columnName).append("\" autocomplete=\"off\" style=\"width: 70%\"></el-input>\n")
                        .append("</el-form-item>\n");
                break;
            }
        }
    }

    private void generateCustomModelEntity(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String shortName = topLevelClass.getType().getShortName() + "Entity";
        String packageName = topLevelClass.getType().getPackageName() + ".entity";
        // 创建业务类

        TopLevelClass businessClass = new TopLevelClass(packageName + "." +shortName);

        // 获取目标路径
        String targetPath = context.getJavaModelGeneratorConfiguration()
                .getTargetProject() + "/"
                + businessClass.getType().getPackageName().replace('.', '/')
                + "/" + shortName + ".java";

        // 创建父目录
        File file = new File(targetPath);
        file.getParentFile().mkdirs(); // 创建所有必要的父目录

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetPath))) {
            StringBuilder stringBuffer = new StringBuilder();
            boolean dateTypeFlag = false;
            boolean bigDecimalTypeFlag = false;

            // 添加字段的注释
            List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
            IntrospectedColumn introspectedColumn = introspectedTable.getPrimaryKeyColumns().get(0);
            String actualColumnName = introspectedColumn.getActualColumnName();

            // 你可以在这里定义一个排除字段的条件，例如按字段名或者其他属性来排除字段
            Set<String> excludedFields = isNotColumns();

            Map<String, String> map = new HashMap<>();
            List<Field> fields = topLevelClass.getFields();
            for (Field field : fields) {
                String[] split = field.getType().toString().split("\\.");
                map.put(field.getName(), split[split.length-1]);
            }
            // 遍历所有字段，为每个字段添加注释
            for (IntrospectedColumn column : columns) {
                String columnName = column.getJavaProperty(); // 获取字段名称
                // 如果字段在排除列表中，则跳过此字段
                if (excludedFields.contains(columnName)) {
                    continue;
                }

                String columnComment = column.getRemarks();  // 获取字段的备注（注释）

                if (!actualColumnName.equals(column.getActualColumnName())){
                    String type = map.get(columnName);
                    if (type != null){
                        if (!dateTypeFlag){
                            dateTypeFlag = type.equals("Date");
                        }
                        if (!bigDecimalTypeFlag){
                            bigDecimalTypeFlag = type.equals("BigDecimal");
                        }
                        stringBuffer.append("\n\t@ExcelProperty(\"").append(columnComment).append("\")\n");
                        stringBuffer.append("\tprivate ").append(type).append(" ").append(columnName).append(";\n");
                    }
                }
            }

            String dateImport = dateTypeFlag ? "import java.util.Date;\n" : "";
            String bigDecimalImport = bigDecimalTypeFlag ? "import java.math.BigDecimal;\n" : "";

            String content = "package " +packageName +";\n" +
                    "\n" +
                    "import com.alibaba.excel.annotation.ExcelProperty;\n" +
                    dateImport +
                    bigDecimalImport +
                    "import lombok.Data;\n" +
                    "\n" +
                    "@Data\n" +
                    "public class " +shortName +" {\n" +
                    stringBuffer +
                    "}\n"
                    ;


            writer.write(content);
        } catch (IOException e) {
            System.err.println("文件写入失败: " + e.getMessage());
        }

        logger.warn("实体类：" + topLevelClass.getType().getShortName() + "Entity" + "已经重新生成注意字段变化");
    }

    private void generateCustomModel(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 清除字段的默认注释
        topLevelClass.getFields().forEach(field -> {
            // 清除所有注释
            field.getJavaDocLines().clear();
        });

        // 到入需要注解的包
        topLevelClass.addImportedType("org.hibernate.validator.constraints.Length");
        topLevelClass.addImportedType("jakarta.validation.constraints.NotNull");

        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addAnnotation("@Data");

        // 添加 Serializable 接口
        FullyQualifiedJavaType serializableType = new FullyQualifiedJavaType("java.io.Serializable");

        // 获取当前类已实现的接口
        Set<FullyQualifiedJavaType> importedTypes = topLevelClass.getImportedTypes();

        // 检查是否已经实现了 Serializable 接口
        boolean implementsSerializable = false;
        for (FullyQualifiedJavaType interfaceType : importedTypes) {
            if (interfaceType.getFullyQualifiedName().equals(serializableType.getFullyQualifiedName())) {
                implementsSerializable = true;
                break;
            }
        }

        // 如果没有实现 Serializable 接口，则添加它
        if (!implementsSerializable) {
            topLevelClass.addImportedType(serializableType);
            topLevelClass.addSuperInterface(serializableType);
        }

        // 添加domain的注释
        String shortName = topLevelClass.getType().getShortName();
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine("* @ClassName: " + shortName);
        topLevelClass.addJavaDocLine("* @Description: " + introspectedTable.getTableConfiguration() + "（" + introspectedTable.getRemarks() + "）");
        topLevelClass.addJavaDocLine("* @author: " + generatorUserName);
        topLevelClass.addJavaDocLine("* @date " + DateUtils.date2StringTime(new Date(), DateUtils.DATE_FORMAT_SECOND));
        topLevelClass.addJavaDocLine("*/");
        topLevelClass.addJavaDocLine("");

        // 删除 getter 和 setter 方法
        topLevelClass.getMethods().removeIf(method -> method.getName().startsWith("get") || method.getName().startsWith("set"));

        // 添加字段的注释
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();

        // 你可以在这里定义一个排除字段的条件，例如按字段名或者其他属性来排除字段
        Set<String> excludedFields = isNotColumns();

        IntrospectedColumn introspectedColumn = introspectedTable.getPrimaryKeyColumns().get(0);
        String actualColumnName = introspectedColumn.getActualColumnName();

        // 遍历所有字段，为每个字段添加注释
        for (IntrospectedColumn column : columns) {
            String columnName = column.getJavaProperty(); // 获取字段名称
            int length = column.getLength();
            // 如果字段在排除列表中，则跳过此字段
            if (excludedFields.contains(columnName)) {
                continue;
            }

            String columnComment = column.getRemarks();  // 获取字段的备注（注释）

            // 获取该字段的 Java 类成员变量
            List<Field> fields = topLevelClass.getFields();
            for (Field field : fields) {
                // 确保注释只添加到与字段匹配的成员变量
                if (field.getName().equals(columnName)) {
                    // 为字段添加 JavaDoc 注释
                    field.addJavaDocLine("/**");
                    field.addJavaDocLine(" * " + columnComment); // 注释内容
                    field.addJavaDocLine(" */");

                    if (!column.getActualColumnName().equals(actualColumnName)){
                        String[] split = field.getType().toString().split("\\.");
                        if (!split[split.length-1].equals("Date") && !split[split.length-1].equals("BigDecimal")){
                            if (!column.isNullable()){
                                field.addJavaDocLine("@NotNull(message = \"" +columnComment + "不能为空\")");
                            }
                            field.addAnnotation("@Length(max = " +length +", message = \"" +columnComment +"长度不能超过" +length +"\")");
       /*                     @Digits(integer = 8, fraction = 2, message = "整数部分最多8位，小数部分最多8位")
                                    field.addAnnotation("@Length(max = " +length +", message = \"" +columnComment +"长度不能超过" +length +"\")");*/
                        }
                    }
                }
            }
        }

        // 添加 serialVersionUID 属性
        Field serialVersionUIDField = new Field("serialVersionUID", new FullyQualifiedJavaType("long"));
        serialVersionUIDField.setStatic(true);
        serialVersionUIDField.setFinal(true);
        serialVersionUIDField.setInitializationString("1L");
        // 设置字段的可见性为 private
        serialVersionUIDField.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(serialVersionUIDField);

        logger.warn("实体类：" + shortName + "已经重新生成注意字段变化");
    }


    // 你可以在这里修改 Mapper 接口，例如添加注解、修改方法签名等
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        recordType = introspectedTable.getBaseRecordType();
        modelName = recordType.substring(recordType.lastIndexOf(".") + 1);

        String mapperName = mapperPackage + "." + modelName + "Mapper";
        File mapperFile = new File(targetProject +"\\" + mapperName.replace(".", "\\") +".java");
        if (!mapperFile.exists()) {
            // 获取要继承的接口类型，例如 BaseMapper
            FullyQualifiedJavaType baseMapperType = new FullyQualifiedJavaType(superMapper);

            // 将 BaseMapper<T> 作为泛型接口添加到当前 Mapper 接口中
            FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(modelName);
            baseMapperType.addTypeArgument(entityType);

            // 为当前的 Mapper 接口添加 BaseMapper 接口作为父类
            interfaze.addSuperInterface(baseMapperType);

            // 如果需要的话，添加必要的 import 语句
            interfaze.addImportedType(baseMapperType);
            interfaze.addImportedType(new FullyQualifiedJavaType(modelName));

            // 获取接口中所有的方法
            List<Method> methods = new ArrayList<>(interfaze.getMethods());
            // 遍历并删除方法
            for (Method method : methods) {
                interfaze.getMethods().remove(method);
            }
        }else {
            logger.info("mapper已存在，跳过");
            return false;
        }
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        String tableName = introspectedTable.getTableConfiguration().toString();
        List<IntrospectedColumn> baseColumns = introspectedTable.getAllColumns();
        // 将新的 SQL 语句添加到根元素
        XmlElement rootElement = document.getRootElement();
        // 批量新增
        rootElement.addElement(getXmlBatchInsertSelective(tableName, baseColumns));
        rootElement.addElement(getXmlBatchInsert(tableName, baseColumns));
        //批量修改
        rootElement.addElement(getXmlBatchUpdate(tableName, baseColumns));
        //批量删除
        rootElement.addElement(getXmlBatchDeleteByPrimaryKey(tableName, baseColumns));
        //where标签
        rootElement.addElement(getXmlWhere(tableName, baseColumns));
        //修改日期降序排序
        rootElement.addElement(getXmlOrderBy(tableName, baseColumns));
        //count查询
        rootElement.addElement(getXmlSelectCount(tableName, baseColumns));
        //map count查询
        rootElement.addElement(getXmlSelectCountByMap(tableName, baseColumns));
        //bean查询
        rootElement.addElement(getXmlSelectBean(tableName, baseColumns));
        //map查询
        rootElement.addElement(getXmlSelectByMap(tableName, baseColumns));
        //bean删除
        rootElement.addElement(getXmlDeleteBean(tableName, baseColumns));
        //map删除
        rootElement.addElement(getXmlDeleteByMap(tableName, baseColumns));

        logger.warn("记得手动清除xml中旧的批量方法");
        return true;
    }

    private VisitableElement getXmlDeleteByMap(String tableName, List<IntrospectedColumn> baseColumns) {
        XmlElement deleteByMapElement = new XmlElement("delete");
        deleteByMapElement.addAttribute(new Attribute("id", "deleteByMap"));
        deleteByMapElement.addAttribute(new Attribute("parameterType", "java.util.Map"));
        deleteByMapElement.addElement(new TextElement("DELETE FROM " +tableName));

        XmlElement includeWhereElement = new XmlElement("include");
        includeWhereElement.addAttribute(new Attribute("refid", "Base_Where"));

        deleteByMapElement.addElement(includeWhereElement);
        return deleteByMapElement;
    }

    private VisitableElement getXmlDeleteBean(String tableName, List<IntrospectedColumn> baseColumns) {
        XmlElement deleteBeanElement = new XmlElement("delete");
        deleteBeanElement.addAttribute(new Attribute("id", "deleteByBean"));
        deleteBeanElement.addAttribute(new Attribute("parameterType", recordType));
        deleteBeanElement.addElement(new TextElement("DELETE FROM " +tableName));

        XmlElement includeWhereElement = new XmlElement("include");
        includeWhereElement.addAttribute(new Attribute("refid", "Base_Where"));

        deleteBeanElement.addElement(includeWhereElement);
        return deleteBeanElement;
    }

    private XmlElement getXmlSelectBean(String tableName, List<IntrospectedColumn> baseColumns) {
        XmlElement selectBeanElement = new XmlElement("select");
        selectBeanElement.addAttribute(new Attribute("id", "selectListByBean"));
        selectBeanElement.addAttribute(new Attribute("parameterType", recordType));
        selectBeanElement.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        selectBeanElement.addElement(new TextElement("SELECT "));

        XmlElement includeListElement = new XmlElement("include");
        includeListElement.addAttribute(new Attribute("refid", "Base_Column_List"));

        XmlElement includeWhereElement = new XmlElement("include");
        includeWhereElement.addAttribute(new Attribute("refid", "Base_Where"));

        XmlElement includeOrderElement = new XmlElement("include");
        includeOrderElement.addAttribute(new Attribute("refid", "Base_OrderBy"));

        selectBeanElement.addElement(includeListElement);
        selectBeanElement.addElement(new TextElement("FROM " +tableName));
        selectBeanElement.addElement(includeWhereElement);
        selectBeanElement.addElement(includeOrderElement);

        return selectBeanElement;
    }

    private XmlElement getXmlSelectByMap(String tableName, List<IntrospectedColumn> baseColumns) {
        XmlElement selectBeanElement = new XmlElement("select");
        selectBeanElement.addAttribute(new Attribute("id", "selectListByMap"));
        selectBeanElement.addAttribute(new Attribute("parameterType", "java.util.Map"));
        selectBeanElement.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        selectBeanElement.addElement(new TextElement("SELECT "));

        XmlElement includeListElement = new XmlElement("include");
        includeListElement.addAttribute(new Attribute("refid", "Base_Column_List"));

        XmlElement includeWhereElement = new XmlElement("include");
        includeWhereElement.addAttribute(new Attribute("refid", "Base_Where"));

        XmlElement includeOrderElement = new XmlElement("include");
        includeOrderElement.addAttribute(new Attribute("refid", "Base_OrderBy"));

        selectBeanElement.addElement(includeListElement);
        selectBeanElement.addElement(new TextElement("FROM " +tableName));
        selectBeanElement.addElement(includeWhereElement);
        selectBeanElement.addElement(includeOrderElement);

        return selectBeanElement;
    }

    private XmlElement getXmlSelectCount(String tableName, List<IntrospectedColumn> baseColumns) {
        XmlElement selectCountElement = new XmlElement("select");
        selectCountElement.addAttribute(new Attribute("id", "selectCountByBean"));
        selectCountElement.addAttribute(new Attribute("parameterType", recordType));
        selectCountElement.addAttribute(new Attribute("resultType", "java.lang.Long"));
        selectCountElement.addElement(new TextElement("SELECT COUNT(1) FROM " +tableName));

        XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", "Base_Where"));

        selectCountElement.addElement(includeElement);
        return selectCountElement;
    }

    private XmlElement getXmlSelectCountByMap(String tableName, List<IntrospectedColumn> baseColumns) {
        XmlElement selectCountElement = new XmlElement("select");
        selectCountElement.addAttribute(new Attribute("id", "selectCountByMap"));
        selectCountElement.addAttribute(new Attribute("parameterType", "java.util.Map"));
        selectCountElement.addAttribute(new Attribute("resultType", "java.lang.Long"));
        selectCountElement.addElement(new TextElement("SELECT COUNT(1) FROM " +tableName));

        XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", "Base_Where"));

        selectCountElement.addElement(includeElement);
        return selectCountElement;
    }

    private XmlElement getXmlOrderBy(String tableName, List<IntrospectedColumn> baseColumns) {
        IntrospectedColumn column = baseColumns.get(0);

        XmlElement sqlElement = new XmlElement("sql");
        sqlElement.addAttribute(new Attribute("id", "Base_OrderBy"));
        sqlElement.addElement(new TextElement("ORDER BY XG_SJ DESC, " +column.getActualColumnName()));

        return sqlElement;
    }

    private XmlElement getXmlWhere(String tableName, List<IntrospectedColumn> baseColumns) {
        Set<String> excludedFields = isNotColumns();

        XmlElement sqlElement = new XmlElement("sql");
        sqlElement.addAttribute(new Attribute("id", "Base_Where"));

        XmlElement whereElement = new XmlElement("where");
        whereElement.addElement(new TextElement("ZF_BZ = 'N'"));
        for (IntrospectedColumn baseColumn : baseColumns) {
            String javaProperty = baseColumn.getJavaProperty();
            if (excludedFields.contains(javaProperty)) {
                continue;
            }
            String field = baseColumn.getActualColumnName();
            String jdbcTypeName = baseColumn.getJdbcTypeName();

            XmlElement ifElement = new XmlElement("if");
            if (jdbcTypeName.toLowerCase().contains("date")){
                ifElement.addAttribute(new Attribute("test", javaProperty + " != null"));
            }else {
                ifElement.addAttribute(new Attribute("test", javaProperty + " != null and " + javaProperty + " != ''"));
            }
            ifElement.addElement(new TextElement("AND " +field + " = #{" +javaProperty +"}"));
            whereElement.addElement(ifElement);
        }

        sqlElement.addElement(whereElement);
        return sqlElement;
    }

    private XmlElement getXmlBatchDeleteByPrimaryKey(String tableName, List<IntrospectedColumn> baseColumns) {
        IntrospectedColumn column = baseColumns.get(0);

        XmlElement deleteElement = new XmlElement("delete");
        deleteElement.addAttribute(new Attribute("id", "batchDeleteByPrimaryKey"));
        deleteElement.addAttribute(new Attribute("parameterType", "java.util.List"));
        deleteElement.addElement(new TextElement("DELETE FROM " +tableName));

        XmlElement whereElement = new XmlElement("where");
        whereElement.addElement(new TextElement("ZF_BZ = 'N'"));

        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "list != null and list.size() != 0"));
        ifElement.addElement(new TextElement( "AND " +column.getActualColumnName() +" IN"));

        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("close", ")"));
        foreachElement.addAttribute(new Attribute("open", "("));
        foreachElement.addAttribute(new Attribute("index", "idx"));
        foreachElement.addAttribute(new Attribute("item", "item"));
        foreachElement.addAttribute(new Attribute("separator", ","));
        foreachElement.addElement(new TextElement("#{item}"));

        ifElement.addElement(foreachElement);
        whereElement.addElement(ifElement);
        deleteElement.addElement(whereElement);
        return deleteElement;
    }

    private XmlElement getXmlBatchUpdate(String tableName, List<IntrospectedColumn> baseColumns) {
        XmlElement updateElement = new XmlElement("update");
        updateElement.addAttribute(new Attribute("id", "batchUpdate"));
        updateElement.addAttribute(new Attribute("parameterType", "java.util.List"));

        // 添加 foreach 循环
        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("index", "idx"));
        foreachElement.addAttribute(new Attribute("item", "item"));
        foreachElement.addAttribute(new Attribute("separator", ";"));
        foreachElement.addElement(new TextElement("UPDATE " +tableName));

        XmlElement columnsElement = new XmlElement("set");

        // 为你想要插入的字段列表
        for (int i = 0; i < baseColumns.size(); i++) {
            IntrospectedColumn baseColumn = baseColumns.get(i);
            String field = baseColumn.getActualColumnName();
            String javaProperty = baseColumn.getJavaProperty();
            String jdbcTypeName = baseColumn.getJdbcTypeName();
            boolean isLast = (i == baseColumns.size() - 1);

            XmlElement ifElement = new XmlElement("if");
            if (jdbcTypeName.toLowerCase().contains("date")){
                ifElement.addAttribute(new Attribute("test", "item." + javaProperty + " != null"));
            }else {
                ifElement.addAttribute(new Attribute("test", "item." + javaProperty + " != null and item." + javaProperty + " != ''"));
            }
            ifElement.addElement(new TextElement(field + " = #{item." +javaProperty +(isLast ? "}" : "},")));
            columnsElement.addElement(ifElement);
        }
        foreachElement.addElement(columnsElement);

        XmlElement whereElement = new XmlElement("where");
        IntrospectedColumn column = baseColumns.get(0);
        whereElement.addElement(new TextElement(column.getActualColumnName() +" = #{item." +column.getJavaProperty() +"}"));

        foreachElement.addElement(whereElement);

        updateElement.addElement(foreachElement);
        return updateElement;
    }

    private XmlElement getXmlBatchInsertSelective(String tableName, List<IntrospectedColumn> baseColumns) {

        XmlElement insertElement = new XmlElement("insert");
        insertElement.addAttribute(new Attribute("id", "batchInsertSelective"));
        insertElement.addAttribute(new Attribute("parameterType", "java.util.List"));

        // 添加 foreach 循环
        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("index", "idx"));
        foreachElement.addAttribute(new Attribute("item", "item"));
        foreachElement.addAttribute(new Attribute("separator", ";"));
        foreachElement.addElement(new TextElement("INSERT INTO " +tableName));

        // 构建插入列的部分
        XmlElement columnsElement = new XmlElement("trim");
        columnsElement.addAttribute(new Attribute("prefix", "("));
        columnsElement.addAttribute(new Attribute("suffix", ")"));
        columnsElement.addAttribute(new Attribute("suffixOverrides", ","));

        // fieldsToInsert 为你想要插入的字段列表
        for (int i = 0; i < baseColumns.size(); i++) {
            IntrospectedColumn baseColumn = baseColumns.get(i);
            String field = baseColumn.getActualColumnName();
            String javaProperty = baseColumn.getJavaProperty();
            String jdbcTypeName = baseColumn.getJdbcTypeName().toLowerCase();
            boolean isLast = (i == baseColumns.size() - 1);  // ✅ 判断是否最后一个

            XmlElement ifElement = new XmlElement("if");
            if (jdbcTypeName.contains("date") || jdbcTypeName.contains("timestamp") || jdbcTypeName.contains("decimal")) {
                ifElement.addAttribute(new Attribute("test", "item." + javaProperty + " != null"));
            } else {
                ifElement.addAttribute(new Attribute("test", "item." + javaProperty + " != null and item." + javaProperty + " != ''"));
            }
            // ✅ 最后一个字段不加逗号
            ifElement.addElement(new TextElement(field + (isLast ? "" : ",")));
            columnsElement.addElement(ifElement);
        }
        foreachElement.addElement(columnsElement);

        // 构建 values 部分
        XmlElement valuesElement = new XmlElement("trim");
        valuesElement.addAttribute(new Attribute("prefix", "values ("));
        valuesElement.addAttribute(new Attribute("suffix", ")"));
        valuesElement.addAttribute(new Attribute("suffixOverrides", ","));

        // 对应的值
        for (int i = 0; i < baseColumns.size(); i++) {
            IntrospectedColumn baseColumn = baseColumns.get(i);
            String field = baseColumn.getJavaProperty();
            String jdbcTypeName = baseColumn.getJdbcTypeName().toLowerCase();
            boolean isLast = (i == baseColumns.size() - 1);

            XmlElement ifElement = new XmlElement("if");
            if (jdbcTypeName.contains("date")){
                ifElement.addAttribute(new Attribute("test", "item." + field + " != null"));
            }else {
                ifElement.addAttribute(new Attribute("test", "item." + field + " != null and item." + field + " != ''"));
            }
            ifElement.addElement(new TextElement("#{item." + field + (isLast ? "}" : "},")));
            valuesElement.addElement(ifElement);
        }
        foreachElement.addElement(valuesElement);

        insertElement.addElement(foreachElement);

        return insertElement;
    }

    private XmlElement getXmlBatchInsert(String tableName, List<IntrospectedColumn> baseColumns) {

        XmlElement insertElement = new XmlElement("insert");
        insertElement.addAttribute(new Attribute("id", "batchInsert"));
        insertElement.addAttribute(new Attribute("parameterType", "java.util.List"));
        insertElement.addElement(new TextElement("INSERT INTO " +tableName +"("));

        // fieldsToInsert 为你想要插入的字段列表
        for (int i = 0; i < baseColumns.size(); i++) {
            IntrospectedColumn baseColumn = baseColumns.get(i);
            String field = baseColumn.getActualColumnName();
            boolean isLast = (i == baseColumns.size() - 1);

            insertElement.addElement(new TextElement(field + (isLast ? "" : ",")));
        }
        insertElement.addElement(new TextElement(" ) VALUES"));

        // 构建 values 部分
        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("item", "item"));
        foreachElement.addAttribute(new Attribute("separator", ","));
        foreachElement.addElement(new TextElement("("));

        // 对应的值
        for (int i = 0; i < baseColumns.size(); i++) {
            IntrospectedColumn baseColumn = baseColumns.get(i);
            String field = baseColumn.getJavaProperty();
            boolean isLast = (i == baseColumns.size() - 1);
            foreachElement.addElement(new TextElement("#{item." + field +  (isLast ? "}" : "},")));
        }
        foreachElement.addElement(new TextElement(")"));

        insertElement.addElement(foreachElement);

        return insertElement;
    }

    public Set<String> isNotColumns() {
        Set<String> excludedFields = new HashSet<>();
        excludedFields.add("cjRyDm");
        excludedFields.add("cjSj");
        excludedFields.add("xgRyDm");
        excludedFields.add("xgSj");
        excludedFields.add("sjgsJgDm");
        excludedFields.add("zfRyDm");
        excludedFields.add("zfSj");
        return excludedFields;
    }
}
