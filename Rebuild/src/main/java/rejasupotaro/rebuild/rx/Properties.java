package rejasupotaro.rebuild.rx;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public final class Properties {
    private Properties() {
    }

    /**
     * Updates the <em>enabled</em> property of a view for each event received.
     */
    public static EnabledProperty enabledFrom(View view) {
        return new EnabledProperty(view);
    }

    /**
     * Updates the <em>text</em> property of a view for each event received.
     */
    public static TextProperty textFrom(TextView view) {
        return new TextProperty(view);
    }

    /**
     * Clear, update and notify changes of dataset for {@code ArrayAdapter}.
     */
    public static <T> ArrayAdapterProperty<T> dataSetFrom(ArrayAdapter<T> adapter) {
        return new ArrayAdapterProperty<T>(adapter);
    }

    public static class EnabledProperty implements Action1<Boolean> {

        private final View view;

        private EnabledProperty(View view) {
            this.view = view;
        }

        public void set(Observable<Boolean> observable) {
            observable.subscribe(this);
        }

        @Override
        public void call(Boolean enabled) {
            view.setEnabled(enabled);
        }
    }

    public static class TextProperty implements Action1<String> {

        private final TextView view;

        private TextProperty(TextView view) {
            this.view = view;
        }

        public void set(Observable<String> observable) {
            observable.subscribe(this);
        }

        @Override
        public void call(String text) {
            view.setText(text);
        }
    }

    public static class ArrayAdapterProperty<T> implements Action1<List<T>> {

        private final ArrayAdapter<T> adapter;

        ArrayAdapterProperty(ArrayAdapter<T> adapter) {
            this.adapter = adapter;
        }

        public void set(Observable<List<T>> observable) {
            observable.subscribe(this);
        }

        @Override
        public void call(List<T> items) {
            adapter.clear();
            for (T item : items) {
                adapter.add(item);
            }
            adapter.notifyDataSetChanged();
        }
    }
}

