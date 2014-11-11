package com.bardframework.plugin;


import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

@Mojo(name = "war", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class BardMojo extends AbstractMojo {

    File webAppFile;
    File webInfFile;
    File xml;
    @Component
    private MavenProject mavenProject;
    @Component
    private MavenSession mavenSession;
    @Component
    private BuildPluginManager pluginManager;
    @Parameter(property = "servletClass", required = true)
    private String servletClass;
    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}")
    private String outputDirectory;
    private boolean cleanWebApp = false;
    private boolean cleanWEBINF = false;
    private boolean cleanWebXml = false;

    public void execute() throws MojoExecutionException {
        writeWebXml();

        executeMojo(
            plugin(
                groupId("org.apache.maven.plugins"),
                artifactId("maven-war-plugin"),
                version("2.5")
            ),
            goal("war"),
            configuration(
                element(name("outputDirectory"), outputDirectory)
            ),
            executionEnvironment(
                mavenProject,
                mavenSession,
                pluginManager
            )
        );
        cleanWebXml();
    }

    private void writeWebXml() throws MojoExecutionException {
        // mkdir webapp
        webAppFile = new File("src/main/webapp");
        if (!webAppFile.exists()) {
            webAppFile.mkdirs();
            cleanWebApp = true;
        }
        // mkdir WEB-INF
        webInfFile = new File(webAppFile, "WEB-INF");
        if (!webInfFile.exists()) {
            webInfFile.mkdirs();
            cleanWEBINF = true;
        }
        xml = new File(webInfFile, "web.xml");
        if (!xml.exists()) {
            try {
                FileWriter w = new FileWriter(xml);
                w.write(
                    "<web-app xmlns=\"http://java.sun.com/xml/ns/javaee\" version=\"2.5\">\n"
                        + "    <servlet>\n"
                        + "        <servlet-name>bard</servlet-name>\n"
                        + "        <servlet-class>" + servletClass + "</servlet-class>\n"
                        + "    </servlet>\n"
                        + "    <servlet-mapping>\n"
                        + "        <servlet-name>bard</servlet-name>\n"
                        + "        <url-pattern>/*</url-pattern>\n"
                        + "    </servlet-mapping>\n"
                        + "</web-app>\n");
                w.close();
            } catch (IOException e) {
                throw new MojoExecutionException("Error while write web.xml.", e);
            }
            cleanWebXml = true;
        }
    }

    private void cleanWebXml() {
        if (cleanWebXml) {
            xml.delete();
        }
        if (cleanWEBINF) {
            webInfFile.delete();
        }
        if (cleanWebApp) {
            webAppFile.delete();
        }
    }
}
