package com.idat.qa.listeners;

import com.idat.qa.config.ConfigReader;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

/**
 * Muestra en consola el avance de cada prueba con el navegador en el que se ejecuta.
 */
public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        log("========== Inicio: " + context.getName() + " ==========");
    }

    @Override
    public void onTestStart(ITestResult result) {
        log("[RUN ] " + describe(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log("[PASS] " + describe(result) + " (" + duration(result) + ")");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log("[FAIL] " + describe(result) + " (" + duration(result) + ")");
        if (result.getThrowable() != null) {
            log("       Motivo: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log("[SKIP] " + describe(result));
    }

    @Override
    public void onFinish(ITestContext context) {
        log("========== Fin: " + context.getName() + " | OK: " + context.getPassedTests().size()
                + " | Fallidas: " + context.getFailedTests().size()
                + " | Omitidas: " + context.getSkippedTests().size() + " ==========");
    }

    private static String describe(ITestResult result) {
        String browser = browserOf(result);
        String description = result.getMethod().getDescription();
        String name = result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
        String params = result.getParameters().length > 0 ? " " + Arrays.toString(result.getParameters()) : "";
        return "[" + browser + "] " + name + params + (description != null ? " - " + description : "");
    }

    /** Mismo criterio que BaseTest: parámetro del suite y, si no existe, -Dbrowser o config.properties. */
    private static String browserOf(ITestResult result) {
        String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        return (browser != null ? browser : ConfigReader.get("browser")).toUpperCase();
    }

    private static String duration(ITestResult result) {
        return (result.getEndMillis() - result.getStartMillis()) / 1000.0 + " s";
    }

    private static void log(String message) {
        System.out.println(message);
    }
}
