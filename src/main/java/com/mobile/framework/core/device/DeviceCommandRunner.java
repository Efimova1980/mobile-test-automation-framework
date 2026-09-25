package com.mobile.framework.core.device;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

final class DeviceCommandRunner {

    private DeviceCommandRunner() {
    }

    /**
     * stderr goes to a temp file, not a pipe: reading stdout and stderr pipes one after
     * another can hang when the unread pipe's buffer fills up.
     */
    static String runCommand(List<String> command) throws IOException, InterruptedException {
        Path errorFile = Files.createTempFile("device-command", ".err");
        try {
            Process process = new ProcessBuilder(command)
                    .redirectError(errorFile.toFile())
                    .start();

            String output = new String(process.getInputStream().readAllBytes());
            checkExitCode(command, process.waitFor(), errorFile);
            return output;
        } finally {
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
