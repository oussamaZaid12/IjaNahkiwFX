package entities;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private int age;

    private String password;
    private Role role;
    private String email;
    private String profile_picture;
    private Boolean isBanned;

    public Boolean getBanned() {
        return isBanned;
    }

    public void setBanned(Boolean banned) {
        isBanned = banned;
    }

    private static User instance;

    public User(int id, String email, String password, String profile_picture, Role role, Boolean isBanned, String nom, String prenom, int age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.profile_picture = profile_picture;
        this.role = role;
        this.isBanned = isBanned;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }

    public User(String email, String password, String profile_picture, Role role, Boolean isBanned, String nom, String prenom, int age) {
        this.email = email;
        this.password = password;
        this.profile_picture = profile_picture;
        this.role = role;
        this.isBanned = isBanned;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return profile_picture;
    }

    public void setImage(String image) {
        this.profile_picture = image;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public static User getInstance() {
        return instance;
    }

    public static void setInstance(User instance) {
        User.instance = instance;
    }



    @Override
    public String toString() {
        return "Personne{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", age=" + age +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", image='" + profile_picture + '\'' +
                ", isBanned=" + isBanned +
                '}';
    }



}
