
package admins;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import static financementparticipatif.HomeAdminController.idRec;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import javax.swing.JOptionPane;
import utils.ConnectionUtil;

public class DashboardController implements Initializable {

    @FXML
    private Label lblNbrProjet;
    @FXML
    private Label lblNbrFinanceurs;
    @FXML
    private Label lblProjetfinancés;
    @FXML
    private Label lblProjetRestants;
    @FXML
    private PieChart pieChart;
    @FXML
    private TextField btnRechercher;
    @FXML
    private Label LblBénifi;
    @FXML
    private FontAwesomeIconView close;
    private double xOffset=0;
    private double yOffset=0;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadChart();
         NombreFinancé();
         NombreProjet();
         NombreRestants();
         NombreFinanceurs();
         NombreBénificiare();
         MoveScene();
          
        
    }    

    public DashboardController() {
     con = ConnectionUtil.conBD();
    }
    Connection con = null; 
    //Diagramme en cercle des catégories
    private void loadChart()
    {

        PieChart.Data slice1 = new PieChart.Data("Solidaire", NombreSolidaire());
        PieChart.Data slice2 = new PieChart.Data("Sportif"  , NombreSportif());
        PieChart.Data slice3 = new PieChart.Data("Culturel" , NombreCulturel());
        PieChart.Data slice4 = new PieChart.Data("Santé" , NombreSanté());
        PieChart.Data slice5 = new PieChart.Data("Animaux" , NombreAnimaux());
        PieChart.Data slice6 = new PieChart.Data("Educatif" , NombreEducatif());
        pieChart.getData().addAll(slice1,slice2,slice3,slice4,slice5,slice6);
    }
       public void MoveScene() {
        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
// rechercher projet par nom
    @FXML
    private void Rechercher(ActionEvent event) {
         String test = "SELECT idProjet FROM projet where NomProjet='" + Character.toUpperCase(btnRechercher.getText().charAt(0)) + btnRechercher.getText().substring(1) + "'";
        PreparedStatement preparedStatement;
        ResultSet rt;
        try {
            preparedStatement = con.prepareStatement(test);
            rt = preparedStatement.executeQuery();
            if (rt.next()) {
                idRec = rt.getInt("idProjet");
                ExplorerProjet(event);

            } else {
                JOptionPane.showMessageDialog(null, "Ce projet n'existe pas");
            }
        } catch (SQLException ex) {

            ex.printStackTrace();
        }
    }
    // les informations complétes des projets pour l'admin
     public void ExplorerProjet(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/admins/VisualisationProjet_Admin.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root1));
            stage.show();
            

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
     //      Récuperer le nombre des projets dans la base de données
    public void NombreProjet() {

        String nmbr1 = "SELECT COUNT(*) FROM projet";
        PreparedStatement preparedStatement;
        ResultSet rt;
        try {
            preparedStatement = con.prepareStatement(nmbr1);
            rt = preparedStatement.executeQuery();
            if (rt.next()) {
                lblNbrProjet.setText(rt.getString(1));

             }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    //      Récuperer le nombre des projets financés dans la base de données

    public void NombreFinancé() {

        String nmbr = "SELECT  COUNT(idProjet) FROM projet where SommeRécolté>=Budget";
        PreparedStatement preparedStatement2;
        ResultSet rt2;
        try {
            preparedStatement2 = con.prepareStatement(nmbr);
            rt2 = preparedStatement2.executeQuery();
            if (rt2.next()) {
                lblProjetfinancés.setText(rt2.getString(1));
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
       
    }
 //      Récuperer le nombre des projets non financés dans la base de données
    private void NombreRestants() {
         String nmbr = "SELECT  COUNT(idProjet) FROM projet where SommeRécolté<Budget";
        PreparedStatement preparedStatement2;
        ResultSet rt2;
        try {
            preparedStatement2 = con.prepareStatement(nmbr);
            rt2 = preparedStatement2.executeQuery();
            if (rt2.next()) {
                lblProjetRestants.setText(rt2.getString(1));
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
     //      Récuperer le nombre des financeurs dans la base de données

    private void NombreFinanceurs() {
        ResultSet rst;
        Statement prepared;
        try {
            
           prepared=con.createStatement();
           String cin = "SELECT  count(Distinct(CinFinanceur)) FROM financement ";
            rst = prepared.executeQuery(cin);
            if (rst.next()) {
           lblNbrFinanceurs.setText(rst.getString(1));
            }
        } catch (SQLException ex) {
             ex.printStackTrace();
        }
    }
     //      Récuperer le nombre des projets de chaque catégorie
     private int NombreSolidaire() {
        ResultSet rst;
        Statement prepared;
        try {
            
           prepared=con.createStatement();
           String cat = "SELECT  count(Distinct(idProjet)) FROM projet where Catégorie= 'Solidaire'";
            rst = prepared.executeQuery(cat);
            if (rst.next()) {
                
                return rst.getInt(1);
            }
        } catch (SQLException ex) {
                ex.printStackTrace();
        }
        return 0;
    }
     private int NombreEducatif() {
        ResultSet rst;
        Statement prepared;
        try {
            
           prepared=con.createStatement();
           String cat = "SELECT  count(Distinct(idProjet)) FROM projet where Catégorie= 'Educatif'";
            rst = prepared.executeQuery(cat);
            if (rst.next()) {
                
                return rst.getInt(1);
            }
        } catch (SQLException ex) {
                ex.printStackTrace();
        }
        return 0;
    }

     private int NombreArt() {
        ResultSet rst;
        Statement prepared;
        try {
            
           prepared=con.createStatement();
           String cat = "SELECT  count(Distinct(idProjet)) FROM projet where Catégorie= 'Art'";
            rst = prepared.executeQuery(cat);
            if (rst.next()) {
                
                return rst.getInt(1);
            }
        } catch (SQLException ex) {
                ex.printStackTrace();
        }
        return 0;
    }
     private int NombreCulturel() {
        ResultSet rst;
        Statement prepared;
        try {
            
           prepared=con.createStatement();
           String cat = "SELECT  count(Distinct(idProjet)) FROM projet where Catégorie= 'Culturel'";
            rst = prepared.executeQuery(cat);
            if (rst.next()) {
                
                return rst.getInt(1);
            }
        } catch (SQLException ex) {
                ex.printStackTrace();
        }
        return 0;
    }
     private int NombreSportif() {
        ResultSet rst;
        Statement prepared;
        try {
            
           prepared=con.createStatement();
           String cat = "SELECT  count(Distinct(idProjet)) FROM projet where Catégorie= 'Sportif'";
            rst = prepared.executeQuery(cat);
            if (rst.next()) {
                
                return rst.getInt(1);
            }
        } catch (SQLException ex) {
                ex.printStackTrace();
        }
        return 0;
    }
    
      private int NombreAnimaux() {
        ResultSet rst;
        Statement prepared;
        try {
            
           prepared=con.createStatement();
           String cat = "SELECT  count(Distinct(idProjet)) FROM projet where Catégorie= 'Animaux'";
            rst = prepared.executeQuery(cat);
            if (rst.next()) {
                
                return rst.getInt(1);
            }
        } catch (SQLException ex) {
                ex.printStackTrace();
        }
        return 0;
    }
        private int NombreSanté() {
        ResultSet rst;
        Statement prepared;
        try {
            
           prepared=con.createStatement();
           String cat = "SELECT  count(Distinct(idProjet)) FROM projet where Catégorie= 'Santé'";
            rst = prepared.executeQuery(cat);
            if (rst.next()) {
                
                return rst.getInt(1);
            }
        } catch (SQLException ex) {
                ex.printStackTrace();
        }
        return 0;
    }


 
 //      Récuperer le nombre des bénéficiaires dans la base de données
    public void NombreBénificiare() {

       String nmbr2 = "SELECT  COUNT(DISTINCT(CinUtilisateur)) FROM projet where etat='Validé'";
        PreparedStatement preparedStatement2;
        ResultSet rt2;
        try {
            preparedStatement2 = con.prepareStatement(nmbr2);
            rt2 = preparedStatement2.executeQuery();
            if (rt2.next()) {
                LblBénifi.setText(rt2.getString(1));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

     @FXML
    private void close(MouseEvent event) {
         Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        
}
}