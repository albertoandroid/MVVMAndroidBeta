package com.example.mvvm;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;
/*
La forma de gestionar los hilos por parte de Android me gusta mucho. Lo que hacemos es crear
agrupadores globales de ejecutores para toda nuestra App.

La razon de agrupar tareas como esta evita los efectos de la innación de tareas, basicamente es que
por ejemplo las lecturas de disco no esperan a las solicitudes de servicio web.

Este código utilizado por google es muy bueno.

Declaramos un singlenton, es decir una sola instancia de esta clase.
 */
@Singleton
public class AppExecutors {

    /*
    La clase Executor, ejecuta el comando dado en algún momento en el futuro.
    El comando puede ejecutarse en un nuevo hilo, en un hilo agrupado o en la llamada
     */

    private final Executor diskIO;

    private final Executor networkIO;

    private final Executor mainThread;

    public AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    @Inject
    public AppExecutors() {
        /*
        - para el diskIo nos creamos un Executors.newSingleThreadExecutor que nos va a permitir que
        una vez gtengamos la instancia dle Executor, podemos enviarle multiples tareas y ejecutras una tras
        otra. Algo que no podriamos hacer con un simple hilo.
        - para el networkIo Executors.newFixedThreadPool crea un grupo de hilos que se reutilizan.
        en este caso le estamos indicando que van a tener un número de hilo de 3. Es decir
        que como mucho vamos a poder tener 3 llamadas al servidor activas en un mismo momento.
        Si enviamos una 4 llamda al servicio web, esta quedará en cola hasta que haya un hilo
        disponible.
        - Por ultimo el Executor para el hilo principal.
         */
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
                new MainThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    /*
    A Executor que ejecuta sus tareas en el hilo principal.
     */
    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
