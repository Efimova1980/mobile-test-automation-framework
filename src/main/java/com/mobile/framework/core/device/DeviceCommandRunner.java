package com.mobile.framework.core.device;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

final class DeviceCommandRunner {

    private DeviceCommandRunner() {
    }

    static String runCommand(List<String> command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command)
                .start();

        String output = new String(process.getInputStream().readAllBytes());
        String error = new String(process.getErrorStream().readAllBytes());

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException(
                    "Command \"" + String.join(" ", command)
                            + "\" failed with exit code " + exitCode
                            + (error.isBlank() ? "" : System.lineSeparator() + error)
            );
        }

        return output;
    }

    static void runCommand(List<String> command, Path outputFile) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command)
                .redirectOutput(outputFile.toFile())
                .start();

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            String error = new String(process.getErrorStream().readAllBytes()).trim();

            throw new RuntimeException(
                    "Command \"" + String.join(" ", command)
                            + "\" failed with exit code " + exitCode
                            + (error.isEmpty() ? "" : System.lineSeparator() + error)
            );
        }
    }
}
