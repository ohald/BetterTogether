package db.responseparsers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "person1",
        "person2",
        "date"
})
public class PairResponse {

    @JsonProperty("person1")
    private String person1;
    @JsonProperty("person2")
    private String person2;
    @JsonProperty("date")
    private String date;

    @JsonProperty("person1")
    public String getPerson1() {
        return person1;
    }

    @JsonProperty("person1")
    public void setPerson1(String person1) {
        this.person1 = person1;
    }

    @JsonProperty("person2")
    public String getPerson2() {
        return person2;
    }

    @JsonProperty("person2")
    public void setPerson2(String person2) {
        this.person2 = person2;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }


}
