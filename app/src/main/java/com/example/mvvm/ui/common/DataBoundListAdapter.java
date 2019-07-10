package com.example.mvvm.ui.common;

/*
Un Adaptador de Recycler view generico que usa dataBinding y DiffUtill

 */

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
/*
Un adapter para RecyclerView generico que usa Data binding y DiffUtil

T-> typo de items en la lista
V el tipo de ViewDataBinding

DiffUtil es una clase de utilidad que puede calcular la diferencia entre dos listas
y generar una lista de operaciones de actualización que convierte la primera
lista en la segunda.
Se puede usar para calcular actualizaciones para un Adaptador RecyclerView.
 */

public abstract class DataBoundListAdapter<T, V extends ViewDataBinding>
        extends RecyclerView.Adapter<DataBoundViewHolder<V>> {

    @Nullable
    private List<T> items;
    // each time data is set, we update this variable so that if DiffUtil calculation returns
    //    // after repetitive updates, we can ignore the old calculation
    /*

    Cada vez que se actualizan los datos del recyclerview,
    actualizamos esta variable de modo que si el cálculo de DiffUtil
    que nos devuelve es distinto al de data versión,
    podemos ignorar el cálculo anterior.
    Esto es debido a que vamos a utilizar un hilo de background y puede
    que el hilo de interfaz de usuario y de background tengan versiones
    distintas.
     */
    private int dataVersion = 0;

    @Override
    public final DataBoundViewHolder<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        V binding = createBinding(parent);
        return new DataBoundViewHolder<>(binding);
    }

    protected abstract V createBinding(ViewGroup parent);

    @Override
    public final void onBindViewHolder(DataBoundViewHolder<V> holder, int position) {
        //noinspection ConstantConditions
        bind(holder.binding, items.get(position));
        /*
        Una nota importante a la hora de hacer databinding, es que cuando realizamos
        un cambio en nuestra binding no significa que se vaya a hacer de manera
        inmediata el cambio en la vista, sino que significa que se va a programar
        dicho camibio para un futuro cercano.

        Por ello tenemos el metodo executependingBinding que lo que hace es
        forzar al framezo para hacer todo lo necesario para actualizar el bingind
        al momento y no esperar.

         */
        holder.binding.executePendingBindings();
    }

    @SuppressLint("StaticFieldLeak")
    @MainThread
    public void replace(List<T> update) {
        dataVersion ++;
        if (items == null) {
            if (update == null) {
                return;
            }
            items = update;
            notifyDataSetChanged();
        } else if (update == null) {
            int oldSize = items.size();
            items = null;
            /*
            Notifica a los observadores que los elementos itemCount que se
            encontraban antes en positionStart han sido eleminado.
            Es decir borramos los datos.
             */
            notifyItemRangeRemoved(0, oldSize);
        } else {
            final int startVersion = dataVersion;
            final List<T> oldItems = items;
            new AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                @Override
                protected DiffUtil.DiffResult  doInBackground(Void... voids) {
                    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        /*
                        Devuelve el tamaño de la lista anterior.
                         */
                        public int getOldListSize() {
                            return oldItems.size();
                        }

                        /*
                        Devuelve el tamaño de la nueva lista
                         */
                        @Override
                        public int getNewListSize() {
                            return update.size();
                        }

                        /*
                        Llamado por DiffUtil para decidir si dos objetos representan el mismo objeto.
                        Si dos objetos tienen  el mismo id, este metodo debe chequear que son
                        iguales.
                         */
                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = update.get(newItemPosition);
                            return DataBoundListAdapter.this.areItemsTheSame(oldItem, newItem);
                        }

                        /*
                        Chequea si dos items tiene los mismos datos.
                        Tu puedes cambiar su comportamiento dependiente de tu UI.
                        Este metodo es llamado by DiffUtil solo si areItemTheSame devuelve
                        verdadero.
                         */

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = update.get(newItemPosition);
                            return DataBoundListAdapter.this.areContentsTheSame(oldItem, newItem);
                        }
                    });
                }

                @Override
                protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                    if (startVersion != dataVersion) {
                        // ignore update
                        return;
                    }
                    items = update;
                    //al llamar al metodo dispatchUptedTo notificamos al
                    //adapter del cambio.
                    diffResult.dispatchUpdatesTo(DataBoundListAdapter.this);

                }
            }.execute();
        }
    }

    protected abstract void bind(V binding, T item);

    protected abstract boolean areItemsTheSame(T oldItem, T newItem);

    protected abstract boolean areContentsTheSame(T oldItem, T newItem);

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}
