package db.responseparsers;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import db.RewardType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "reward_type",
        "threshold"
})
public class ThresholdResponse {

    @JsonProperty("reward_type")
    private String reward_type;

    @JsonProperty("threshold")
    private Integer threshold;

    @JsonProperty("reward_type")
    public RewardType getRewardType() {
        return RewardType.fromString(reward_type);
    }

    @JsonProperty("reward_type")
    public void setRewardType(String reward_type) {
        this.reward_type = reward_type;
    }

    @JsonProperty("threshold")
    public Integer getThreshold() {
        return threshold;
    }

    @JsonProperty("threshold")
    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

}
