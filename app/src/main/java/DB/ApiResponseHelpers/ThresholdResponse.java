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
    private RewardType rewardtype;

    @JsonProperty("threshold")
    private Integer threshold;


    @JsonProperty("reward_type")
    public RewardType getRewardtype() {
        return rewardtype;
    }

    @JsonProperty("reward_type")
    public void setRewardtype(String rewardtype) {
        if(rewardtype.toLowerCase().equals("cake"))
            this.rewardtype = RewardType.CAKE;
        else this.rewardtype = RewardType.PIZZA;
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