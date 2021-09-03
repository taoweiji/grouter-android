package com.grouter.compiler;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class RouterBuildHelperTest {

    @Test
    public void getRouterModel() {
        File file = new File("/Users/Wiki/Documents/Workspace/joyrun/joyrun-android-dev/lib.base/src/main/java/com/grouter/data");
        RouterModel routerModel = RouterBuildHelper.getRouterModel(file,"","","",false);

    }
}