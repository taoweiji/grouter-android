package router.delegate;

import com.grouter.compiler.FileUtils;
import com.grouter.compiler.RouterDelegateModel;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import router.KotlinTypeUtils;

public class DelegateJavaSourceFileParser implements DelegateSourceFileParser {


    @Override
    public int parse(List<RouterDelegateModel> delegateModels, File file) {
        String text = FileUtils.readToString(file);
        if (text == null) {
            return 0;
        }
        int modelCount = 0;
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(text.toCharArray());
        parser.setResolveBindings(true);
        CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
        List importsNode = compilationUnit.imports();
        Map<String, String> importMap = new HashMap<>();
        for (Object object : importsNode) {
            ImportDeclaration importDeclaration = (ImportDeclaration) object;
            QualifiedName qualifiedName = (QualifiedName) importDeclaration.getName();
            importMap.put(qualifiedName.getName().getIdentifier(), qualifiedName.getFullyQualifiedName());
//            qualifiedName.getQualifier().getFullyQualifiedName();
//            qualifiedName.getName().getIdentifier();
        }
        List types = compilationUnit.types();
        if (types.size() == 0) {
            return 0;
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
            Annotation routerDelegate = null;
//            String protocolString = null;
            String packageName = compilationUnit.getPackage().getName().getFullyQualifiedName();
            // 分析该 Class 是否带有 @Component 注解
            for (Object modifierItem : modifiers) {
                if (!(modifierItem instanceof Annotation)) {
                    continue;
                }
                Annotation annotation = (Annotation) modifierItem;
                if (annotation.getTypeName().getFullyQualifiedName().equals("RouterDelegate")) {
                    routerDelegate = annotation;
                }
            }
            // 如果没有那么就不处理
            if (routerDelegate == null) {
                continue;
            }
            RouterDelegateModel delegateModel = new RouterDelegateModel();
            delegateModels.add(delegateModel);
            String className = typeDeclaration.getName().getFullyQualifiedName();
            delegateModel.type = packageName + "." + className;
            modelCount++;

            MethodDeclaration[] methods = typeDeclaration.getMethods();
            boolean hasDefaultConstructor = false;
            int constructorCount = 0;
            for (MethodDeclaration item : methods) {
                if (item.isConstructor()) {
                    constructorCount++;
                    if (item.parameters().size() == 0) {
                        hasDefaultConstructor = true;
//                        continue;
                    }
                    List fieldModifiers = item.modifiers();
                    boolean hasRouterDelegateConstructor = false;
                    for (Object modifierItem : fieldModifiers) {
                        if (!(modifierItem instanceof Annotation)) {
                            continue;
                        }
                        Annotation annotation = (Annotation) modifierItem;
                        if (annotation.getTypeName().getFullyQualifiedName().equals("RouterDelegateConstructor")) {
                            hasRouterDelegateConstructor = true;
                            break;
                        }
                    }
                    if (!hasRouterDelegateConstructor) {
                        continue;
                    }
                    RouterDelegateModel.ConstructorBean constructorBean = new RouterDelegateModel.ConstructorBean();
                    delegateModel.constructor.add(constructorBean);
                    List parameters = item.parameters();
                    // 识别参数
                    if (item.parameters().size() > 0) {
                        for (Object object : parameters) {
                            SingleVariableDeclaration declaration = (SingleVariableDeclaration) object;
                            String type = declaration.getType().toString();
                            String name = declaration.getName().getIdentifier();
                            constructorBean.parameterTypes.add(KotlinTypeUtils.getTypeString(false, importMap, type));
                            constructorBean.parameterNames.add(name);
                        }
                    }
                } else {
                    // 处理代理方法
                    List fieldModifiers = item.modifiers();
                    boolean hasRouterDelegateMethod = false;
                    for (Object modifierItem : fieldModifiers) {
                        if (!(modifierItem instanceof Annotation)) {
                            continue;
                        }
                        Annotation annotation = (Annotation) modifierItem;
                        if (annotation.getTypeName().getFullyQualifiedName().equals("RouterDelegateMethod")) {
                            hasRouterDelegateMethod = true;
                            break;
                        }
                    }
                    if (!hasRouterDelegateMethod) {
                        continue;
                    }
                    RouterDelegateModel.MethodsBean methodsBean = new RouterDelegateModel.MethodsBean();
                    methodsBean.name = item.getName().getIdentifier();
                    methodsBean.returns = KotlinTypeUtils.getTypeString(false, importMap, item.getReturnType2().toString());
                    delegateModel.methods.add(methodsBean);
                    if (item.parameters().size() > 0) {
                        List parameters = item.parameters();
                        for (Object object : parameters) {
                            SingleVariableDeclaration declaration = (SingleVariableDeclaration) object;
                            String type = KotlinTypeUtils.getTypeString(false, importMap, declaration.getType().toString());
                            String name = declaration.getName().getIdentifier();
                            methodsBean.parameterTypes.add(type);
                            methodsBean.parameterNames.add(name);
                        }
                    }
                }
            }
            // 如果构造方法的数量等于0，那么就代表存在构造方法
            if (constructorCount == 0) {
                hasDefaultConstructor = true;
            }
            // 如果没有带有注解的构造方法，那么就判断是否有默认的构造方法
            if (delegateModel.constructor.size() == 0 && hasDefaultConstructor) {
                RouterDelegateModel.ConstructorBean constructorBean = new RouterDelegateModel.ConstructorBean();
                delegateModel.constructor.add(constructorBean);
            }
        }
        return modelCount;
    }
}
