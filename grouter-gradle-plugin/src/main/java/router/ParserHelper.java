package router;

import com.grouter.compiler.RouterBuildHelper;
import com.grouter.compiler.RouterModel;

import java.io.File;
import java.util.List;

import router.activity.ActivitySourceFileParserHelper;
import router.component.ComponentSourceFileHelper;
import router.delegate.DelegateSourceFileHelper;
import router.task.TaskSourceFileParserHelper;

public class ParserHelper {
    public static void parse(File jsonDir, List<File> files, String moduleName) {
        try {
            RouterModel routerModel = new RouterModel();
            routerModel.activityModels = ActivitySourceFileParserHelper.parse(files);
            routerModel.taskModels = TaskSourceFileParserHelper.parse(files);
            routerModel.componentModels = ComponentSourceFileHelper.parse(files);
            routerModel.delegateModels = DelegateSourceFileHelper.parse(files);
            RouterBuildHelper.saveRouterModel(jsonDir, routerModel, moduleName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
