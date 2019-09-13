package router.delegate;

import com.grouter.compiler.RouterDelegateModel;

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
import org.jetbrains.kotlin.psi.KtNamedFunction;
import org.jetbrains.kotlin.psi.KtPackageDirective;
import org.jetbrains.kotlin.psi.KtParameter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import router.KotlinTypeUtils;

public class DelegateKotlinSourceFileParser implements DelegateSourceFileParser {
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
    public int parse(List<RouterDelegateModel> delegateModels, File file) {
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
                    if (fqName != null) {
                        importMap.put(fqName.shortName().getIdentifier(), fqName.toString());
                    }
                }

            }
            // 分析类
            if (item instanceof KtClass) {
                KtClass ktClass = (KtClass) item;
                List<KtAnnotationEntry> annotationEntries = ktClass.getAnnotationEntries();
                if (annotationEntries.size() == 0) {
                    continue;
                }
                KtAnnotationEntry routerDelegateAnnotation = null;
                for (KtAnnotationEntry ktAnnotationEntry : annotationEntries) {
                    Name name = ktAnnotationEntry.getShortName();
                    if (name == null) {
                        continue;
                    }
                    if ("RouterDelegate".equals(name.getIdentifier())) {
                        routerDelegateAnnotation = ktAnnotationEntry;
                    }

                }
                if (routerDelegateAnnotation == null) {
                    continue;
                }

                String implement = packageName + "." + ktClass.getName();
                RouterDelegateModel delegateModel = new RouterDelegateModel();
                delegateModels.add(delegateModel);
                delegateModel.type = implement;
                handleCount++;


                List<KtConstructor> ktConstructors = new ArrayList<>();
                if (ktClass.getPrimaryConstructor() != null) {
                    ktConstructors.add(ktClass.getPrimaryConstructor());
                }
                ktConstructors.addAll(ktClass.getSecondaryConstructors());
                boolean hasDefaultConstructor = false;
                for (KtConstructor ktConstructor : ktConstructors) {
                    if (ktConstructor.getTypeParameters().size() == 0) {
                        hasDefaultConstructor = true;
                    }
                    List<KtAnnotationEntry> tmpAnnotationEntries = ktConstructor.getAnnotationEntries();
                    boolean hasRouterDelegateConstructor = false;
                    for (KtAnnotationEntry ktAnnotationEntry : tmpAnnotationEntries) {
                        Name name = ktAnnotationEntry.getShortName();
                        if (name == null) {
                            continue;
                        }
                        if ("RouterDelegateConstructor".equals(name.getIdentifier())) {
                            hasRouterDelegateConstructor = true;
                        }
                    }
                    if (!hasRouterDelegateConstructor) {
                        continue;
                    }
                    RouterDelegateModel.ConstructorBean constructorBean = new RouterDelegateModel.ConstructorBean();
                    delegateModel.constructor.add(constructorBean);
                    List<KtParameter> parameters = ktConstructor.getValueParameters();
                    for (KtParameter parameter : parameters) {
                        String type = parameter.getTypeReference().getText();
                        String name = parameter.getName();
                        constructorBean.parameterNames.add(name);
                        constructorBean.parameterTypes.add(KotlinTypeUtils.getTypeString(true, importMap, type));
                    }
                }
                if (ktConstructors.size() == 0) {
                    hasDefaultConstructor = true;
                }
                if (hasDefaultConstructor && delegateModel.constructor.size() == 0) {
                    RouterDelegateModel.ConstructorBean constructorBean = new RouterDelegateModel.ConstructorBean();
                    delegateModel.constructor.add(constructorBean);
                }

                // 代理方法
                if (ktClass.getBody() == null) {
                    continue;
                }
                PsiElement[] children = ktClass.getBody().getChildren();
                for (PsiElement psiElement : children) {
                    if (psiElement instanceof KtNamedFunction) {
                        KtNamedFunction ktNamedFunction = (KtNamedFunction) psiElement;
                        List<KtAnnotationEntry> tmpAnnotationEntries = ktNamedFunction.getAnnotationEntries();
                        boolean hasRouterDelegateMethod = false;
                        for (KtAnnotationEntry ktAnnotationEntry : tmpAnnotationEntries) {
                            Name name = ktAnnotationEntry.getShortName();
                            if (name == null) {
                                continue;
                            }
                            if ("RouterDelegateMethod".equals(name.getIdentifier())) {
                                hasRouterDelegateMethod = true;
                            }
                        }
                        if (!hasRouterDelegateMethod) {
                            continue;
                        }
                        // 处理带有注解的方法
                        RouterDelegateModel.MethodsBean methodsBean = new RouterDelegateModel.MethodsBean();
                        methodsBean.name = ktNamedFunction.getName();
                        if (ktNamedFunction.getTypeReference() == null) {
                            methodsBean.returns = "void";
                        } else {
                            methodsBean.returns = KotlinTypeUtils.getTypeString(true, importMap, ktNamedFunction.getTypeReference().getText());
                        }

                        delegateModel.methods.add(methodsBean);
                        List<KtParameter> parameters = ktNamedFunction.getValueParameters();
                        for (KtParameter parameter : parameters) {
                            String type = parameter.getTypeReference().getText();
                            String name = parameter.getName();
                            methodsBean.parameterNames.add(name);
                            methodsBean.parameterTypes.add(KotlinTypeUtils.getTypeString(true, importMap, type));

                        }
                    }
                }
            }
        }
        return handleCount;
    }

}
