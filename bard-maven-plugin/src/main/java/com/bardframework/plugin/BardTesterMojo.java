package com.bardframework.plugin;

import com.bardframework.bard.basic.filter.APIDocFilter;
import com.bardframework.bard.core.HandlerFactory;
import com.bardframework.bard.core.HandlerMeta;
import com.bardframework.bard.core.Servlet;
import com.bardframework.bard.core.doc.Document;
import com.bardframework.bard.util.server.BardServer;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mojo(name = "generate" , defaultPhase = LifecyclePhase.PACKAGE,
    requiresDependencyResolution = ResolutionScope.RUNTIME)
public class BardTesterMojo extends AbstractMojo {

    @Component
    private MavenProject mavenProject;
    @Component
    private MavenSession mavenSession;
    @Component
    private BuildPluginManager pluginManager;
    @Parameter(property = "servletClass" , required = true)
    private String servletClass;
    @Parameter(property = "outputDirectory" , defaultValue = "${project.build.directory}" )
    private String outputDirectory;

    @Override public void execute() throws MojoExecutionException, MojoFailureException {
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath" );
        Velocity.setProperty("classpath.resource.loader.class" ,
            ClasspathResourceLoader.class.getName());
        Velocity.init();

        VelocityContext context = new VelocityContext();

        try {
            /*
            List runtimeClasspathElements = mavenProject.getRuntimeClasspathElements();
            URL[] runtimeUrls = new URL[runtimeClasspathElements.size()];
            for (int i = 0; i < runtimeClasspathElements.size(); i++) {
                String element = (String) runtimeClasspathElements.get(i);
                runtimeUrls[i] = new File(element).toURI().toURL();
            }
            URLClassLoader newLoader = new URLClassLoader(runtimeUrls,
                Thread.currentThread().getContextClassLoader());
            Class<? extends Servlet> c =
                (Class<? extends Servlet>) newLoader.loadClass(servletClass);
                */
            Set<URL> urls = new HashSet<>();
            List<String> elements = mavenProject.getTestClasspathElements();
            for (String element : elements) {
                urls.add(new File(element).toURI().toURL());
            }

            ClassLoader contextClassLoader = new URLClassLoader(
                urls.toArray(new URL[0]),
                Thread.currentThread().getContextClassLoader());
            Thread.currentThread().setContextClassLoader(contextClassLoader);

            Class<? extends Servlet> c =
                (Class<? extends Servlet>) Class.forName(servletClass);
            Servlet servlet = c.newInstance();
            HandlerMeta.annotationMapper = servlet.mapper;
            Document document = APIDocFilter.getDocument(c, "" );
            context.put("doc" , document);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Template template = Velocity.getTemplate("GenerateTester.java.vm" );
        StringWriter sw = new StringWriter();
        template.merge(context, sw);
        System.out.print(sw.toString());
    }
}
