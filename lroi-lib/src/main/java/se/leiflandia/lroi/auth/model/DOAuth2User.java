package se.leiflandia.lroi.auth.model;

import java.util.List;


public class DOAuth2User extends AbstractLongEntity {

    public static final int UNVERIFIED_STATE = 0;
    public static final int ACTIVE_STATE = 1;
    public static final int LOCKED_STATE = 2;

    private String displayName;

    private String email;

    private String profileLink;

    private List<String> roles;

    /**
     * State of the user account.
     * 0 - unverified; user has not verified the email yet
     * 1 - verified; user has verified the email
     * 2 - locked; account is locked
     */
    private Integer state;

    private String thumbnailUrl;

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Integer getState() {
        return state;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    private DOAuth2User() { super(null); }

    public DOAuth2User(Long id, String displayName, String email, String profileLink,
                       List<String> roles, Integer state, String thumbnailUrl) {
        super(id);
        this.displayName = displayName;
        this.email = email;
        this.profileLink = profileLink;
        this.roles = roles;
        this.state = state;
        this.thumbnailUrl = thumbnailUrl;
    }


}
