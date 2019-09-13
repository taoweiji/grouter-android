package router.component;

import com.grouter.compiler.ComponentModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentSourceFileHelper {
    private static ComponentJavaSourceFileParser javaSourceFileParser = new ComponentJavaSourceFileParser();
    private static ComponentKotlinSourceFileParser kotlinSourceFileParser = new ComponentKotlinSourceFileParser();

    public static void parse(List<com.grouter.compiler.ComponentModel> componentModels, File file) {
        if (file.isFile()) {
            if (!hasRouterComponentAnnotationQuick(file)) {
                return;
            }

            int handleCount;
            if (file.getName().endsWith(".kt")) {
                handleCount = kotlinSourceFileParser.parse(componentModels, file);
            } else {
                handleCount = javaSourceFileParser.parse(componentModels, file);
            }
            if (handleCount > 0) {
                System.out.println("GRouter fix: " + file.getAbsolutePath());
            }

        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File item : files) {
                    try {
                        parse(componentModels, item);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static boolean hasRouterComponentAnnotationQuick(File file) {
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
                if (str.startsWith("@RouterComponent")) {
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

    public static List<ComponentModel> parse(List<File> files) {
        List<ComponentModel> componentModels = new ArrayList<>();
        for (File file : files) {
            parse(componentModels, file);
        }
        Collections.sort(componentModels);
        return componentModels;
    }
}
