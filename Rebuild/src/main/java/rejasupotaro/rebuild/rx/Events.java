package rejasupotaro.rebuild.rx;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.models.Episode;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public final class Events {

    private Events() {
    }

    public static Observable<String> text(TextView view) {
        String currentText = String.valueOf(view.getText());
        final BehaviorSubject<String> subject = BehaviorSubject.create(currentText);
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                subject.onNext(editable.toString());
            }
        });
        return subject;
    }

    public static Observable<Object> click(View target) {
        final PublishSubject<Object> subject = PublishSubject.create();
        target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subject.onNext(new Object());
            }
        });
        return subject;
    }

    public static Observable<Integer> itemClick(AbsListView target) {
        final PublishSubject<Integer> subject = PublishSubject.create();
        target.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                subject.onNext(i);
            }
        });
        return subject;
    }
}

