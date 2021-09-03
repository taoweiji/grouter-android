package com.grouter.hybrid;

import android.app.Activity;
import android.net.Uri;
import androidx.annotation.Keep;
import android.webkit.JavascriptInterface;

import com.grouter.GRouter;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class GRouterJavascriptInterface {
    protected Activity activity;

    protected List<String> accessDomains = new ArrayList<>();
    protected boolean onlyHttps = false;
    protected String url;

    protected GRouterJavascriptInterface onlyHttps(boolean onlyHttps) {
        this.onlyHttps = onlyHttps;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public GRouterJavascriptInterface(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    @Keep
    public final String executeTask(String url) {
        if (!hasAccessPermission()) {
            return "error";
        }
        return GRouter.getInstance().taskBuilder(url).execute().string();
    }

    @JavascriptInterface
    @Keep
    public final void startPage(String url) {
        if (!hasAccessPermission()) {
            return;
        }
        GRouter.getInstance().startActivity(this.activity, url);
    }

    public boolean hasAccessPermission() {
        if (accessDomains.size() == 0) {
            return true;
        }
        if (url == null || url.length() == 0) {
            return false;
        }
        try {
            Uri uri = Uri.parse(url);
            String host = uri.getHost();
            if (onlyHttps && uri.getScheme() != null && uri.getScheme().equals("http")) {
                return false;
            }
            if (host == null) {
                return false;
            }
            for (String reg : accessDomains) {
                if (host.equals(reg)) {
                    return true;
                }
                if (reg.startsWith("*") && url.contains(reg.substring(2))) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 允许访问的
     *
     * @param domain *.grouter.com,wap.grouter.com
     */
    public GRouterJavascriptInterface addAccessDomain(String domain) {
        accessDomains.add(domain);
        return this;
    }
}
