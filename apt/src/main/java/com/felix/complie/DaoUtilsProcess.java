package com.felix.complie;


import com.felix.annotation.DaoUtils;
import com.felix.complie.model.DaoManagerField;
import com.felix.complie.model.DaoUtilsField;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;


@AutoService(Processor.class)
@SupportedAnnotationTypes("com.felix.annotation.DaoUtils")
public class DaoUtilsProcess extends AbstractProcessor {
    //打印消息类
    private Messager messager;

    //最终的生成的文件要通过这个写入进去
    private Filer filer;
    private Elements mElementUtils;
    private boolean isWriteManage = false;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        mElementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) throws IllegalArgumentException {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(DaoUtils.class)) {
            ElementBean bean = new ElementBean(element.getSimpleName().toString(), element.getEnclosingElement().toString());
            DaoUtilsField utilsField = new DaoUtilsField(element);
            DaoManagerField managerField = new DaoManagerField(element);
            //生成DaoManager
            TypeSpec manager = TypeSpec.classBuilder(bean.managerFileName)
                    .addModifiers(Modifier.PUBLIC)
                    .addField(managerField.doTag(bean))
                    .addField(managerField.doDB_NAME(bean))
                    .addField(managerField.doContext(bean))
                    .addField(managerField.doInstance(bean))
                    .addField(managerField.doDaoMaster(bean))
                    .addField(managerField.doDevOpenHelper(bean))
                    .addField(managerField.doDaoSession(bean))
                    .addMethod(managerField.doGetInstance(bean))
                    .addMethod(managerField.doDaoManager(bean))
                    .addMethod(managerField.doInit(bean))
                    .addMethod(managerField.doGetDaoMaster(bean))
                    .addMethod(managerField.doGetDaoSession(bean))
                    .addMethod(managerField.doSetDebug(bean))
                    .addMethod(managerField.doCloseConnection(bean))
                    .addMethod(managerField.doCloseDaoSession(bean))
                    .addMethod(managerField.doCloseHelper(bean))
                    .build();
            //生成UserDaoUtils
            TypeSpec daoUtils = TypeSpec.classBuilder(bean.utilsFileName)
                    .addModifiers(Modifier.PUBLIC)
                    .addField(utilsField.doTag(bean))
                    .addField(utilsField.doInstance(bean))
                    .addField(utilsField.doDaoManager(bean))
                    .addMethod(utilsField.doGetInstance(bean))
                    .addMethod(utilsField.doDaoUtils(bean))
                    .addMethod(utilsField.doInsert(bean))
                    .addMethod(utilsField.doUpdate(bean))
                    .addMethod(utilsField.doDelete(bean))
                    .addMethod(utilsField.doDeleteAll(bean))
                    .addMethod(utilsField.doQueryAll(bean))
                    .addMethod(utilsField.doQueryById(bean))
                    .addMethod(utilsField.doQueryByNativeSql(bean))
                    .build();

            try {
                if (!isWriteManage) {
                    JavaFile javaFile1 = JavaFile.builder(bean.utilsFilePkgName, manager)
                            .addFileComment(" This codes are generated automatically. Do not modify!")
                            .build();
                    // 写入文件
                    javaFile1.writeTo(filer);
                    isWriteManage = !isWriteManage;
                }
                JavaFile javaFile = JavaFile.builder(bean.utilsFilePkgName, daoUtils)
                        .addFileComment(" This codes are generated automatically. Do not modify!")
                        .build();
                // 写入文件
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
