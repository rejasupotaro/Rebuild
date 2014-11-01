package rejasupotaro.rebuild.data.services;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.api.TwitterApiClient;
import rejasupotaro.rebuild.data.GuestNameTable;
import rejasupotaro.rebuild.data.models.Guest;
import rejasupotaro.rebuild.jobs.UpdateGuestTask;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class GuestService {
    public Observable<ArrayList<Guest>> getList(List<String> guestNames) {
        return Observable.from(addMiyagawaIfNecessary(guestNames))
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String name) {
                        return GuestNameTable.inquire(name);
                    }
                })
                .map(new Func1<String, Guest>() {
                    @Override
                    public Guest call(String name) {
                        Guest guest = Guest.findByName(name);

                        if (Guest.isEmpty(guest)) {
                            guest = TwitterApiClient.getInstance().getUser(name);
                            if (!Guest.isEmpty(guest)) {
                                guest.save();
                            }
                        } else {
                            new UpdateGuestTask(guest.getName()).execute();
                        }

                        return guest;
                    }
                })
                .filter(new Func1<Guest, Boolean>() {
                    @Override
                    public Boolean call(Guest guest) {
                        return !Guest.isEmpty(guest);
                    }
                })
                .reduce(new ArrayList<Guest>(), new Func2<ArrayList<Guest>, Guest, ArrayList<Guest>>() {
                    @Override
                    public ArrayList<Guest> call(ArrayList<Guest> guests, Guest guest) {
                        guests.add(guest);
                        return guests;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<String> addMiyagawaIfNecessary(List<String> names) {
        if (names.contains("miyagawa")) {
            return names;
        }
        names.add("miyagawa");
        return names;
    }
}
