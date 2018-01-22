package com.laoyuegou.mvpgenerator

import org.gradle.api.Project
import org.gradle.api.Plugin

class MvpPlugin implements Plugin<Project> {
    void apply(Project target){

        target.extensions.create("Mvp", MvpExtension)

        target.task('generateMvp', type: MvpTask){
            group "mvpGenerator"
        }
    }
}