package DB.ApiResponseHelpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import DB.RewardType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "reward_type",
        "used_reward"
})
public class RewardResponse {

    @JsonProperty("date")
    private String date;

    @JsonProperty("reward_type")
    private String reward_type;

    @JsonProperty("used_reward")
    private String usedreward;

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("reward_type")
    public RewardType getRewardtype() {
        return RewardType.fromString(reward_type);
    }

    @JsonProperty("reward_type")
    public void setRewardType(String rewardtype) {
        this.reward_type = rewardtype;
    }

    @JsonProperty("used_reward")
    public Boolean getUsedReward(){ return Boolean.parseBoolean(usedreward); }

    @JsonProperty("used_reward")
    public void setUsedReward(String usedreward) {
        this.usedreward = usedreward;
    }


}