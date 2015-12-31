package com.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class ProcessBuilderRedirectIOExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("echo", "hello");
        System.out.println("Configure parameters");
        Map<String, String> env = pb.environment();
        env.put("name", "ping command");
        env.put("echoCount", "2");

        System.out.println("Redirect output and error to file");
        File outputFile = new File("/tmp/PingLog.txt");
        File errorFile = new File("/tmp/PingErrLog.txt");
        pb.redirectOutput(outputFile);
        pb.redirectError(errorFile);

        Process process = pb.start();
        process.waitFor();

        System.out.println("\nRerun again so that the mkdir command throws error");
        process = pb.start();
        process.waitFor();

        System.out.println("\nPrint Output:");
        printFile(outputFile);
        System.out.println("\nPrint Error:");
        printFile(errorFile);

        System.out.println("\nRedirect error and run again so that error is redirected to output file");
        pb.redirectErrorStream(true);
        File commonOutputFile = new File("src/resources/PingCommonLog.txt");
        pb.redirectOutput(commonOutputFile);
        process = pb.start();
        process.waitFor();

        System.out.println("\nPrint Common Output:");
        printFile(commonOutputFile);

        System.out.println("\nRedirect input source to a file");
        pb = new ProcessBuilder("cmd");
        pb.environment().putAll(env);
        pb.inheritIO();
        pb.redirectInput(new File("src/resources/ping.bat"));
        process = pb.start();
        process.waitFor();
    }

    private static void printFile(File file) throws IOException {
        System.out.println("*********************************");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
        fr.close();
        System.out.println("*********************************");
    }

}
