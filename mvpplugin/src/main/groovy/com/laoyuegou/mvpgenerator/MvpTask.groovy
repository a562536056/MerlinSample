package com.laoyuegou.mvpgenerator

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.text.SimpleDateFormat;

class MvpTask extends DefaultTask {

    @TaskAction
    def generateMvpFile() {

        def mvpExtension = project.extensions.getByType(MvpExtension)
        //应用ID
        def applicationId = mvpExtension.applicationId
        //包名
        def packageName = mvpExtension.packageName
        //功能名
        def functionName = mvpExtension.functionName
        //作者
        def author = mvpExtension.author
        //view是activity 还是 fragment
        def isViewActivity = mvpExtension.isViewActivity

        def mvpArray = [
                [
                        templateName : "MvpContract.template",
                        type : "contract",
                        fileName : "Contract.java"
                ],
                [
                        templateName : "MvpPresenter.template",
                        type : "presenter",
                        fileName : "Presenter.java"
                ]
        ]

        if (isViewActivity){
            mvpArray.add([
                    templateName : "MvpActivity.template",
                    type : "activity",
                    fileName : "Activity.java"
            ])
        } else {
            mvpArray.add([
                    templateName : "MvpFragment.template",
                    type : "fragment",
                    fileName : "Fragment.java"
            ])
        }

        String dateString = getFormatTime();

        def mBinding = [
                applicaitionId  : applicationId,
                packageName     : packageName,
                functionName    : functionName,
                date            : dateString,
                author          : author
        ];

        def packageFilePath = mvpExtension.applicationId.replace(".", "/");

        //代码文件根路径
        def fullPath =  project.projectDir.toString() + "/src/main/java/" + packageFilePath

        generateMvpFile(mvpArray, mBinding, fullPath)

    }

    void generateMvpFile(def mvpArray, def binding, def fullPath){

        for(int i = 0; i < mvpArray.size(); i++){
            preGenerateFile(mvpArray[i], binding, fullPath)
        }
    }

    void preGenerateFile(def map, def binding, def fullPath){
        // File mvpContractTemplateFile = new File("template/" + map.templateFileName)

//        println "preGenerateFile : map.templateName=" + map.templateName
//        println "preGenerateFile : map.type=" + map.type
//        println "preGenerateFile : map.fileName=" + map.fileName
        def template = makeTemplate(map.templateName, binding);
        def path = fullPath + "/" + binding.packageName + "/" + map.type;
        def fileName = path + "/" + binding.functionName + map.fileName

        generateFile(path, fileName, template)
    }

    /**
     * 加载模板
     */
    def makeTemplate(def templateName, def binding){

        File f = new File("./buildSrc/mvpplugin/template/" + templateName);

        def engine = new groovy.text.GStringTemplateEngine()

        return engine.createTemplate(f).make(binding)
    }

    /**
     * 生成文件
     * @param path
     * @param fileName
     * @param template
     */
    void generateFile(def path, def fileName, def template){
        //验证文件路径，没有则创建
        validatePath(path);

        File mvpFile = new File(fileName);

        //如果文件已经存在，直接返回
        if(!mvpFile.exists()){
            mvpFile.createNewFile()
        } else {
            return;
        }

        FileOutputStream out = new FileOutputStream(mvpFile, false)
        out.write(template.toString().getBytes("utf-8"))
        out.close()
    }

    /**
     * 验证文件路径，没有则创建
     * @param path
     */
    void validatePath(def path){
        File mvpFileDir = new File(path);

        if(!mvpFileDir.exists()){
            mvpFileDir.mkdirs()
        }
    }

    /**
     * 格式化当前时间
     * @return
     */
    def getFormatTime(){
        Date date = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
}