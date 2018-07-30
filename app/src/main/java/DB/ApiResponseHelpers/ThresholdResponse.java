package DB.ApiResponseHelpers;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import DB.RewardType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "reward_type",
        "threshold"
})
public class ThresholdResponse {

    @JsonProperty("reward_type")
    private String rewardtype;

    @JsonProperty("threshold")
    private Integer threshold;


    @JsonProperty("reward_type")
    public RewardType getRewardtype() {
        return RewardType.fromString(rewardtype);
    }

    @JsonProperty("reward_type")
    public void setRewardType(String rewardtype) {
        this.rewardtype = rewardtype;
    }

    @JsonProperty("threshold")
    public Integer getThreshold() {
        return threshold;
    }

    @JsonProperty("threshold")
    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return "ParsedThreshold{" +
                "reward_type='" + rewardtype + '\'' +
                ", threshold=" + threshold +
                '}';
    }
}