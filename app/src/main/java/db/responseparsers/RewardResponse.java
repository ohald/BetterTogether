package db.responseparsers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import db.RewardType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "reward_type",
        "used_reward"
})
public class RewardResponse {

    @JsonProperty("date")
    @JsonIgnore()
    private String date;

    @JsonProperty("reward_type")
    private String reward_type;

    @JsonProperty("used_reward")
    private String used_reward;

    @JsonProperty("reward_type")
    public RewardType getRewardType() {
        return RewardType.fromString(reward_type);
    }

    @JsonProperty("reward_type")
    public void setRewardType(String reward_type) {
        this.reward_type = reward_type;
    }

    @JsonProperty("used_reward")
    public Boolean getUsedReward(){ return Boolean.parseBoolean(used_reward); }

    @JsonProperty("used_reward")
    public void setUsedReward(String used_reward) {
        this.used_reward = used_reward;
    }


}