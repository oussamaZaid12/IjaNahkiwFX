package entities;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private int age;

    private String password;
    private Role role;
    private String email;
    private String image;
    private Boolean isBanned;
    private Boolean isRecaptchaVerified;
    public Boolean getBanned() {
        return isBanned;
    }

    public void setBanned(Boolean banned) {
        isBanned = banned;
    }

    private static User instance;

    public User(int id, String email, String password, String image, Role role, Boolean isBanned, String nom, String prenom, int age,Boolean isRecaptchaVerified) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.image = image;
        this.role = role;
        this.isBanned = isBanned;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.isRecaptchaVerified = isRecaptchaVerified;
    }

    public User(String email, String password, String image, Role role, Boolean isBanned, String nom, String prenom, int age,Boolean isRecaptchaVerified) {
        this.email = email;
        this.password = password;
        this.image = image;
        this.role = role;
        this.isBanned = isBanned;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.isRecaptchaVerified = isRecaptchaVerified;
    }

    public User() {

    }
    public Boolean getRecaptchaVerified() {
        return isRecaptchaVerified;
    }

    public void setRecaptchaVerified(Boolean recaptchaVerified) {
        isRecaptchaVerified = recaptchaVerified;
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
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
                ", image='" + image + '\'' +
                ", isBanned=" + isBanned +
                '}';
    }



}
