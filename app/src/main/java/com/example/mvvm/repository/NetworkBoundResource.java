package com.example.mvvm.repository;


import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.mvvm.AppExecutors;
import com.example.mvvm.api.ApiResponse;

import java.util.Objects;

/*
Nos creamos esta clase generica que nos proporciona un recurso que puede venir tanto
de la base de datos local Room como del WebService.
 */
public abstract class NetworkBoundResource<ResultType, RequestType> {

    private final AppExecutors appExecutors;


    /*
    MediatorLiveData nos permite mergear varias objetos liveData y cada que que uno de estos
    obejtos live data cambia lo notifica el MediatorliveData, es decir mergea todos los livedata
    que tenemos en uno solo en el mediator.
     */
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();


    /*
    @MainThread
    Indica que el metodo anotado solo debe llamarse desde el hilo principal.
     */
    @MainThread
    NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        //Marcamos que vamos a empezar a hacer la petición y por tanto hacemos un loading.
        result.setValue(Resource.loading(null));
        //Hacemos la llamada a la base de datos, para ver si existen datos.
        LiveData<ResultType> dbSource = loadFromDb();
        //Añadimos al mediatorlivedata, para que empiece a observar si hay cambion en nuestra
        //petición a la base de datos.
        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(ResultType data) {
                //Le decimos que pare de observar esa fuente de datos al metiator livedata.
                result.removeSource(dbSource);
                //Vemos si debemos solicitar datos a través del web service, ya sea por que no
                //hay datos en la base de datos, o porque estos esten caducados.
                if (NetworkBoundResource.this.shouldFetch(data)) {
                    //Si hay que cargar datos del servicio web.
                    NetworkBoundResource.this.fetchFromNetwork(dbSource);
                } else {
                    //añadimos al meditaor livedata los datos
                    result.addSource(dbSource, (ResultType newData) -> {
                        NetworkBoundResource.this.setValue(Resource.success(newData));
                    });
                }
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (!Objects.equals(result.getValue(), newValue)) {
            result.setValue(newValue);
        }
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        //Creamos la llamada a la base de datos.
        LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(ResultType newData) {
                NetworkBoundResource.this.setValue(Resource.loading(newData));
            }
        });
        //Y hamos lo mismo para wl webservice.
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            //noinspection ConstantConditions
            //Si la respuesta es correcta
            if (response.isSuccessful()) {
                //Salvamos en Room la información a través del executor diskIO.
                appExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        //procesamos la respuesta que hemos recibido
                        NetworkBoundResource.this.saveCallResult(NetworkBoundResource.this.processResponse(response));
                        appExecutors.mainThread().execute(() ->
                                // we specially request a new live data,
                                // otherwise we will get immediately last cached value,
                                // which may not be updated with latest results received from network.
                                //Vovemos a añadir la base de datos, para que nos coja esta vez
                                //los datos que acabmos de salbar en la base de datos.
                                {
                                    result.addSource(NetworkBoundResource.this.loadFromDb(),
                                            newData -> NetworkBoundResource.this.setValue(Resource.success(newData)));
                                }
                        );
                    }
                });
            } else {
                //Si ha fallado la petición. En este caso nuestro Resource será de Error.
                onFetchFailed();
                result.addSource(dbSource,
                        newData -> setValue(Resource.error(response.errorMessage, newData)));
            }
        });
    }

    protected void onFetchFailed() {
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    @WorkerThread
    protected RequestType processResponse(ApiResponse<RequestType> response) {
        return response.body;
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();
}
