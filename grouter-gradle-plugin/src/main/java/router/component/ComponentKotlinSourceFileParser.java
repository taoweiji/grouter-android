package router.component;

import com.grouter.compiler.ComponentModel;

import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles;
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment;
import org.jetbrains.kotlin.com.intellij.openapi.Disposable;
import org.jetbrains.kotlin.com.intellij.openapi.vfs.StandardFileSystems;
import org.jetbrains.kotlin.com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.kotlin.com.intellij.psi.PsiElement;
import org.jetbrains.kotlin.com.intellij.psi.PsiFile;
import org.jetbrains.kotlin.com.intellij.psi.PsiManager;
import org.jetbrains.kotlin.config.CompilerConfiguration;
import org.jetbrains.kotlin.name.FqName;
import org.jetbrains.kotlin.name.Name;
import org.jetbrains.kotlin.psi.KtAnnotationEntry;
import org.jetbrains.kotlin.psi.KtClass;
import org.jetbrains.kotlin.psi.KtConstructor;
import org.jetbrains.kotlin.psi.KtImportDirective;
import org.jetbrains.kotlin.psi.KtImportList;
import org.jetbrains.kotlin.psi.KtPackageDirective;
import org.jetbrains.kotlin.psi.KtParameter;
import org.jetbrains.kotlin.psi.ValueArgument;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import router.KotlinTypeUtils;

public class ComponentKotlinSourceFileParser implements ComponentSourceFileParser {
    private static PsiManager psiManager;

    static {
        Disposable projectEnvironment = () -> {
        };
        CompilerConfiguration initialConfiguration = new CompilerConfiguration();
        KotlinCoreEnvironment kotlinCoreEnvironment = KotlinCoreEnvironment.createForTests(projectEnvironment, initialConfiguration, EnvironmentConfigFiles.JVM_CONFIG_FILES);
        psiManager = PsiManager.getInstance(kotlinCoreEnvironment.getProject());
    }

    @Override
    public int parse(List<com.grouter.compiler.ComponentModel> componentModels, File file) {
        int handleCount = 0;
        VirtualFile virtualFile = StandardFileSystems.local().findFileByPath(file.getAbsolutePath());
        if (virtualFile == null) {
            return 0;
        }
        PsiFile psiFile = psiManager.findFile(virtualFile);
        if (psiFile == null) {
            return 0;
        }
        PsiElement[] psiElements = psiFile.getChildren();
        if (psiElements.length == 0) {
            return 0;
        }
        String packageName = null;
        Map<String, String> importMap = new HashMap<>();
        for (PsiElement item : psiElements) {
            // 获取包名称
            if (item instanceof KtPackageDirective) {
                KtPackageDirective ktPackageDirective = (KtPackageDirective) item;
                packageName = ktPackageDirective.getQualifiedName();
            }
            // 用于解决对象的依赖
            if (item instanceof KtImportList) {
                KtImportList ktImportList = (KtImportList) item;
                List<KtImportDirective> imports = ktImportList.getImports();
                for (KtImportDirective ktImport : imports) {
                    FqName fqName = ktImport.getImportedFqName();
                    importMap.put(fqName.shortName().getIdentifier(), fqName.toString());
                }

            }
            // 分析类
            if (item instanceof KtClass) {
                KtClass ktClass = (KtClass) item;
                List<KtAnnotationEntry> annotationEntries = ktClass.getAnnotationEntries();
                if (annotationEntries.size() == 0) {
                    continue;
                }
                KtAnnotationEntry component = null;
                for (KtAnnotationEntry ktAnnotationEntry : annotationEntries) {
                    Name name = ktAnnotationEntry.getShortName();
                    if (name == null) {
                        continue;
                    }
                    if ("RouterComponent".equals(name.getIdentifier())) {
                        component = ktAnnotationEntry;
                    }

                }
                if (component == null) {
                    continue;
                }
                String protocolShortName = "";
                List<? extends ValueArgument> valueArguments = component.getValueArguments();
                for(ValueArgument valueArgument:valueArguments){
                    String name;
                    if (valueArgument.getArgumentName() != null){
                        name = valueArgument.getArgumentName().getAsName().getIdentifier();
                    }else {
                        break;
                    }
                    String protocolValue = valueArgument.getArgumentExpression().getText();
                    if ("protocol".equals(name)){
                        protocolShortName = protocolValue.replace("::class","");
                        break;
                    }
                }
                String protocol = importMap.get(protocolShortName);
                if (protocol == null) {
                    protocol = packageName + "." + protocolShortName;
                }
                String implement = packageName + "." + ktClass.getName();
                com.grouter.compiler.ComponentModel componentModel = new ComponentModel();
                componentModels.add(componentModel);
                componentModel.protocol = protocol;
                componentModel.implement = implement;

                List<KtConstructor> ktConstructors = new ArrayList<>();
                if (ktClass.getPrimaryConstructor() != null) {
                    ktConstructors.add(ktClass.getPrimaryConstructor());
                }
                ktConstructors.addAll(ktClass.getSecondaryConstructors());
                if (ktConstructors.size() == 0) {
                    ComponentModel.ConstructorBean constructorBean = new ComponentModel.ConstructorBean();
                    componentModel.constructors.add(constructorBean);
                    handleCount++;
                }
                boolean hasDefaultConstructor = false;
                int constructorCount = ktConstructors.size();
                for (KtConstructor ktConstructor : ktConstructors) {
                    List<KtAnnotationEntry> tmpAnnotationEntries = ktConstructor.getAnnotationEntries();
                    boolean hasRouterComponentConstructor = false;
                    for (KtAnnotationEntry ktAnnotationEntry : tmpAnnotationEntries) {
                        Name name = ktAnnotationEntry.getShortName();
                        if (name == null) {
                            continue;
                        }
                        if ("RouterComponentConstructor".equals(name.getIdentifier())) {
                            hasRouterComponentConstructor = true;
                        }
                    }
                    if (ktConstructor.getValueParameters().size() == 0){
                        hasDefaultConstructor = true;
                    }
                    if (!hasRouterComponentConstructor) {
                        continue;
                    }

                    List<KtParameter> parameters = ktConstructor.getValueParameters();
                    ComponentModel.ConstructorBean constructorBean = new ComponentModel.ConstructorBean();
                    componentModel.constructors.add(constructorBean);
                    handleCount++;
                    for (KtParameter parameter : parameters) {
                        String name = parameter.getName();
                        String type = parameter.getTypeReference().getText();
                        constructorBean.parameterNames.add(name);
                        constructorBean.parameterTypes.add(KotlinTypeUtils.getTypeString(true, importMap, type));
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
        }
        return handleCount;
    }
}
