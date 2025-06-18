package ambient_intelligence.id;

import java.util.Objects;

public class UserID {
    private String email;
    private String systemID;

    public UserID() {}

    public UserID(String email, String systemID) {
        this.email = email;
        this.systemID = systemID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSystemID() {
        return systemID;
    }

    public void setSystemID(String systemID) {
        this.systemID = systemID;
    }

    @Override
    public String toString() {
        return "UserID [email=" + email + ", systemID=" + systemID + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserID)) return false;
        UserID userID = (UserID) o;
        return Objects.equals(email, userID.email) &&
               Objects.equals(systemID, userID.systemID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, systemID);
    }
}
