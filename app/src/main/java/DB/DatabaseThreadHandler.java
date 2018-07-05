package DB;

import android.content.Context;

import java.util.Date;
import java.util.List;

import DB.Dao.PairDao;
import DB.Dao.PersonDao;
import DB.Dao.RewardDao;
import DB.Tables.Pair;
import DB.Tables.Person;
import DB.Tables.Reward;
import DB.Tables.Threshold;
import io.reactivex.Maybe;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DatabaseThreadHandler {

    private SQLiteDB db;
    private PersonDao personDao;
    private PairDao pairDao;
    private RewardDao rewDao;
    private Scheduler ps;
    private Scheduler as;

    public DatabaseThreadHandler(Context context, Scheduler processScheduler, Scheduler androidScheduler) {
        db = SQLiteDB.getInstance(context);
        personDao = db.personDao();
        pairDao = db.pairDao();
        rewDao = db.rewardDao();
        ps = processScheduler;
        as = androidScheduler;
    }

    public Maybe<List<Person>> allPersons() {
        return Maybe.fromCallable(()
                -> personDao.getAllPersons())
                .subscribeOn(ps)
                .observeOn(as);
    }

    public Maybe<List<Person>> allActivePersons(){
        return Maybe.fromCallable(()
                -> personDao.getAllActivePersons())
                .subscribeOn(ps)
                .observeOn(as);
    }

    public Single<Integer> udpatePerson(Person person){
        return Single.fromCallable(()
        -> personDao.updatePerson(person)).subscribeOn(ps).observeOn(as);
    }

    public Single<Long> addPair(Pair pair) {
        return Single.fromCallable(()
                -> pairDao.insertPair(pair))
                .subscribeOn(ps)
                .observeOn(as);
    }

    public Maybe<List<Pair>> getPairHistory(Date date) {
        return Maybe.fromCallable(()
                -> pairDao.getHistory(date))
                .subscribeOn(ps)
                .observeOn(as);
    }

    public Maybe<Integer> getUnusedRewardsCount(RewardType type) {
        return Maybe.fromCallable(()
                -> rewDao.numberOfUnusedRewards(type))
                .subscribeOn(ps)
                .observeOn(as);
    }

    public Maybe<List<Pair>> getPairsSinceLastReward(RewardType rewardType) {
        return Maybe.fromCallable(()
                -> pairDao.getPairsSinceLastReward(rewardType))
                .subscribeOn(ps)
                .observeOn(as);
    }

    public Single<Integer> setThreshold(Threshold threshold) {
        return Single.fromCallable(()
                -> rewDao.setThreshold(threshold))
                .subscribeOn(ps)
                .observeOn(as);
    }

    public Maybe<Date> getLastReward(RewardType type) {
        return Maybe.fromCallable(()
                -> rewDao.getLastRewardDate(type))
                .subscribeOn(ps)
                .observeOn(as);
    }

    public Single<Long> addNewReward(Reward reward) {
        return Single.fromCallable(()
                -> rewDao.addReward(reward))
                .subscribeOn(ps)
                .observeOn(as);
    }

    public Maybe<Person> getPerson(String username) {
        return Maybe.fromCallable(()
                -> personDao.getPerson(username))
                .subscribeOn(ps)
                .observeOn(as);
    }

    public Single<Integer> getThreshold(RewardType type) {
        return Single.fromCallable(()
                -> rewDao.getThreshold(type))
                .subscribeOn(ps)
                .observeOn(as);
    }

    public Single<Long> addPerson(Person person){
        return Single.fromCallable(()
                -> personDao.insertPerson(person))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
