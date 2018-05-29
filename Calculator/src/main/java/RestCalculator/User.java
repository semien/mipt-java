package RestCalculator;

public class User {
    private String username;
    private String password;
    private boolean enabled;

    public User(String name, String password, boolean enabled){
        this.username = name;
        this.password = password;
        this.enabled = enabled;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User that = (User) o;

        if (enabled != that.enabled) {
            return false;
        }
        if (!username.equals(that.username)) {
            return false;
        }
        return password.equals(that.password);

    }
}
