package router.task;

import com.grouter.compiler.TaskModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskSourceFileParserHelper {

    /**
     * @return 是否带有 @RouterActivity 注解
     */
    public static boolean hasRouterActivityAnnotationQuick(File file) {
        // 如果不是Java或者Kotlin文件直接返回false
        if (!file.getName().endsWith(".java") && !file.getName().endsWith(".kt")) {
            return false;
        }
        boolean hasRouterActivity = false;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                str = str.trim();
                if (str.startsWith("@RouterTask")) {
                    hasRouterActivity = true;
                    break;
                }
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hasRouterActivity;
    }

    private static TaskKotlinSourceFileParser routerKotlinSourceFileParser = new TaskKotlinSourceFileParser();
    private static TaskJavaSourceFileParser routerJavaSourceFileParser = new TaskJavaSourceFileParser();

    public static List<TaskModel> parse(List<File> files) {
        List<TaskModel> typeModels = new ArrayList<>();
        for (File file : files) {
            parse(typeModels, file);
        }
        Collections.sort(typeModels);
        return typeModels;
    }

    public static void parse(List<TaskModel> typeModels, File file) {
        if (file.isFile()) {
            if (!hasRouterActivityAnnotationQuick(file)) {
                return;
            }
            int handleCount;
            if (file.getName().endsWith(".kt")) {
                handleCount = routerKotlinSourceFileParser.parse(typeModels, file);
            } else {
                handleCount = routerJavaSourceFileParser.parse(typeModels, file);
            }
            if (handleCount > 0) {
                System.out.println("GRouter fix: " + file.getAbsolutePath());
            }
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File item : files) {
                    try {
                        parse(typeModels, item);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
