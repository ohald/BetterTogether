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
        "rewardtype",
        "threshold"
})
public class ParsedThreshold {

    @JsonProperty("rewardtype")
    private RewardType rewardtype;

    @JsonProperty("threshold")
    private Integer threshold;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("rewardtype")
    public RewardType getRewardtype() {
        return rewardtype;
    }

    @JsonProperty("rewardtype")
    public void setRewardtype(String rewardtype) {
        switch (rewardtype) {
            case "cake":
                this.rewardtype = RewardType.CAKE;
                break;
            case "pizza":
                this.rewardtype = RewardType.PIZZA;
                break;
            default:
                this.rewardtype = null;
        }
    }

    @JsonProperty("threshold")
    public Integer getThreshold() {
        return threshold;
    }

    @JsonProperty("threshold")
    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "ParsedThreshold{" +
                "rewardtype='" + rewardtype + '\'' +
                ", threshold=" + threshold +
                '}';
    }
}