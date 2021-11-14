
package admins;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import financementparticipatif.HomeAdminController;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import utils.ConnectionUtil;


public class TableProjets_AdminController implements Initializable {

    @FXML
    private TableColumn<ModeleViewAdmin, String> colName,colDate, colCat,colCréateur,colContrepartie;
    @FXML
    private TableColumn<ModeleViewAdmin, Double> colBudget;
    @FXML
    private TableColumn<ModeleViewAdmin, ImageView> colImage;
    @FXML
    private TableView<ModeleViewAdmin> TableView;
    ObservableList<ModeleViewAdmin> oblist = FXCollections.observableArrayList();
    private ImageView img;
    @FXML
    private FontAwesomeIconView pleinEcran, MINUS,closeA;
    @FXML
    private TableColumn<?, ?> colEtat;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ViewProjects();

    }

    public TableProjets_AdminController() {
        con = ConnectionUtil.conBD();

    }
    Connection con = null;
    PreparedStatement preparedStatement = null;
     // affichage des projets dans la table pour admin

    public void ViewProjects() {
        String type = "SELECT NomProjet,Catégorie,DateCréation,Budget,Contrepartie,CinUtilisateur,Image,etat FROM projet  ";
        Statement prepared, prepared2;
        ResultSet rst, result;
        try {
            prepared = con.createStatement();
            rst = prepared.executeQuery(type);

            while (rst.next()) {
                InputStream is = rst.getBinaryStream("Image");
                Image image = new Image(is, 140, 90, false, true);
                img = new ImageView(image);

                // info créateur
                String info = "SELECT  nom , prénom FROM utilisateur  WHERE cin='" + rst.getString("CinUtilisateur") + "'";
                prepared2 = con.createStatement();
                result = prepared2.executeQuery(info);
                if (result.next()) {
                    String InfosCréateur = result.getString("nom") + " " + result.getString("prénom");

                    oblist.add(new ModeleViewAdmin(rst.getString("NomProjet"), rst.getString("Catégorie"), rst.getString("DateCréation"),
                            Double.parseDouble(rst.getString("Budget")), rst.getString("Contrepartie"), InfosCréateur, img,rst.getString("etat")));

                }
            }

        } catch (SQLException ex) {
            System.err.println("Erreur d'affichage des projets");
        }
        colName.setCellValueFactory(new PropertyValueFactory<>("NomProjet"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("DateCréation"));
        colCat.setCellValueFactory(new PropertyValueFactory<>("Catégorie"));
        colBudget.setCellValueFactory(new PropertyValueFactory<>("Budget"));
        colContrepartie.setCellValueFactory(new PropertyValueFactory<>("Contrepartie"));
        colCréateur.setCellValueFactory(new PropertyValueFactory<>("InfosCréateur"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("imagView"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        TableView.setItems(oblist);
    }
// récupération de l'idProjet à partir  de la ligne séléctionée
    @FXML
    private void GetIndex(MouseEvent event) {
        Statement prepared;
        ResultSet rst;
        try {
            ModeleViewAdmin Mv = (ModeleViewAdmin) TableView.getSelectionModel().getSelectedItem();
            String NomP = Mv.getNomProjet();
            String query = "select idProjet from projet where NomProjet='" + NomP + "'";
            prepared = con.createStatement();
            rst = prepared.executeQuery(query);
            while (rst.next()) {
                HomeAdminController.idRec = rst.getInt("idProjet");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VisualisationProjet_Admin.fxml"));
                Parent root1;
                try {
                    root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(new Scene(root1));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void closeA(MouseEvent event) {
      Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Scene scene;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/financementparticipatif/HomeAdmin.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
      
    }

    @FXML
    private void maxA(MouseEvent event) {
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.setFullScreen(true);
    }

    @FXML
    private void minA(MouseEvent event) {
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

}
