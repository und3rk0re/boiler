package k0re.boiler.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class SingleThreadReactor implements Reactor, Runnable
{
    BlockingQueue<Object> events = new LinkedBlockingQueue<>();
    LinkedList<Executor> executors = new LinkedList<>();

    @RequiredArgsConstructor
    class Executor<T>
    {
        final Class<T> clazz;
        final Consumer<T> listener;

        boolean can(Object e) {
            return clazz.isAssignableFrom(e.getClass());
        }

        @SuppressWarnings("unchecked")
        void apply(Object e) {
            listener.accept((T) e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void register(@NonNull Class<T> clazz, @NonNull Consumer<T> listener) {
        executors.add(new Executor(clazz, listener));
    }

    @Override
    public void send(@NonNull Object event) {
        events.add(event);
    }

    @Override
    public void run() {
        Object event;
        for (;;) {
            try {
                event = events.take();
            } catch (InterruptedException e) {
                break;
            }

            for (Executor executor : executors) {
                if (executor.can(event)) {
                    executor.apply(event);
                }
            }
        }
    }
}
