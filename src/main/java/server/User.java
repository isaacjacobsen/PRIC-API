package server;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final int userId;
    private final String username;
    private final String name;
    private final String email;
    private final String phone;
    private final String profilePicPath;
    private final String aboutMe;
    private final ArrayList<Position> positions;
    private final boolean isOnBoard;

    public User(int userId, String username, String name, String email, String phone, String profilePicPath, String aboutMe, List<String> positionNames, List<String> positionNamesShort, boolean isOnBoard) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profilePicPath = profilePicPath;
        this.aboutMe = aboutMe;
        this.isOnBoard = isOnBoard;

        positions = new ArrayList<>();
        for (int i = 0; i < positionNames.size(); i++) {
            positions.add(new Position(positionNames.get(i), positionNamesShort.get(i)));
        }
    }

    @JsonProperty("userId")
    public int getUserId() {
        return userId;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("profilePicPath")
    public String getProfilePicPath() {
        return profilePicPath;
    }

    @JsonProperty("aboutMe")
    public String getAboutMe() {
        return aboutMe;
    }

    @JsonProperty("positions")
    public Position[] getPositions() {
        return positions.toArray(new Position[0]);
    }

    @JsonProperty("isOnBoard")
    public boolean getIsOnBoard() {
        return isOnBoard;
    }
}