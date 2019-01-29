package server;

public class Position {

    private final String positionName;
    private final String positionNameShort;

    public Position(String positionName, String positionNameShort) {
        this.positionName = positionName;
        this.positionNameShort = positionNameShort;
    }

    public String getPositionName() {
        return positionName;
    }

    public String getPositionNameShort() {
        return positionNameShort;
    }
}
