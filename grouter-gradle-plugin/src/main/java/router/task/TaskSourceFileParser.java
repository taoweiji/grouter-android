package router.task;

import com.grouter.compiler.TaskModel;

import java.io.File;
import java.util.List;

public interface TaskSourceFileParser {
    int parse(List<TaskModel> typeModels, File file);
}
