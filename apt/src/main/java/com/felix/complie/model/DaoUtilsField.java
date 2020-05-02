package com.felix.complie.model;

import com.felix.annotation.DaoUtils;
import com.felix.complie.ElementBean;
import com.felix.complie.interfaze.CreateEntityUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class DaoUtilsField implements CreateEntityUtils {
    private final TypeElement mTypeElement;
    private final String daoSrc;
    private final String daoName;

    public DaoUtilsField(Element element) {
        if (element.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException(
                    String.format("Only class can be annotated with @%s",
                            DaoUtils.class.getSimpleName()));
        }
        mTypeElement = (TypeElement) element;
        daoSrc = mTypeElement.getAnnotation(DaoUtils.class).configuration()[0];
        daoName = mTypeElement.getAnnotation(DaoUtils.class).configuration().length > 1 ?
                mTypeElement.getAnnotation(DaoUtils.class).configuration()[1] : "greenDao";
    }

    @Override
    public FieldSpec doTag(ElementBean bean) {
        return FieldSpec.builder(String.class, "TAG", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$T.class.getSimpleName()", bean.utilsFileClassName).build();
    }

    @Override
    public FieldSpec doDaoManager(ElementBean bean) {
        return FieldSpec
//                .builder(ClassName.get(daoSrc, "DaoManager")
                .builder(ClassName.get("com.felix.testapplication.utils", "DaoManager")
                        , "mManager", Modifier.PRIVATE)
                .build();
    }

    @Override
    public FieldSpec doInstance(ElementBean bean) {
        return FieldSpec.builder(bean.utilsFileClassName, "instance", Modifier.PRIVATE, Modifier.STATIC)
                .build();
    }

    @Override
    public MethodSpec doDaoUtils(ElementBean bean) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .addStatement("mManager = DaoManager.getInstance()")
                .addStatement("mManager.init(context)")
                .build();
    }

    /**
     * if (instance == null) {
     * synchronized (TeacherDaoUtils.class) {
     * if (instance == null) {
     * instance = new TeacherDaoUtils(context);
     * }
     * }
     * }
     * return instance;
     */
    @Override
    public MethodSpec doGetInstance(ElementBean bean) {
        return MethodSpec.methodBuilder("getInstance")
                .returns(bean.utilsFileClassName)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .beginControlFlow("if (instance == null)")
                .beginControlFlow("synchronized ($N.class)", bean.utilsFileName)
                .beginControlFlow("if (instance == null)")
                .addStatement("instance = new $N(context)", bean.utilsFileName)
                .endControlFlow()
                .endControlFlow()
                .endControlFlow()
                .addStatement("return instance")
                .build();
    }

    @Override
    public MethodSpec doInsert(ElementBean bean) {
        return MethodSpec.methodBuilder("insert")
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addJavadoc("完成$N记录的插入，如果表未创建，先创建$N表", bean.beanName, bean.beanName)
                .addJavadoc("\n")
                .addJavadoc("@param $N ", bean.beanName.toLowerCase())
                .addJavadoc("\n")
                .addJavadoc("@return 插入成功/失败")
                .addJavadoc("\n")
                .addParameter(bean.beanClassName, bean.beanName.toLowerCase())
                .addStatement("boolean flag")
                .addStatement("flag = mManager.getDaoSession().get$TDao().insert($N) == -1", bean.beanClassName, bean.beanName.toLowerCase())
                .addStatement("return flag")
                .build();
    }

    @Override
    public MethodSpec doUpdate(ElementBean bean) {
        return MethodSpec.methodBuilder("update")
                .addJavadoc("修改一条数据")
                .addJavadoc("\n")
                .addJavadoc("@param $N ", bean.beanName.toLowerCase())
                .addJavadoc("\n")
                .addJavadoc("@return 修改成功/失败")
                .addJavadoc("\n")
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(bean.beanClassName, bean.beanName.toLowerCase())
                .addStatement("boolean flag = false")
                .beginControlFlow("try ")
                .addStatement("mManager.getDaoSession().get$TDao().update($N)", bean.beanClassName, bean.beanName.toLowerCase())
                .addStatement("flag = true")
                .endControlFlow()
                .beginControlFlow("catch (Exception e)")
                .addStatement("e.printStackTrace()")
                .endControlFlow()
                .addStatement("return flag")
                .build();
    }

    @Override
    public MethodSpec doDelete(ElementBean bean) {
        return MethodSpec.methodBuilder("delete")
                .addJavadoc("删除单条数据")
                .addJavadoc("\n")
                .addJavadoc("@param $N ", bean.beanName.toLowerCase())
                .addJavadoc("\n")
                .addJavadoc("@return 删除成功/失败")
                .addJavadoc("\n")
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(bean.beanClassName, bean.beanName.toLowerCase())
                .addStatement("boolean flag = false")
                .beginControlFlow("try ")
                .addStatement("mManager.getDaoSession().get$TDao().delete($N)", bean.beanClassName, bean.beanName.toLowerCase())
                .addStatement("flag = true")
                .endControlFlow()
                .beginControlFlow("catch (Exception e)")
                .addStatement("e.printStackTrace()")
                .endControlFlow()
                .addStatement("return flag")
                .build();
    }

    @Override
    public MethodSpec doDeleteAll(ElementBean bean) {
        return MethodSpec.methodBuilder("deleteAll")
                .addJavadoc("删除所有记录")
                .addJavadoc("\n")
                .addJavadoc("@return 删除成功/失败")
                .addJavadoc("\n")
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(bean.beanClassName, bean.beanName.toLowerCase())
                .addStatement("boolean flag = false")
                .beginControlFlow("try ")
                .addStatement("mManager.getDaoSession().delete($N)", bean.beanClassName + ".class")
                .addStatement("flag = true")
                .endControlFlow()
                .beginControlFlow("catch (Exception e)")
                .addStatement("e.printStackTrace()")
                .endControlFlow()
                .addStatement("return flag")
                .build();
    }

    @Override
    public MethodSpec doQueryAll(ElementBean bean) {
        return MethodSpec.methodBuilder("queryAll")
                .addJavadoc("查询所有记录")
                .addJavadoc("\n")
                .addJavadoc("@return $N 集合", bean.beanName)
                .addJavadoc("\n")
                .addModifiers(Modifier.PUBLIC)
                .returns(List.class)
                .addStatement("return mManager.getDaoSession().loadAll($N.class)", bean.beanName)
                .build();
    }

    @Override
    public MethodSpec doQueryById(ElementBean bean) {
        return MethodSpec.methodBuilder("queryById")
                .addJavadoc("根据主键Id查询记录")
                .addJavadoc("\n")
                .addJavadoc("@return $N ", bean.beanName)
                .addJavadoc("\n")
                .addModifiers(Modifier.PUBLIC)
                .returns(bean.beanClassName)
                .addParameter(long.class, "key")
                .addStatement("return mManager.getDaoSession().load($N.class, key)", bean.beanName)
                .build();
    }

    @Override
    public MethodSpec doQueryByNativeSql(ElementBean bean) {
        return MethodSpec.methodBuilder("queryAll")
                .addJavadoc("使用 native sql 进行查询操作")
                .addJavadoc("\n")
                .addJavadoc("@return $N 集合", bean.beanName)
                .addJavadoc("\n")
                .addModifiers(Modifier.PUBLIC)
                .returns(List.class)
                .addParameter(String.class, "sql")
                .addParameter(String[].class, "conditions")
                .addStatement("return mManager.getDaoSession().queryRaw($N.class, sql, conditions)", bean.beanName)
                .build();
    }
}
