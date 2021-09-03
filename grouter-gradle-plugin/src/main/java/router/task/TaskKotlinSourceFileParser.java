package router.task;

import com.grouter.compiler.ParameterModel;
import com.grouter.compiler.TaskModel;

import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles;
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment;
import org.jetbrains.kotlin.com.intellij.openapi.Disposable;
import org.jetbrains.kotlin.com.intellij.openapi.vfs.StandardFileSystems;
import org.jetbrains.kotlin.com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.kotlin.com.intellij.psi.PsiElement;
import org.jetbrains.kotlin.com.intellij.psi.PsiFile;
import org.jetbrains.kotlin.com.intellij.psi.PsiManager;
import org.jetbrains.kotlin.config.CompilerConfiguration;
import org.jetbrains.kotlin.name.Name;
import org.jetbrains.kotlin.psi.KtAnnotationEntry;
import org.jetbrains.kotlin.psi.KtClass;
import org.jetbrains.kotlin.psi.KtImportDirective;
import org.jetbrains.kotlin.psi.KtImportList;
import org.jetbrains.kotlin.psi.KtPackageDirective;
import org.jetbrains.kotlin.psi.KtProperty;

import java.io.File;
import java.util.List;

public class TaskKotlinSourceFileParser implements TaskSourceFileParser {
    private static PsiManager psiManager;

    static {
        Disposable projectEnvironment = () -> {
        };
        CompilerConfiguration initialConfiguration = new CompilerConfiguration();
        KotlinCoreEnvironment kotlinCoreEnvironment = KotlinCoreEnvironment.createForTests(projectEnvironment, initialConfiguration, EnvironmentConfigFiles.JVM_CONFIG_FILES);
        psiManager = PsiManager.getInstance(kotlinCoreEnvironment.getProject());
//        JavaCoreProjectEnvironment
    }


    @Override
    public int parse(List<TaskModel> typeModels, File file) {
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
            }
            // 分析类
            if (item instanceof KtClass) {
                KtClass ktClass = (KtClass) item;
                List<KtAnnotationEntry> annotationEntries = ktClass.getAnnotationEntries();
                if (annotationEntries.size() == 0) {
                    continue;
                }
                KtAnnotationEntry routerActivity = null;
                for (KtAnnotationEntry ktAnnotationEntry : annotationEntries) {
                    Name name = ktAnnotationEntry.getShortName();
                    if (name == null) {
                        continue;
                    }
                    if ("RouterTask".equals(name.getIdentifier())) {
                        routerActivity = ktAnnotationEntry;
                    }

                }
                if (routerActivity == null) {
                    continue;
                }
                TaskModel typeModel = new TaskModel();
                typeModels.add(typeModel);
                typeModel.type = packageName + "." + ktClass.getName();
                handleCount++;
                List<KtProperty> properties = ktClass.getProperties();
                for (KtProperty property : properties) {
                    List<KtAnnotationEntry> tmpAnnotationEntries = property.getAnnotationEntries();
                    if (tmpAnnotationEntries.size() == 0) {
                        continue;
                    }
                    KtAnnotationEntry routerField = null;
                    for (KtAnnotationEntry ktAnnotationEntry : tmpAnnotationEntries) {
                        Name name = ktAnnotationEntry.getShortName();
                        if (name == null) {
                            continue;
                        }
                        if ("RouterField".equals(name.getIdentifier())) {
                            routerField = ktAnnotationEntry;
                        }
                    }
                    if (routerField == null) {
                        continue;
                    }
                    if (property.getTypeReference() == null) {
                        continue;
                    }
                    ParameterModel typeMember = new ParameterModel();
                    typeModel.params.add(typeMember);
                    typeMember.name = property.getName();
                    typeMember.type = "Object";
                }
            }
        }
        return handleCount;
    }

}
