package com.felix.complie.interfaze;

import com.felix.complie.ElementBean;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
public interface CreateEntityUtils {
    FieldSpec doTag(ElementBean bean);

    FieldSpec doDaoManager(ElementBean bean);

    FieldSpec doInstance(ElementBean bean);

    MethodSpec doDaoUtils(ElementBean bean);

    MethodSpec doGetInstance(ElementBean bean);

    MethodSpec doInsert(ElementBean bean);

    MethodSpec doUpdate(ElementBean bean);

    MethodSpec doDelete(ElementBean bean);

    MethodSpec doDeleteAll(ElementBean bean);

    MethodSpec doQueryAll(ElementBean bean);

    MethodSpec doQueryById(ElementBean bean);

    MethodSpec doQueryByNativeSql(ElementBean bean);
}
