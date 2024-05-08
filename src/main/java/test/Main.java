package test;

public class Main {
    public static void main(String[] args) {
        // Create service instance
     /*   ServicePublication sp = new ServicePublication();

        // Create some publication instances
        publication pub1 = new publication( 14,1, "Titre 111", "Description 1", "image1.png", new java.sql.Date(new Date().getTime()));
        publication pub2 = new publication(2, "Titre 22ddd2", "Description 2", "image2.png", new java.sql.Date(new Date().getTime()));
        publication pub3 = new publication(3, "Titre 322", "Description 3", "image3.png", new java.sql.Date(new Date().getTime()));

       // Add publications
        try {
            sp.ajouter(pub1);
            System.out.println("Publication 1 added");
            sp.ajouter(pub2);
            System.out.println("Publication 2 added");
            // We won't add pub3 to the database for now to avoid trying to modify an unexisting entry.
        } catch (SQLException e) {
            System.out.println("Error during addition: " + e.getMessage());
        }


// Modify publication
        try {
            pub2.setTitreP("Updated Title AZ2");
            pub2.setDescriptionP("Updated Description 2");
            sp.modifier(pub2);
            System.out.println("Publication 2 modified");
        } catch (SQLException e) {
            System.out.println("Error during modification: " + e.getMessage());
        }


        // Delete publication
        try {
            sp.supprimer(pub1);
            System.out.println("Publication 1 deleted");
        } catch (SQLException e) {
            System.out.println("Error during deletion: " + e.getMessage());
        }
*/
       /* // Display all publications
        try {
            List<publication> publications = sp.afficher();
            System.out.println("List of publications:");
            for (publication pub : publications) {
                System.out.println(pub);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    */
    }
}
