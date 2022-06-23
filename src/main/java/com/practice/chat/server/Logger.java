package com.practice.chat.server;

import java.io.PrintStream;

public class Logger {
    private PrintStream printStream;
    private static final Logger logger = new Logger();

    private Logger() {
        this(System.out);
    }

    private Logger(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void setPrintStream(PrintStream printStream) {
        this.printStream = printStream;
    }

    public static Logger getInstance() {
        return logger;
    }

    public void logAccept(String hostAddress) {
        printStream.println("accept: " + hostAddress);
    }

    public void logUserLogin() {

    }
}
