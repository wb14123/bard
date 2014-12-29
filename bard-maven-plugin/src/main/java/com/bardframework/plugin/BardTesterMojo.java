package com.bardframework.plugin;

import com.bardframework.bard.basic.filter.APIDocFilter;
import com.bardframework.bard.core.HandlerMeta;
import com.bardframework.bard.core.Servlet;
import com.bardframework.bard.core.doc.Api;
import com.bardframework.bard.core.doc.Document;
import org.apache.maven.plugin.AbstractMojo;
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
import java.io.FileWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

@Mojo(name = "generate-tester", defaultPhase = LifecyclePhase.GENERATE_TEST_SOURCES,
    requiresDependencyResolution = ResolutionScope.COMPILE, requiresProject = true)
public class BardTesterMojo extends AbstractMojo {

    @Component
    private MavenProject mavenProject;
    @Parameter(property = "servletClass", required = true)
    private String servletClass;

    @Override public void execute() throws MojoExecutionException, MojoFailureException {
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class",
            ClasspathResourceLoader.class.getName());
        Velocity.init();


        try {
            List runtimeClasspathElements = mavenProject.getRuntimeClasspathElements();
            URL[] runtimeUrls = new URL[runtimeClasspathElements.size()];
            for (int i = 0; i < runtimeClasspathElements.size(); i++) {
                String element = (String) runtimeClasspathElements.get(i);
                runtimeUrls[i] = new File(element).toURI().toURL();
            }
            URLClassLoader newLoader = new URLClassLoader(runtimeUrls,
                Thread.currentThread().getContextClassLoader());
            Thread.currentThread().setContextClassLoader(newLoader);

            Class<? extends Servlet> c =
                (Class<? extends Servlet>) newLoader.loadClass(servletClass);
            Servlet servlet = c.newInstance();
            HandlerMeta.annotationMapper = servlet.mapper;
            Document document = APIDocFilter.getDocument(c, "");
            Map<Class<?>, Set<Api>> classDocumentMap = new HashMap<>();
            for (Api api : document.apis) {
                Class<?> handlerClass = api.getHandlerMethod().getDeclaringClass();
                Set<Api> apis = classDocumentMap.get(handlerClass);
                if (apis == null) {
                    apis = new HashSet<>();
                }
                apis.add(api);
                classDocumentMap.put(handlerClass, apis);
            }
            for (Map.Entry<Class<?>, Set<Api>> entry : classDocumentMap.entrySet()) {
                VelocityContext context = new VelocityContext();
                String allName = entry.getKey().getName();
                String[] classNames = allName.split("\\.");
                String className = classNames[classNames.length - 1];
                String packageName = allName.replace("." + className, "");
                File packageDir = new File(
                    "target/generated-test-sources/java/" + packageName.replace(".", "/"));
                if (!packageDir.exists() && !packageDir.mkdirs()) {
                    throw new MojoExecutionException("Cannot mkdirs for generated-test-sources");
                }
                File fileName = new File(packageDir, className + "Tester.java");
                FileWriter fileWriter = new FileWriter(fileName);
                context.put("package", packageName);
                context.put("instance", this);
                context.put("servletClass", servletClass);
                context.put("handlerClass", className);
                context.put("apis", entry.getValue());
                Template template = Velocity.getTemplate("GenerateTester.java.vm");
                template.merge(context, fileWriter);
                fileWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String covertNameToParam(String name) {
        char[] chars = name.toCharArray();
        String result = "";
        int l = chars.length;
        for (int i = 0; i < l; i++) {
            if (!(
                (chars[i] >= 'A' && chars[i] <= 'Z') ||
                    (chars[i] >= 'a' && chars[i] <= 'z') ||
                    (chars[i] >= '0' && chars[i] <= '9'))
                ) {
                if (i + 1 < l) {
                    chars[i + 1] = Character.toUpperCase(chars[i + 1]);
                }
            } else {
                result += chars[i];
            }
        }
        return result;
    }
}
