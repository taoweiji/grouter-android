package router.component;

import com.grouter.compiler.ComponentModel;

import java.io.File;
import java.util.List;

public interface ComponentSourceFileParser {
    int parse(List<ComponentModel> componentModels, File file);
}
