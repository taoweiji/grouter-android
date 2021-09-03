package router.component;

import com.grouter.compiler.ComponentModel;
import com.grouter.compiler.FileUtils;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import router.KotlinTypeUtils;

public class ComponentJavaSourceFileParser implements ComponentSourceFileParser {


    @Override
    public int parse(List<com.grouter.compiler.ComponentModel> componentModels, File file) {
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
            Annotation component = null;
            String protocolString = null;
            String packageName = compilationUnit.getPackage().getName().getFullyQualifiedName();
            // 分析该 Class 是否带有 @Component 注解
            for (Object modifierItem : modifiers) {
                if (!(modifierItem instanceof Annotation)) {
                    continue;
                }
                Annotation annotation = (Annotation) modifierItem;
                if (annotation.getTypeName().getFullyQualifiedName().equals("RouterComponent")) {
                    TypeLiteral typeLiteral = null;
                    if (annotation instanceof SingleMemberAnnotation) {
                        SingleMemberAnnotation singleMemberAnnotation = (SingleMemberAnnotation) annotation;
                        typeLiteral = (TypeLiteral) singleMemberAnnotation.getValue();
                    } else {
                        NormalAnnotation normalAnnotation = (NormalAnnotation) annotation;
                        List annotationParams = normalAnnotation.values();

                        for (Object object : annotationParams) {
                            MemberValuePair memberValuePair = (MemberValuePair) object;
                            if ("protocol".equals(memberValuePair.getName().getIdentifier())) {
                                typeLiteral = (TypeLiteral) memberValuePair.getValue();
                            }
                        }
                    }
                    if (typeLiteral == null) {
                        continue;
                    }
                    String inter = typeLiteral.getType().toString();
                    protocolString = importMap.get(inter);
                    if (protocolString == null) {
                        protocolString = packageName + "." + inter;
                    }
                    component = annotation;
                    break;
                }
            }
            // 如果没有那么就不处理
            if (component == null) {
                continue;
            }

            String className = typeDeclaration.getName().getFullyQualifiedName();
            ComponentModel componentModel = new ComponentModel();
            componentModels.add(componentModel);
            componentModel.protocol = protocolString;
            componentModel.implement = packageName + "." + className;
            modelCount++;

            MethodDeclaration[] methods = typeDeclaration.getMethods();
            boolean hasDefaultConstructor = false;
            int constructorCount = 0;

            for (MethodDeclaration item : methods) {
                if (!item.isConstructor()) {
                    continue;
                }
                constructorCount++;
                List fieldModifiers = item.modifiers();
                boolean hasRouterComponentConstructor = false;
                for (Object modifierItem : fieldModifiers) {
                    if (!(modifierItem instanceof Annotation)) {
                        continue;
                    }
                    Annotation annotation = (Annotation) modifierItem;
                    if (annotation.getTypeName().getFullyQualifiedName().equals("RouterComponentConstructor")) {
                        hasRouterComponentConstructor = true;
                        break;
                    }
                }
                if (!hasRouterComponentConstructor) {
                    continue;
                }

                modelCount++;
                List parameters = item.parameters();
                ComponentModel.ConstructorBean constructorBean = new ComponentModel.ConstructorBean();
                componentModel.constructors.add(constructorBean);
                for (Object object : parameters) {
                    SingleVariableDeclaration declaration = (SingleVariableDeclaration) object;
                    String type = declaration.getType().toString();
                    String name = declaration.getName().getIdentifier();
                    constructorBean.parameterTypes.add(KotlinTypeUtils.getTypeString(true, importMap, type));
                    constructorBean.parameterNames.add(name);
                }
            }
            if (constructorCount == 0){
                hasDefaultConstructor = true;
            }
            // 如果没有构造方法就使用默认的
            if (componentModel.constructors.size() == 0){
                if (hasDefaultConstructor){
                    ComponentModel.ConstructorBean constructorBean = new ComponentModel.ConstructorBean();
                    componentModel.constructors.add(constructorBean);
                }else {
                    // TODO 抛出异常（或者记录在日志中）
                }
            }
        }

        return modelCount;
    }
}
