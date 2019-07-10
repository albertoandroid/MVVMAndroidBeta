package com.example.mvvm.util;

import android.os.SystemClock;
import android.util.ArrayMap;

import java.util.concurrent.TimeUnit;
/*
Utility class que nos va a permitir decidir si debemos o no debemos solicitar datos del
webService.
 */
public class RateLimiter<KEY> {
    //En timestamp guardamos la Key cuando se ha realizado la ultima petición que se ha guardado.
    private ArrayMap<KEY, Long> timestamps = new ArrayMap<>();
    private final long timeout;

    /*
    En el constructor le indicamos el timeout, es decir el tiempo minimo, que una vez pasado
    dicho tiempo tendremos que solicitar de nuevo datos al servidor, puede ser 1 minuto
    1 día una semana. Según pensemso que nuestra aplicación podría cambiar, el objetivo de todo
    esto es no lanzar una petición siempre al servidiro debido a que consume recursos y es posible
    que no haya datos actualizados.
     */
    public RateLimiter(int timeout, TimeUnit timeUnit) {
        this.timeout = timeUnit.toMillis(timeout);
    }

    public synchronized boolean shouldFetch(KEY key) {
        //ultima vez aque se solicito datos al servidor para esa Key.
        Long lastFetched = timestamps.get(key);
        //tiempo actual
        long now = now();
        //Si es null hay que solicitar y dejamos el tiempo a ahora y devolvemos true,
        //Es decir que tenemos que hacer petición.
        if (lastFetched == null) {
            timestamps.put(key, now);
            return true;
        }
        //Si el tiempo actual menos el tiempo en elq ue hicimos la petición es mayor al
        //time out que indicamos tendremos que volver a hacer petición.
        if (now - lastFetched > timeout) {
            timestamps.put(key, now);
            return true;
        }
        //otro caso no hay que hacer petición al servidor y podemos coger los datos de la
        //base de datos local con lo que nos ahorramos la petición,.
        return false;
    }

    private long now() {
        return SystemClock.uptimeMillis();
    }

    public synchronized void reset(KEY key) {
        timestamps.remove(key);
    }
}
