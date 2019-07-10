package com.example.mvvm.api;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Response;
/*
Clase comun utilizada por las respuestas que vamos a obtner de las API.
 */
public class ApiResponse<T> {

    /*
    Declaramos una expresión regular, tambien conocida como regex. Es una secuencia de caracteres
    que forma un patrón de busqueda, principalmente se usa par ala busqueda de patrones de cadenasç
    de caracteres, en nuestro caso para que el link que recibimos cumple este patrón y por lo tanto
    que vamos a poder buscarlos.
    Básicamente este funcion Regex es valida para una url formada correctamente.
     */
    private static final Pattern LINK_PATTERN = Pattern
            .compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"");
    private static final Pattern PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)");
    private static final String NEXT_LINK = "next";
    public final int code;
    @Nullable
    public final T body;
    @Nullable
    public final String errorMessage;
    @NonNull
    public final Map<String, String> links;

    /*
    Si al recibir la respuesta del WebService nos llega con error se ejecutará este constructor.
     */
    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        errorMessage = error.getMessage();
        links = Collections.emptyMap();
    }

    /*
    Si al recibir la respuesta del WebService nos llega corractamente.
     */
    public ApiResponse(Response<T> response) {
        code = response.code();
        if(response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException ignored) {
                    Log.d(ignored.getMessage(), "error while parsing response");
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }
        String linkHeader = response.headers().get("link");
        if (linkHeader == null) {
            links = Collections.emptyMap();
        } else {
            links = new ArrayMap<>();
            // Dejamos en Matcher los link que encuentre dentro de linkHeader y que coincida
            //con la expresión regular. En este caso nos deja 2.
            Matcher matcher = LINK_PATTERN.matcher(linkHeader);
            /*
            El método find de Mather intenta encontrar la siguiente subsecuencia
            que cumple el patrón. Devuelve un boolenao si lo ha encontrado.
             */
            while (matcher.find()) {
                //Devuelve el número grupos que cumplen el Patrón.
                //Como nos debe devolver dos, pues dejamos en link el valor 2 y 1
                int count = matcher.groupCount();
                if (count == 2) {
                    links.put(matcher.group(2), matcher.group(1));
                }
            }
        }
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    public Integer getNextPage() {
        String next = links.get(NEXT_LINK);
        if (next == null) {
            return null;
        }
        Matcher matcher = PAGE_PATTERN.matcher(next);
        /*
            El método find de Mather intenta encontrar la siguiente subsecuencia
            que cumple el patrón. Devuelve un boolenao si lo ha encontrado.
            En este caso groupCount nos debe devolver 1 puesto que solo hay uno que machea con Next
             */
        if (!matcher.find() || matcher.groupCount() != 1) {
            return null;
        }
        try {
            return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException ex) {
            Log.d("cannot parse next", next);
            return null;
        }
    }
}
