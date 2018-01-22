package com.laoyuegou.mvpgenerator;

import org.gradle.api.Project
import org.gradle.api.Plugin

class MvpExtension {
    def applicationId
    def packageName
    def functionName
    def author
    boolean isViewActivity
}