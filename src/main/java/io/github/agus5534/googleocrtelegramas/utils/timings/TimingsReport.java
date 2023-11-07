package io.github.agus5534.googleocrtelegramas.utils.timings;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TimingsReport {

    private static Long runtime = System.currentTimeMillis();
    private static Long lastReport = runtime;
    private static LinkedHashMap<String, Long> reports = new LinkedHashMap<>();

    public static void report(String subject) {
        var current = System.currentTimeMillis();
        var time = current-lastReport;
        reports.put(subject, time);

        lastReport = current;
    }

    public static void buildTimingsReport() {
        StringBuilder reportsString = new StringBuilder();

        AtomicInteger i = new AtomicInteger(0);
        reports.forEach((s, l) -> {
            reportsString.append("  » ").append(s).append(": ").append(l).append("ms").append(i.get()+1 == reports.size() ? "" : "\n");

            i.getAndIncrement();
        });

        GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault());
        cal.setTimeInMillis(runtime);

        String message = """
                -------------TIMINGS REPORT-------------
                Iniciado: %s
                Cantidad de Reportes: %s
                Tiempo Total de Ejecución: %sms
                Reportes:
                %s
                ----------------------------------------
                """.formatted(
                        new SimpleDateFormat("dd-MM-yyyy HH:mm:ss,SSS").format(cal.getTime()),
                              reports.size(),
                              System.currentTimeMillis()-runtime,
                              reportsString.toString()
        );

        System.out.println(message);
    }
}
