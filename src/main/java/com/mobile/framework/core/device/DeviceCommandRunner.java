package com.mobile.framework.core.device;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

final class DeviceCommandRunner {

    private static final long TIMEOUT_SECONDS = 60;

    private DeviceCommandRunner() {
    }

    /**
     * stdout and stderr go to temp files, not pipes: reading from a pipe blocks while
     * a hung command is running, so the timeout could never fire.
     */
    static String runCommand(List<String> command) throws IOException, InterruptedException {
        Path outputFile = Files.createTempFile("device-command", ".out");
        Path errorFile = Files.createTempFile("device-command", ".err");
        try {
            Process process = new ProcessBuilder(command)
                    .redirectOutput(outputFile.toFile())
                    .redirectError(errorFile.toFile())
                    .start();

            if (!process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                throw new RuntimeException(
                        "Command \"" + String.join(" ", command)
                                + "\" did not finish in " + TIMEOUT_SECONDS + " seconds"
                );
            }

            checkExitCode(command, process.exitValue(), errorFile);
            return Files.readString(outputFile);
        } finally {
            Files.deleteIfExists(outputFile);
            Files.deleteIfExists(errorFile);
        }
    }

    private static void checkExitCode(List<String> command, int exitCode, Path errorFile) throws IOException {
        if (exitCode != 0) {
            String error = Files.readString(errorFile).trim();
            throw new RuntimeException(
                    "Command \"" + String.join(" ", command)
                            + "\" failed with exit code " + exitCode
                            + (error.isEmpty() ? "" : System.lineSeparator() + error)
            );
        }
    }
}
