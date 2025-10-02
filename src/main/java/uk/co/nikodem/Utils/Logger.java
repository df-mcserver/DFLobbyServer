package uk.co.nikodem.Utils;

public class Logger {
    public void log(String domain, String msg) {
        System.out.println("[LOG] "+domain+" // "+msg);
    }

    public void warn(String domain, String msg) {
        System.out.println("[WRN] "+domain+" // "+msg);
    }

    public void error(String domain, String msg) {
        System.out.println("[ERR] "+domain+" // "+msg);
    }
}
