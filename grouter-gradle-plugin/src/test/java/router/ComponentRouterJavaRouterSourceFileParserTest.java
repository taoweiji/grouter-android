package router;

import com.alibaba.fastjson.JSON;
import com.grouter.compiler.ComponentModel;
import com.squareup.javapoet.MethodSpec;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import router.component.ComponentKotlinSourceFileParser;
import router.component.ComponentSourceFileHelper;

public class ComponentRouterJavaRouterSourceFileParserTest {
    private router.component.ComponentKotlinSourceFileParser parser = new ComponentKotlinSourceFileParser();

    @Test
    public void parse() {
        List<com.grouter.compiler.ComponentModel> componentModels = new ArrayList<>();
        parser.parse(componentModels, new File("/Users/Wiki/Documents/Workspace/joyrun/ActivityRouter/example/waimai/src/main/java/com/grouter/demo/other/service/AccountServiceImpl.kt"));

        System.out.println(JSON.toJSONString(componentModels, true));
    }

    @Test
    public void parseDirs() {
        List<ComponentModel> componentModels = new ArrayList<>();
//        String path = "/Users/Wiki/Documents/Workspace/joyrun/ActivityRouter/demo-other/src/main/java/com/grouter/demo/other";
        String path = "/Users/Wiki/Documents/Workspace/joyrun/ActivityRouter/demo/src/main/java/com/grouter/demo/service";
        ComponentSourceFileHelper.parse(componentModels, new File(path));
        System.out.println(JSON.toJSONString(componentModels, true));
    }



 @Test
    public void builder() {
     MethodSpec.Builder builder = MethodSpec.methodBuilder("test").addModifiers(Modifier.PUBLIC).addException(Exception.class);
     builder.addJavadoc("@return $N\n","dddddd");
     System.out.println(builder.build().toString());;


//     ComponentCodeBuilder.build("GComponentCenter",new File("/Users/Wiki/Documents/Workspace/joyrun/ActivityRouter/demo-base/src/main/router"));

    }





}