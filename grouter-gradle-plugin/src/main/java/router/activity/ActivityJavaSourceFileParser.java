package router.activity;

import com.grouter.compiler.ActivityModel;
import com.grouter.compiler.FileUtils;
import com.grouter.compiler.ParameterModel;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.io.File;
import java.util.List;

public class ActivityJavaSourceFileParser implements ActivitySourceFileParser {


    @Override
    public int parse(List<ActivityModel> typeModels, File file) {
        int handleCount = 0;
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(FileUtils.readToString(file).toCharArray());
        parser.setResolveBindings(true);
        CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
        // 获取文件中包含的类
        List types = compilationUnit.types();
        // 如果没有包含类就返回 false

        if (types.size() == 0) {
            return handleCount;
        }
        for (Object typeItem : types) {
            if (!(typeItem instanceof TypeDeclaration)) {
                continue;
            }
            TypeDeclaration typeDeclaration = (TypeDeclaration) typeItem;
            List modifiers = typeDeclaration.modifiers();
            if (modifiers.size() == 0) {
                continue;
            }
            boolean hasRouterActivity = false;
            // 分析该 Class 是否带有 @RouterActivity 注解
            for (Object modifierItem : modifiers) {
                if (!(modifierItem instanceof Annotation)) {
                    continue;
                }
                Annotation annotation = (Annotation) modifierItem;
                if (annotation.getTypeName().getFullyQualifiedName().equals("RouterActivity")) {
                    hasRouterActivity = true;
                    break;
                }
            }
            // 如果没有那么就不处理
            if (!hasRouterActivity) {
                continue;
            }
            // 开始处理 Class
            String packageName = compilationUnit.getPackage().getName().getFullyQualifiedName();
            String className = typeDeclaration.getName().getFullyQualifiedName();
            ActivityModel typeModel = new ActivityModel();
            typeModels.add(typeModel);
            typeModel.type = packageName + "." + className;
            handleCount++;
            FieldDeclaration[] fieldDeclarations = typeDeclaration.getFields();
            for (FieldDeclaration field : fieldDeclarations) {
                List fieldModifiers = field.modifiers();
                boolean hasRouterField = false;
                for (Object modifierItem : fieldModifiers) {

                    if (!(modifierItem instanceof Annotation)) {
                        continue;
                    }
                    Annotation annotation = (Annotation) modifierItem;
                    if (annotation.getTypeName().getFullyQualifiedName().equals("RouterField")) {
                        hasRouterField = true;
                        break;
                    }
                }
                if (!hasRouterField) {
                    continue;
                }
                List fragments = field.fragments();
                for (Object fragmentItem : fragments) {
                    if (fragmentItem instanceof VariableDeclarationFragment) {
                        VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragmentItem;
                        ParameterModel typeMember = new ParameterModel();
                        typeModel.params.add(typeMember);
                        typeMember.name = fragment.getName().getIdentifier();
                        typeMember.type = "Object";
                        break;
                    }
                }
            }
        }
        return handleCount;
    }
}
