package services;


import org.mindrot.jbcrypt.BCrypt;
import entities.Role;
import entities.User;
import utils.MyDB;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserService  implements IUser<User>{
    private MyDB myConnection = MyDB.getInstance();
    private Connection connection = myConnection.getConnection();

    public boolean UserExistsById(int userId) {
        String query = "SELECT COUNT(*) FROM user WHERE Id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public void updateUser(User user) {
        String query = "UPDATE user SET nom = ?, prenom = ?, email = ?, is_banned = ?, age = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getPrenom());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setBoolean(4, user.getBanned());
            preparedStatement.setInt(5, user.getAge());
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean UserExistsByEmail(String email) {
        String query = "SELECT COUNT(*) FROM user WHERE Email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
public String getVerificationCodeByEmail(String email) {
    String query = "SELECT verification_code FROM user WHERE Email = ?";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String verificationCode = resultSet.getString("verification_code");
            return verificationCode;
        }
    } catch (SQLException ex) {
        Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}
    public boolean ajouterUser(User user) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        System.out.println(user);
        String query = "INSERT INTO user(`email`, `password`, `profile_picture`, `roles` ,`nom`,`prenom`,`age`) VALUES (?, ?, ?, ?, ?,?,?)";
        if(UserExistsByEmail(user.getEmail())){
            System.out.println("User already exists!");
            return false;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getEmail());
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
            System.out.print(user.getPassword());
            System.out.print(hashedPassword);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, user.getImage());
            String s = "[\"" + user.getRole().toString().toUpperCase() + "\"]";
            preparedStatement.setString(4, s);
            preparedStatement.setString(5, user.getNom());
            preparedStatement.setString(6, user.getPrenom());
            preparedStatement.setInt(7, user.getAge());
            preparedStatement.executeUpdate();
            System.out.println("User added successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            return false;

        }
        return true;
    }

    public List<User> afficherUser() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("Id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setImage(resultSet.getString("profile_picture"));
                user.setRole(Role.valueOf(resultSet.getString("roles")));
                users.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

    public  void changePasswordById(int userId,String newPassword){
        String query = "UPDATE `user` SET `password`=? WHERE `Id`=? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setInt(2, userId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("No user found with the given ID.");
            }
        }catch(SQLException ex){
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void modifierUser(User user) {
        String query = "UPDATE `user` SET `email`=?, `password`=?, `profile_picture`=?,`roles`=? ,`is_banned`=? WHERE `Id`=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getImage());
            String s = "[\"" + user.getRole().toString().toUpperCase() + "\"]";
            preparedStatement.setString(4,s);
            preparedStatement.setBoolean(5, user.getBanned());
            preparedStatement.setInt(6, user.getId());


            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("No user found with the given ID.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteUser(User user) {
        String query = "DELETE FROM user WHERE Id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user.getId());

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("No user found with the given ID.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User getUserById(int userId) {
        User user = null;
        String query = "SELECT * FROM user WHERE Id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("Id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setImage(resultSet.getString("profile_picture"));
              //  user.setRole(Role.valueOf(resultSet.getString("roles")));
                String roleString = resultSet.getString("roles");
                roleString = roleString.replaceAll("[\\[\\]\"]", ""); // Remove square brackets and quotes
                Role role = Role.valueOf(roleString);
                user.setRole(role);
                user.setNom(resultSet.getString("nom"));
                user.setPrenom(resultSet.getString("prenom"));
                user.setAge(resultSet.getInt("age"));

                user.setBanned(resultSet.getBoolean("is_banned"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public int getIdByEmail(String email) {
        int userId = -1; // Default value indicating no user found with the provided email

        String query = "SELECT Id FROM user WHERE Email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt("Id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userId;
    }
    public List<User> getAll() {

        List<User> list = new ArrayList<>();
        try {


            String req = "SELECT * FROM `user` ";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                User u = new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("profile_picture"),
                        Role.valueOf(rs.getString("roles").replaceAll("[\"\\[\\]]", "")),
                        rs.getBoolean("is_banned"),
                        rs.getString("nom"), // Ajout du nom depuis la base de données
                        rs.getString("prenom"), // Ajout du prénom depuis la base de données
                        rs.getInt("age") // Ajout de l'âge depuis la base de données
                );
                list.add(u);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return list;
    }
    public String getRoleByEmail(String email) {
        String role = "Unknown"; // Default value

        String query = "SELECT `roles` FROM user WHERE Email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                role = resultSet.getString("roles");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return role;
    }




    public User authenticateUser(String email, String Mdp) {
        User user = null;
        try {
            String query = "SELECT * FROM user WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, Mdp);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                user = new User();
                user.setId(result.getInt("id"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
                // Set other user attributes from the database
                user.setImage(result.getString("profile_picture"));
                user.setRole(Role.valueOf(result.getString("roles")));
                user.setBanned(result.getBoolean("is_banned"));

                System.out.println("User Retrieved: " + user.toString());
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

public boolean verifyCode(String code) {
    boolean isValid = false;
    String query = "SELECT * FROM user WHERE verification_code = ?";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, code);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            isValid = true;
        }
    } catch (SQLException ex) {
        Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
    }
    return isValid;
}

    public User getUserByEmail(String text) {
        User user = null;
        String query = "SELECT * FROM user WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, text);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setImage(resultSet.getString("profile_picture"));
               // user.setRole(Role.valueOf(resultSet.getString("roles")));
                String roleString = resultSet.getString("roles");
                roleString = roleString.replaceAll("[\\[\\]\"]", ""); // Remove square brackets and quotes
                Role role = Role.valueOf(roleString);
                user.setRole(role);
                user.setNom(resultSet.getString("nom")); // Vous définissez le nom ici
                user.setPrenom(resultSet.getString("prenom")); // Vous définissez le prénom ici
                user.setAge(resultSet.getInt("age"));

                user.setBanned(resultSet.getBoolean("is_banned"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public void changePasswordByEmail(String text, String hashedPassword) {
        String query = "UPDATE user SET password = ? WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, text);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   public void updateImage(User user, String image) {
       String query = "UPDATE user SET profile_picture = ? WHERE id = ?";
       try {
           PreparedStatement preparedStatement = connection.prepareStatement(query);
           preparedStatement.setString(1, user.getImage());
           preparedStatement.setInt(2, user.getId());
           preparedStatement.executeUpdate();

       } catch (SQLException ex) {
           Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
    public void updateProfile(User user) {
        try {
            // Update user information
            String updateUserQuery = "UPDATE user SET email=?, profile_picture=?, nom=?, prenom=?, age=? WHERE id=?";
            PreparedStatement updateUserStatement = connection.prepareStatement(updateUserQuery);
            updateUserStatement.setString(1, user.getEmail());
            updateUserStatement.setString(2, user.getImage());
            updateUserStatement.setString(3, user.getNom());
            updateUserStatement.setString(4, user.getPrenom());
            updateUserStatement.setInt(5, user.getAge());
            updateUserStatement.setInt(6, user.getId());

            int userRowsUpdated = updateUserStatement.executeUpdate();
            updateUserStatement.close();

            if (userRowsUpdated > 0 ) {
                System.out.println("Profile updated successfully!");
            } else {
                System.out.println("Failed to update profile.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void chngeUserActive(User user, boolean active) {
        String query = "UPDATE user SET is_banned = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, active);
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
/*
    //stat/////////
    public Map<Date, Integer> getUserDataByDate() {
        Map<Date, Integer> userDataByDate = new HashMap<>();
        String query = "SELECT date, COUNT(*) AS user_count FROM user GROUP BY date";

        try (
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Date dateRegistered = resultSet.getDate("date");
                int userCount = resultSet.getInt("user_count");
                userDataByDate.put(dateRegistered, userCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately
        }

        return userDataByDate;
    }
*/


    public Map<String, Integer> getUserDataByStatus() {
        Map<String, Integer> userDataByStatus = new HashMap<>();
        String query = "SELECT is_banned, COUNT(*) AS status_count FROM user GROUP BY is_banned";

        try (
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int isBanned = resultSet.getInt("is_banned");
                String status = (isBanned == 1) ? "unbanned" : "banned";
                int statusCount = resultSet.getInt("status_count");
                userDataByStatus.put(status, statusCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately
        }

        return userDataByStatus;
    }
    public Map<String, Integer> getTherapistAndPatientCount() {
        Map<String, Integer> countMap = new HashMap<>();
        String query = "SELECT roles, COUNT(*) AS count FROM user WHERE JSON_CONTAINS(roles, ?) OR JSON_CONTAINS(roles, ?) GROUP BY roles";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "\"ROLE_THERAPEUTE\"");
            statement.setString(2, "\"ROLE_PATIENT\"");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String role = resultSet.getString("roles");
                int count = resultSet.getInt("count");
                countMap.put(role, count);
            }

            // Affichage des résultats dans la console
            System.out.println("Nombre de thérapeutes et patients : ");
            for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer SQLException de manière appropriée
        }

        return countMap;
    }
    public double getAveragePatientAge() {
        double totalAge = 0;
        int patientCount = 0;
        String query = "SELECT AVG(age) AS average_age FROM user WHERE JSON_CONTAINS(roles, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "\"ROLE_PATIENT\"");
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalAge = resultSet.getDouble("average_age");
                    patientCount++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer SQLException de manière appropriée
        }
        System.out.println("Average patient age: " + totalAge);
        return totalAge;
    }
}
