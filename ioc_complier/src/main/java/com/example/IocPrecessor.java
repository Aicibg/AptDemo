package com.example;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

/**
 * Created by DongHao on 2016/9/28.
 * Description:
 */
@AutoService(Processor.class)
public class IocPrecessor extends AbstractProcessor {
    private Filer mFilerUtils;
    private Elements mElementUtils;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFilerUtils=processingEnv.getFiler();
        mElementUtils =processingEnv.getElementUtils();
        messager=processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
//        Set<String> annotationTypes=new LinkedHashSet<String>();
//        annotationTypes.add(BindView.class.getCanonicalName());
//        return annotationTypes;
        return Collections.singleton(BindView.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private Map<String,ProxyInfo> proxyInfoMap=new HashMap<String,ProxyInfo>();
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
       proxyInfoMap.clear();
       Set<? extends Element> elements=roundEnv.getElementsAnnotatedWith(BindView.class);

        for (Element element:elements){
            if (element.getKind()!= ElementKind.FIELD){
                return false;
            }
            VariableElement variableElement= (VariableElement) element;
            TypeElement typeElement= (TypeElement) variableElement.getEnclosingElement();
            String qualifiedName=typeElement.getQualifiedName().toString();

            ProxyInfo proxyInfo=proxyInfoMap.get(qualifiedName);
            if (proxyInfo==null){
                proxyInfo=new ProxyInfo(mElementUtils,typeElement);
                proxyInfoMap.put(qualifiedName,proxyInfo);
            }
            BindView annotation=variableElement.getAnnotation(BindView.class);
            int id=annotation.value();
            proxyInfo.mInjectElement.put(id,variableElement);
        }

        for (String key:proxyInfoMap.keySet()){
            ProxyInfo proxyInfo=proxyInfoMap.get(key);
            try {
                JavaFileObject sourceFile=mFilerUtils.createSourceFile(proxyInfo.getProxyClassFullNae(),
                        proxyInfo.getTypeElement());
                Writer writer=sourceFile.openWriter();
                writer.write(proxyInfo.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
