package router;

import com.alibaba.fastjson.JSON;
import com.grouter.compiler.RouterDelegateModel;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import router.delegate.DelegateKotlinSourceFileParser;

public class DelegateKotlinSourceFileParserTest {

    @Test
    public void parse() {
        router.delegate.DelegateKotlinSourceFileParser parser = new DelegateKotlinSourceFileParser();
        File file = new File("/Users/Wiki/Documents/Workspace/joyrun/ActivityRouter/example-base/src/main/java/com/grouter/demo/Account2Service.kt");
        List<RouterDelegateModel> delegateModels = new ArrayList<>();
        parser.parse(delegateModels, file);
        System.out.println(JSON.toJSONString(delegateModels, true));


//        File file = new File("/Users/Wiki/Documents/Workspace/joyrun/ActivityRouter/example-waimai/src/main/java/com/grouter/demo/other/AccountService.java");
//        List<RouterDelegateModel> delegateModels = new ArrayList<>();
//        DelegateJavaSourceFileParser parser = new DelegateJavaSourceFileParser();
//        parser.parse(delegateModels, file);
//        System.out.println(JSON.toJSONString(delegateModels, true));
    }
}