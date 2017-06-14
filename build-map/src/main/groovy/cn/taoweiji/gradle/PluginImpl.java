package cn.taoweiji.gradle;

import org.gradle.api.Plugin
import org.gradle.api.Project

public class PluginImpl implements Plugin<Project> {
    void apply(Project project) {
       project.task('testTask') << {
            println "Hello gradle plugin"
        }
    }
}