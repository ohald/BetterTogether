package DB.ApiResponseHelpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.BetterTogether.app.Pair;
import com.BetterTogether.app.Person;
import com.BetterTogether.app.Reward;
import com.BetterTogether.app.Threshold;

public class ResponsePojoConverter {

    public static PairResponse pairToPairResponse(Pair p){
        PairResponse r = new PairResponse();
        r.setPerson1(p.getPerson1());
        r.setPerson2(p.getPerson2());
        r.setDate(Long.toString(p.getDate().getTime()));
        return r;
    }

    public static Pair pairResponseToPair(PairResponse r){
        return new Pair(r.getPerson1(), r.getPerson2(), new Date(Long.parseLong(r.getDate())));
    }

    public static List<Pair> pairResponseToPair(List<PairResponse> responses){
        List<Pair> pairs = new ArrayList<>();
        for(PairResponse r : responses)
            pairs.add(pairResponseToPair(r));
        return pairs;
    }

    public static List<Person> personResponseToPerson(List<PersonResponse> response) {
        List<Person> persons = new ArrayList<>();
        for (PersonResponse r : response)
            persons.add(personResponseToPerson(r));
        return persons;
    }

    public static Person personResponseToPerson(PersonResponse r) {
        //TODO: need solution for images from slack.
        return new Person(r.getUsername(), r.getName(),null, r.getActive());
    }

    public static PersonResponse personToPersonResponse(Person p) {
        PersonResponse r = new PersonResponse();
        r.setUsername(p.getUsername());
        r.setName(p.getName());
        return r;
    }

    public static RewardResponse rewardToRewardResponse(Reward r){
        RewardResponse res = new RewardResponse();
        res.setDate(Long.toString(r.getDate().getTime()));
        res.setRewardType(r.getType().toString());
        res.setUsedReward(Boolean.toString(r.isUsedReward()));
        return res;
    }

    public static ThresholdResponse thresholdToThresholdResponse(Threshold t){
        ThresholdResponse r = new ThresholdResponse();
        r.setRewardType(t.getType().toString());
        r.setThreshold(t.getThreshold());
        return r;
    }

}
