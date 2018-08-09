package db.responseparsers;

import java.util.ArrayList;
import java.util.List;

import com.bettertogether.app.Pair;
import com.bettertogether.app.Person;

public class ResponsePojoConverter {

    public static PairResponse pairToPairResponse(Pair p){
        PairResponse r = new PairResponse();
        r.setPerson1(p.getPerson1());
        r.setPerson2(p.getPerson2());
        return r;
    }

    private static Pair pairResponseToPair(PairResponse r){
        return new Pair(r.getPerson1(), r.getPerson2());
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

    private static Person personResponseToPerson(PersonResponse r) {
        return new Person(r.getUsername(), r.getName(), r.getImage());
    }

}
