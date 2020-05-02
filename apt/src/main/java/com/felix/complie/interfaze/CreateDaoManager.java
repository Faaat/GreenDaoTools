package com.felix.complie.interfaze;

import com.felix.complie.ElementBean;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

public interface CreateDaoManager {
    FieldSpec doTag(ElementBean bean);

    FieldSpec doDB_NAME(ElementBean bean);

    FieldSpec doContext(ElementBean bean);

    FieldSpec doDaoMaster(ElementBean bean);

    FieldSpec doInstance(ElementBean bean);

    FieldSpec doDevOpenHelper(ElementBean bean);

    FieldSpec doDaoSession(ElementBean bean);



    MethodSpec doGetInstance(ElementBean bean);
    MethodSpec doDaoManager(ElementBean bean);
    MethodSpec doInit(ElementBean bean);
    MethodSpec doGetDaoMaster(ElementBean bean);
    MethodSpec doGetDaoSession(ElementBean bean);
    MethodSpec doSetDebug(ElementBean bean);
    MethodSpec doCloseConnection(ElementBean bean);

    MethodSpec doCloseHelper(ElementBean bean);

    MethodSpec doCloseDaoSession(ElementBean bean);





}
