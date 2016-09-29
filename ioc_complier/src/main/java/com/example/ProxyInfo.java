package com.example;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by DongHao on 2016/9/28.
 * Description:
 */

public class ProxyInfo {
    private Elements elements;
    private TypeElement typeElement;
    public Map<Integer, VariableElement> mInjectElement;
    public String mPackageName;
    public String mProxyClassName;
    private String SUFFIX = "ViewInject";

    public ProxyInfo(Elements elements, TypeElement typeElement) {
        this.elements = elements;
        this.typeElement = typeElement;
        mInjectElement = new HashMap<Integer, VariableElement>();
        mProxyClassName = typeElement.getSimpleName().toString() + "$$" + SUFFIX;
        PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();
        mPackageName = packageElement.getQualifiedName().toString();
    }

    public ProxyInfo() {
    }

    public String getProxyClassFullNae() {
        return mPackageName + "." + mProxyClassName;
    }

    public Elements getElements() {
        return elements;
    }

    public void setElements(Elements elements) {
        this.elements = elements;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package " + mPackageName).append(";\n\n");
        builder.append("import com.dhao.ioc_api.ViewInject;\n");
        builder.append("public class ").append(mProxyClassName).append(" implements ViewInject" + "<" +
                typeElement.getQualifiedName().toString() + ">");
        builder.append("\n{\n");
        generateMethod(builder);
        return builder.toString();
    }

    private void generateMethod(StringBuilder builder) {
        builder.append("   @Override").append("\n");
        builder.append("public void inject(" + typeElement.getQualifiedName().toString() + " host,Object object)");
        builder.append("\n{\n");
        for (int id : mInjectElement.keySet()) {
            VariableElement variableElement = mInjectElement.get(id);
            String name = variableElement.getSimpleName().toString();
            String type = variableElement.asType().toString();

            builder.append("   if(object instanceof android.app.Activity)");
            builder.append("\n{\n");
            builder.append("   host." + name).append("=");
            builder.append("(" + type + ")(((android.app.Activity)object).findViewById(" + id + "));");
            builder.append("\n}\n").append("else").append("\n{\n");
            builder.append("   host." + name).append("=");
            builder.append("(" + type + ")(((android.view.View)object).findViewById(" + id + "));");
            builder.append("\n}\n");
        }
        builder.append("\n}\n").append("\n}\n");
    }

    public TypeSpec generateJavaCodePoet() {

        StringBuilder builder=new StringBuilder();
        for (int id : mInjectElement.keySet()) {
            VariableElement variableElement = mInjectElement.get(id);
            String name = variableElement.getSimpleName().toString();
            String type = variableElement.asType().toString();

            builder.append("   if(object instanceof android.app.Activity)");
            builder.append("\n{\n");
            builder.append("   host." + name).append("=");
            builder.append("(" + type + ")(((android.app.Activity)object).findViewById(" + id + "));");
            builder.append("\n}\n").append("else").append("\n{\n");
            builder.append("   host." + name).append("=");
            builder.append("(" + type + ")(((android.view.View)object).findViewById(" + id + "));");
            builder.append("\n}\n");
        }
        ClassName activity=ClassName.get(mPackageName,typeElement.getSimpleName().toString());
        MethodSpec inject = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addAnnotation(Override.class)
                .addParameter(activity, "host")
                .addParameter(Object.class, "object")
                .addCode(builder.toString())
                .build();

        ClassName viewInject=ClassName.get("com.dhao.ioc_api","ViewInject");
        TypeSpec activity$$ViewInject = TypeSpec.classBuilder(mProxyClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(viewInject)
                .addMethod(inject)
                .build();

        return activity$$ViewInject;
    }
}
