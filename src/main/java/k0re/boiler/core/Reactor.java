package k0re.boiler.core;

import java.util.function.Consumer;

public interface Reactor
{
    <T> void register(Class<T> clazz, Consumer<T> listener);
    void send(Object event);
}
