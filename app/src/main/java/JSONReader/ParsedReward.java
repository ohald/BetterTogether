package JSONReader;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

import DB.RewardType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "rewardtype",
        "usedreward"
})
public class ParsedReward {

    @JsonProperty("date")
    private String date;
    @JsonProperty("rewardtype")
    private String rewardtype;
    @JsonProperty("usedreward")
    private String usedreward;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("rewardtype")
    public RewardType getRewardtype() {
        return rewardtype.equals("cake") ? RewardType.CAKE : RewardType.PIZZA;
    }

    @JsonProperty("rewardtype")
    public void setRewardtype(String rewardtype) {
        this.rewardtype = rewardtype;
    }

    @JsonProperty("usedreward")
    public boolean getUsedreward() {
        return usedreward.equals("true") ? true : false;
    }

    @JsonProperty("usedreward")
    public void setUsedreward(String usedreward) {
        this.usedreward = usedreward;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}