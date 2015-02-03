package se.leiflandia.lroi.auth.model;

import java.io.Serializable;
import java.util.List;

public class DUser extends DOAuth2User implements Serializable {

    /**
     * A unique user name
     */
    private String username;

    /**
     * User password (hashed)
     */
    private String password;

    /**
     * User users marked as friends.
     * Can be used to create associations between users. The exact meaning of the relations is up to the domain.
     */
    private List<Long> friends;

    /**
     * First name.
     */
    private String firstName;

    /**
     * Last name.
     */
    private String lastName;

    /**
     * Address line 1.
     */
    private String address1;

    /**
     * Address line 2.
     * Project and country specific use.
     */
    private String address2;

    /**
     * ZIP code.
     */
    private String zipCode;

    /**
     * City
     */
    private String city;

    /**
     * Country.
     */
    private String country;

    /**
     * Phone number 1.
     * Project and country specific use.
     */
    private String phoneNumber1;

    /**
     * Phone number 2.
     * Project and country specific use.
     */
    private String phoneNumber2;

    /**
     * Birth related information.
     * Project specific user, e.g. birth year.
     */
    private String birthInfo;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Long> getFriends() {
        return friends;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public String getBirthInfo() {
        return birthInfo;
    }

    public DUser(Builder builder) {
        super(builder.id, builder.displayName, builder.email, builder.profileLink, builder.roles,
                builder.state, builder.thumbnailUrl);
        this.username = builder.username;
        this.password = builder.password;
        this.friends = builder.friends;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.address1 = builder.address1;
        this.address2 = builder.address2;
        this.zipCode = builder.zipCode;
        this.city = builder.city;
        this.country = builder.country;
        this.phoneNumber1 = builder.phoneNumber1;
        this.phoneNumber2 = builder.phoneNumber2;
        this.birthInfo = builder.birthInfo;
    }

    private DUser() { super(null, null, null, null, null, null, null); }

    public static class Builder {

        // AbstractLongEntity
        private Long id;

        // DOAuth2User
        private String displayName;
        private String email;
        private String profileLink;
        private List<String> roles;
        private Integer state;
        private String thumbnailUrl;

        // DUser
        private String username;
        private String password;
        private List<Long> friends;
        private String firstName;
        private String lastName;
        private String address1;
        private String address2;
        private String zipCode;
        private String city;
        private String country;
        private String phoneNumber1;
        private String phoneNumber2;
        private String birthInfo;


        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setProfileLink(String profileLink) {
            this.profileLink = profileLink;
            return this;
        }

        public Builder setRoles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder setState(Integer state) {
            this.state = state;
            return this;
        }

        public Builder setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setFriends(List<Long> friends) {
            this.friends = friends;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setAddress1(String address1) {
            this.address1 = address1;
            return this;
        }

        public Builder setAddress2(String address2) {
            this.address2 = address2;
            return this;
        }

        public Builder setZipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setPhoneNumber1(String phoneNumber1) {
            this.phoneNumber1 = phoneNumber1;
            return this;
        }

        public Builder setPhoneNumber2(String phoneNumber2) {
            this.phoneNumber2 = phoneNumber2;
            return this;
        }

        public Builder setBirthInfo(String birthInfo) {
            this.birthInfo = birthInfo;
            return this;
        }

        public DUser build() {
            return new DUser(this);
        }
    }
}
