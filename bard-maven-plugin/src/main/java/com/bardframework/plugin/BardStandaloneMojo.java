package com.bardframework.plugin;

import org.apache.commons.io.FileUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.jar.JarFile;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

@Mojo(name = "standalone", defaultPhase = LifecyclePhase.PACKAGE,
    requiresDependencyResolution = ResolutionScope.RUNTIME)
public class BardStandaloneMojo extends AbstractMojo {

    @Component
    private MavenProject mavenProject;
    @Component
    private MavenSession mavenSession;
    @Component
    private BuildPluginManager pluginManager;
    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}")
    private String outputDirectory;

    @Override public void execute() throws MojoExecutionException, MojoFailureException {
        String toFileName =
            outputDirectory + "/" + mavenProject.getName() + "-" + mavenProject.getVersion();
        File toFile = new File(toFileName);
        File resourceDir = new File("src/main/resources");
        File confDir = new File(toFile, "conf");
        File configScript = new File(toFile, "bin/config.sh");
        File binDir = new File(toFile, "bin");
        try {
            // copy resource files to conf
            FileUtils.copyDirectory(resourceDir, confDir);
            // copy server.sh
            InputStream serverInput = getClass().getClassLoader().getResourceAsStream("server.sh");
            File serverScript = new File(binDir, "server.sh");
            FileUtils.copyInputStreamToFile(serverInput, serverScript);
            serverInput.close();
            serverScript.setExecutable(true);
            // copy dependencies to lib
            executeMojo(
                plugin(
                    groupId("org.apache.maven.plugins"),
                    artifactId("maven-dependency-plugin"),
                    version("2.9")
                ),
                goal("copy-dependencies"),
                configuration(
                    element(name("outputDirectory"), toFileName + "/lib")
                ),
                executionEnvironment(
                    mavenProject,
                    mavenSession,
                    pluginManager
                )
            );
            // copy jar file
            File jarFile = new File(
                "target/" + mavenProject.getName() + "-" + mavenProject.getVersion() + ".jar");
            FileUtils.copyFileToDirectory(jarFile, toFile);
            // write config.sh
            JarFile j = new JarFile(jarFile);
            if (!configScript.exists() && !configScript.createNewFile()) {
                throw new MojoExecutionException("Fail to create bin/config.sh");
            }
            PrintWriter writer = new PrintWriter(configScript);
            writer.println("#/bin/sh");
            writer.println();
            writer.println("PROJECT=" + mavenProject.getName());
            writer.println("VERSION=" + mavenProject.getVersion());
            writer.println(
                "MAIN_CLASS=" + j.getManifest().getMainAttributes().getValue("Main-Class"));
            writer.close();
        } catch (IOException e) {
            throw new MojoExecutionException("Fail to copy files", e);
        }
    }
}
