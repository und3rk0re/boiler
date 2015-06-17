package kore.b0iler.server;

import k0re.boiler.core.SingleThreadReactor;

public class ApplicationServer
{
    public static void main(String[] args)
    {
        // Building main reactor
        SingleThreadReactor applicationReactor = new SingleThreadReactor();

        // Debug info
        applicationReactor.register(String.class, System.out::println);

        // Staring
        new Thread(applicationReactor).start();

        // Debug
        applicationReactor.send("Starting application");
        applicationReactor.send("OK");
    }
}
