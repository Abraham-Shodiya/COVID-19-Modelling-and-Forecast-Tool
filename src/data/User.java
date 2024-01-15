package data;

public class User {
    private final int id;
    private final String firstName;
    private final String surname;
    private final String password;
    private final boolean viewDeaths;
    private final boolean viewCases;
    private final boolean viewPredictions;
    private final boolean addUser;

    public User(int id, String firstName, String surname, String password, boolean viewDeaths, boolean viewCases, boolean viewPredictions, boolean addUser) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.viewDeaths = viewDeaths;
        this.viewCases = viewCases;
        this.viewPredictions = viewPredictions;
        this.addUser = addUser;
    }

    public User(String firstName, String surname, String password, boolean viewDeaths, boolean viewCases, boolean viewPredictions, boolean addUser) {
        this.id = 0;
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.viewDeaths = viewDeaths;
        this.viewCases = viewCases;
        this.viewPredictions = viewPredictions;
        this.addUser = addUser;
    }

    public int getId(){
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public boolean isViewDeaths() {
        return viewDeaths;
    }

    public boolean isViewCases() {
        return viewCases;
    }

    public boolean isViewPredictions() {
        return viewPredictions;
    }

    public boolean isAddUser() {
        return addUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", viewDeaths=" + viewDeaths +
                ", viewCases=" + viewCases +
                ", viewPredictions=" + viewPredictions +
                ", addUser=" + addUser +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return viewDeaths == user.viewDeaths && viewCases == user.viewCases && viewPredictions == user.viewPredictions && addUser == user.addUser && firstName.equals(user.firstName) && surname.equals(user.surname) && password.equals(user.password);
    }
}
