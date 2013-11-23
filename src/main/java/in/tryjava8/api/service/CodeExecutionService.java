package in.tryjava8.api.service;

import in.tryjava8.api.domain.CompilerOutput;
import in.tryjava8.api.domain.ProgramOutput;
import in.tryjava8.api.domain.Snippet;
import in.tryjava8.api.domain.Verdict;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class CodeExecutionService {

    private static final String LOCAL_SOURCE_LOCATION = "/Users/shekhargulati/dev/tmp";

    private static final String OPENSHIFT_SOURCE_LOCATION = System.getenv("OPENSHIFT_DATA_DIR");

    @Autowired
    private Environment environment;

    public CompilerOutput compile(Snippet snippet) {
        File root = new File(getSourceLocation(snippet));

        File sourceFile = new File(root, getJavaClassName(snippet));
        sourceFile.getParentFile().mkdirs();
        try {
            FileWriter fileWriter = new FileWriter(sourceFile);
            fileWriter.append(snippet.getCode()).close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to write Java source file.", e);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int result = compiler.run(null, ps, ps, sourceFile.getPath());

        String compilerOutput = baos.toString();

        return new CompilerOutput(result, compilerOutput);

    }

    public ProgramOutput run(String fileName, Snippet snippet) {
        System.setSecurityManager(new MySecurityManager());
        URLClassLoader classLoader = null;
        try {
            classLoader = URLClassLoader
                    .newInstance(new URL[] { new File(getSourceLocation(snippet)).toURI().toURL() });
        } catch (MalformedURLException malformedURLException) {
            return new ProgramOutput("Unable to load the class file" + malformedURLException.getMessage(),
                    Verdict.FAILURE);
        }

        String javaCompiledClass = fileName.split("\\.")[0];
        Class<?> cls = null;
        try {
            cls = Class.forName(javaCompiledClass, true, classLoader);
        } catch (ClassNotFoundException e2) {
            return new ProgramOutput("ClassNotFoundException " + e2.getMessage(), Verdict.FAILURE);
        }

        Method meth = null;
        try {
            meth = cls.getMethod("main", String[].class);
        } catch (Exception e) {
            return new ProgramOutput("Main method not found " + e.getMessage(), Verdict.FAILURE);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);
        String[] params = null;
        try {
            meth.invoke(null, (Object) params);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            System.out.flush();
            System.setOut(old);
            System.setSecurityManager(null);
            return new ProgramOutput(cause.getMessage() + "\n" + baos.toString(), Verdict.FAILURE);
        }
        System.out.flush();
        System.setOut(old);
        System.setSecurityManager(null);
        return new ProgramOutput(baos.toString(), Verdict.SUCCESS);
    }

    public String getJavaClassName(Snippet snippet) {
        String code = snippet.getCode();

        String classStr = "public class";
        int index = code.indexOf(classStr);

        if (index == -1) {
            return "Main.java";
        }

        int indexFirstCurlyBrace = code.indexOf("{");

        String className = code.substring(index + classStr.length(), indexFirstCurlyBrace);
        className = className.trim();

        return className + ".java";
    }

    private String getSourceLocation(Snippet snippet) {
        if (ArrayUtils.contains(environment.getActiveProfiles(), "openshift")) {
            return OPENSHIFT_SOURCE_LOCATION + "/" + snippet.getId();
        }
        return LOCAL_SOURCE_LOCATION + "/" + snippet.getId();
    }
}
