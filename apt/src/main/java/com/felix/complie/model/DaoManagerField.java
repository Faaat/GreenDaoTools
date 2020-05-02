package com.felix.complie.model;

import com.felix.annotation.DaoUtils;
import com.felix.complie.ElementBean;
import com.felix.complie.interfaze.CreateDaoManager;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class DaoManagerField implements CreateDaoManager {
    private final TypeElement mTypeElement;
    private final String daoSrc;
    private final String daoName;

    public DaoManagerField(Element element) {
        if (element.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException(
                    String.format("Only class can be annotated with @%s",
                            DaoUtils.class.getSimpleName()));
        }
        mTypeElement = (TypeElement) element;
        daoSrc = mTypeElement.getAnnotation(DaoUtils.class).configuration()[0];
        daoName =
                mTypeElement.getAnnotation(DaoUtils.class).configuration().length > 1 ?
                        mTypeElement.getAnnotation(DaoUtils.class).configuration()[1] :
                        "greenDao";
    }

    @Override
    public FieldSpec doTag(ElementBean bean) {
        return FieldSpec.builder(String.class, "TAG", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$T.class.getSimpleName()", bean.utilsFileClassName).build();
    }

    @Override
    public FieldSpec doDB_NAME(ElementBean bean) {
        return FieldSpec.builder(String.class, "DB_NAME", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", daoName).build();
    }

    @Override
    public FieldSpec doContext(ElementBean bean) {
        return FieldSpec.builder(ClassName.get("android.content", "Context"), "context", Modifier.PRIVATE, Modifier.STATIC)
                .build();
    }


    @Override
    public FieldSpec doInstance(ElementBean bean) {
        return FieldSpec.builder(bean.managerFileClassName, "instance", Modifier.PRIVATE, Modifier.VOLATILE, Modifier.STATIC)
                .build();
    }

    @Override
    public FieldSpec doDaoMaster(ElementBean bean) {
        return FieldSpec.builder(ClassName.get(daoSrc, "DaoMaster"), "mDaoMaster",Modifier.PRIVATE)
                .build();
    }

    @Override
    public FieldSpec doDevOpenHelper(ElementBean bean) {
        return FieldSpec.builder(ClassName.get(daoSrc, "DaoMaster.DevOpenHelper"), "mHelper",Modifier.PRIVATE)
                .build();
    }

    @Override
    public FieldSpec doDaoSession(ElementBean bean) {
        return FieldSpec.builder(ClassName.get(daoSrc, "DaoSession"), "mDaoSession",Modifier.PRIVATE)
                .build();
    }

    @Override
    public MethodSpec doDaoManager(ElementBean bean) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build();
    }

    @Override
    public MethodSpec doInit(ElementBean bean) {
        return MethodSpec.methodBuilder("init")
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.context = context")
                .build();
    }


    @Override
    public MethodSpec doCloseConnection(ElementBean bean) {
        return MethodSpec.methodBuilder("closeConnection")
                .addModifiers(Modifier.PUBLIC)
                .addStatement(doCloseHelper(bean).name + "()")
                .addStatement(doCloseDaoSession(bean).name + "()")
                .build();
    }

    @Override
    public MethodSpec doCloseHelper(ElementBean bean) {
        return MethodSpec.methodBuilder("closeHelper")
                .addModifiers(Modifier.PUBLIC)
                .beginControlFlow("if(mHelper != null)")
                .addStatement("mHelper.close()")
                .addStatement("mHelper = null")
                .endControlFlow()
                .build();
    }

    @Override
    public MethodSpec doCloseDaoSession(ElementBean bean) {
        return MethodSpec.methodBuilder("closeDaoSession")
                .addModifiers(Modifier.PUBLIC)
                .beginControlFlow("if(mDaoSession != null)")
                .addStatement("mDaoSession.clear()")
                .addStatement("mDaoSession = null")
                .endControlFlow()
                .build();
    }

    @Override
    public MethodSpec doGetInstance(ElementBean bean) {
        return MethodSpec.methodBuilder("getInstance")
                .returns(bean.managerFileClassName)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .beginControlFlow("if (instance == null)")
                .beginControlFlow("synchronized ($N.class)", bean.managerFileName)
                .beginControlFlow("if (instance == null)")
                .addStatement("instance = new $N()", bean.managerFileName)
                .endControlFlow()
                .endControlFlow()
                .endControlFlow()
                .addStatement("return instance")
                .build();
    }

    @Override
    public MethodSpec doGetDaoMaster(ElementBean bean) {
        return MethodSpec.methodBuilder("getDaoMaster")
                .returns(ClassName.get(daoSrc, "DaoMaster"))
                .addModifiers(Modifier.PUBLIC)
                .beginControlFlow("if(mDaoMaster == null)")
                .addStatement("mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null)")
                .addStatement("mDaoMaster = new DaoMaster(mHelper.getWritableDatabase())")
                .endControlFlow()
                .addStatement("return mDaoMaster")
                .build();
    }

    @Override
    public MethodSpec doGetDaoSession(ElementBean bean) {
        return MethodSpec.methodBuilder("getDaoSession")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(daoSrc, "DaoSession"))
                .beginControlFlow("if(mDaoSession == null)")
                .beginControlFlow("if(mDaoMaster == null)")
                .addStatement("mDaoMaster = $N()", doGetDaoMaster(bean).name)
                .endControlFlow()
                .addStatement("mDaoSession = mDaoMaster.newSession()")
                .endControlFlow()
                .addStatement("return mDaoSession")
                .build();
    }

    @Override
    public MethodSpec doSetDebug(ElementBean bean) {
        return MethodSpec.methodBuilder("setDebug").addModifiers(Modifier.PUBLIC)
                .addParameter(boolean.class, "b")
                .addModifiers(Modifier.PUBLIC)
                .beginControlFlow("if(b)")
                .addStatement("org.greenrobot.greendao.query.QueryBuilder.LOG_SQL = true")
                .addStatement("org.greenrobot.greendao.query.QueryBuilder.LOG_VALUES = true")
                .endControlFlow()
                .build();
    }
}
