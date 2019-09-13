package router.delegate;

import com.grouter.compiler.RouterDelegateModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DelegateSourceFileHelper {
    private static DelegateJavaSourceFileParser javaSourceFileParser = new DelegateJavaSourceFileParser();
    private static DelegateKotlinSourceFileParser kotlinSourceFileParser = new DelegateKotlinSourceFileParser();

    public static void parse(List<RouterDelegateModel> componentModels, File file) {
        if (file.isFile()) {
            if (!hasRouterDelegateAnnotationQuick(file)) {
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

    private static boolean hasRouterDelegateAnnotationQuick(File file) {
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
                if (str.startsWith("@RouterDelegate")) {
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

    public static List<RouterDelegateModel> parse(List<File> files) {
        List<RouterDelegateModel> componentModels = new ArrayList<>();
        for (File file : files) {
            parse(componentModels, file);
        }
//        System.out.println(JSON.toJSONString(componentModels,true));
        Collections.sort(componentModels);
        return componentModels;
    }
}
