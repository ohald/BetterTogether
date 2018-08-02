package db;

public enum RewardType {

    CAKE,
    PIZZA;

    public static RewardType fromString(String type){
        for(RewardType r : RewardType.values()){
            if(r.toString().equalsIgnoreCase(type)){
                return r;
            }
        }
        throw new IllegalArgumentException("No RewardType named: " + type + ".");
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
