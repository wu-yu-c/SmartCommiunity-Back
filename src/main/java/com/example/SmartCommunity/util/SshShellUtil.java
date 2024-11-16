package com.example.SmartCommunity.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Objects;

public class SshShellUtil {

    private static void executeCommandInternal(Connection conn, String command) throws Exception {
        Session session = null;
        try {
            session = conn.openSession();
            session.execCommand(command);
            try (InputStream is = new StreamGobbler(session.getStdout());
                 BufferedReader brs = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = brs.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } finally {
            if (Objects.nonNull(session)) {
                session.close();
            }
        }
    }

    public static void executeCommand(String ip, int port, String username, String password, String command) {
        Connection conn = new Connection(ip, port);
        try {
            conn.connect();
            if (conn.authenticateWithPassword(username, password)) {
                executeCommandInternal(conn, command);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            conn.close();
        }
    }

    public static void executeCommand(String ip, int port, String username, Path keyFilePath, String command) {
        Connection conn = new Connection(ip, port);
        try {
            conn.connect();
            File keyFile = keyFilePath.toFile();
            if (conn.authenticateWithPublicKey(username, keyFile, null)) {
                executeCommandInternal(conn, command);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            conn.close();
        }
    }

    public static void main(String[] args) {
        // Example usage
        // executeCommand("100.64.163.72", 5122, "nami_default", "/path/to/nami_default", "your_command_here");
    }
}