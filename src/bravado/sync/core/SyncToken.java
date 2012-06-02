package bravado.sync.core;

/**
 * SyncToken
 * bob
 * 6/2/12 5:10 PM
 */
public class SyncToken {
    private String location;
    private String owner;

    public SyncToken() {

    }

    public SyncToken(String location, String owner) {
        this.location = location;
        this.owner = owner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
