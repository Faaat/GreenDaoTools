package com.felix.complie;

import com.squareup.javapoet.ClassName;

public class ElementBean {
    public String beanName;
    public String beanPkgName;
    public ClassName beanClassName;
    public String utilsFileName;
    public String managerFileName;
    public String utilsFilePkgName;
    public ClassName utilsFileClassName;
    public ClassName managerFileClassName;


    public ElementBean(String beanName, String beanPkgName) {
        this.beanName = beanName;
        this.beanPkgName = beanPkgName;
        this.beanClassName = ClassName.get(beanPkgName, beanName);
        this.utilsFileName = beanName + "DaoUtils";
        this.managerFileName = "DaoManager";
        this.utilsFilePkgName = beanPkgName + ".utils";
        this.utilsFileClassName = ClassName.get(utilsFilePkgName, utilsFileName);
        this.managerFileClassName = ClassName.get(utilsFilePkgName, managerFileName);
    }
}
