package in.tryjava8.api.service;

import java.security.Permission;

public class MySecurityManager extends SecurityManager {


    @Override
    public void checkDelete(String file) {
        throw new SecurityException("You are not allowed to delete a file");
    }

    @Override
    public void checkPermission(Permission perm) {
    }

    @Override
    public void checkPermission(Permission perm, Object context) {

    }

    @Override
    public void checkExit(int status) {
        throw new SecurityException("You are not allowed to use System.exit(-1)");
    }

    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("You can't execute commands using Runtime.getRuntime().exec()");
    }

    @Override
    public void checkPackageAccess(String pkg) {
        if (pkg.startsWith("javax.management") || pkg.startsWith("javax.ejb") || pkg.startsWith("javax.enterprise")) {
            throw new SecurityException("You can't use javax.* package classes");
        }

        if (pkg.startsWith("java.awt")) {
            throw new SecurityException("You can't use java.awt.* package classes");
        }

        if (pkg.startsWith("java.applet")) {
            throw new SecurityException("You can't use java.applet.* package classes");
        }

        if (pkg.startsWith("java.lang.management")) {
            throw new SecurityException("You can't use java.lang.management.* package classes");
        }

        if (pkg.startsWith("java.rmi")) {
            throw new SecurityException("You can't use java.rmi.* package classes");
        }

    }

    @Override
    public void checkPropertiesAccess() {
        throw new SecurityException("You can't access system properties");
    }

}
