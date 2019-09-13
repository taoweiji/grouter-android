package com.grouter.hybrid;

import android.net.Uri;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class GRouterJavascriptInterfaceTest {

    @Test
    public void getTask() {
    }

    @Test
    public void startPage() {
    }

    @Test
    public void addAccessDomain() {
        List<String> accessDomains = new ArrayList<>();
        accessDomains.add("*.grouter.com");
        String currentUrl = "https://wap.grouter.com";

        if (currentUrl == null || currentUrl.length() == 0) {
            System.out.println("匹配失败");
            return ;
        }
        try {
            Uri uri = Uri.parse(currentUrl);
            String host = uri.getHost();
            for (String reg : accessDomains) {
                if (host.equals(reg)) {
                    System.out.println("匹配成功");
                    return ;
                }
                if (reg.startsWith("*") && currentUrl.contains(reg.substring(2))) {
                    System.out.println("匹配成功");
                    return ;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("匹配失败");
        return ;
    }
}