package net.avenwu.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project;

/**
* 定义Plugin
*/
public class HelloPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.extensions.create("author", Author)
        project.task('echo') << {
            println 'Author information:'
            println project.author.name
            println project.author.email

        }
    }
}
