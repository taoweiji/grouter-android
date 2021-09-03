package router.activity;

import com.grouter.compiler.ActivityModel;
import com.grouter.compiler.ParameterModel;

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
import org.jetbrains.kotlin.psi.KtPackageDirective;
import org.jetbrains.kotlin.psi.KtProperty;

import java.io.File;
import java.util.List;

public class ActivityKotlinSourceFileParser implements ActivitySourceFileParser {
    private static PsiManager psiManager;

    static {
        Disposable projectEnvironment = () -> {
        };
        CompilerConfiguration initialConfiguration = new CompilerConfiguration();
        KotlinCoreEnvironment kotlinCoreEnvironment = KotlinCoreEnvironment.createForTests(projectEnvironment, initialConfiguration, EnvironmentConfigFiles.JVM_CONFIG_FILES);
        psiManager = PsiManager.getInstance(kotlinCoreEnvironment.getProject());
    }


    @Override
    public int parse(List<ActivityModel> typeModels, File file) {
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
                    if ("RouterActivity".equals(name.getIdentifier())) {
                        routerActivity = ktAnnotationEntry;
                    }
                }
                if (routerActivity == null) {
                    continue;
                }
                ActivityModel typeModel = new ActivityModel();
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
