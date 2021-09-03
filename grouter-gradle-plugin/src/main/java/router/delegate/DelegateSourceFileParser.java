package router.delegate;

import com.grouter.compiler.RouterDelegateModel;

import java.io.File;
import java.util.List;

public interface DelegateSourceFileParser {
    int parse(List<RouterDelegateModel> componentModels, File file);
}
