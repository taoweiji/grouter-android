package router.activity;

import com.grouter.compiler.ActivityModel;

import java.io.File;
import java.util.List;

public interface ActivitySourceFileParser {
    int parse(List<ActivityModel>  typeModels, File file);
}
