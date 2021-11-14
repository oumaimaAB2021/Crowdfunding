package Projet;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import financementparticipatif.HomeController;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import utils.ConnectionUtil;

public class TableProjetsController implements Initializable {

    @FXML
    private TableColumn<ModeleView, String> colName, colDate, colCat, colCréateur, colContrepartie;
    @FXML
    private TableColumn<ModeleView, Double> colBudget;
    @FXML
    private TableColumn<ModeleView, ImageView> colImage;
    @FXML
    private TableView<ModeleView> TableView;
    ObservableList<ModeleView> oblist = FXCollections.observableArrayList();
    private ImageView img;
    @FXML
    private FontAwesomeIconView close, pleinEcran, MINUS;
      private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ViewProjects();
        MoveScene();

    }

    public TableProjetsController() {
        con = ConnectionUtil.conBD();

    }
    Connection con = null;
    PreparedStatement preparedStatement = null;
//afficher les projets dans la table
    public void ViewProjects() {
        String type = "SELECT NomProjet,Catégorie,DateCréation,Budget,Contrepartie,CinUtilisateur,Image FROM projet where etat='Validé' ";
        Statement prepared, prepared2;
        ResultSet rst, result;
        try {
            prepared = con.createStatement();
            rst = prepared.executeQuery(type);

            while (rst.next()) {
                //Récupération d'image à partir de la base de donée
                InputStream is = rst.getBinaryStream("Image");
                Image image = new Image(is, 140, 90, false, true);
                img = new ImageView(image);

                // info créateur
                String info = "SELECT  nom , prénom FROM utilisateur  WHERE cin='" + rst.getString("CinUtilisateur") + "'";
                prepared2 = con.createStatement();
                result = prepared2.executeQuery(info);
                if (result.next()) {
                    String InfosCréateur = result.getString("nom") + " " + result.getString("prénom");

                    oblist.add(new ModeleView(rst.getString("NomProjet"), rst.getString("Catégorie"), rst.getString("DateCréation"),
                            Double.parseDouble(rst.getString("Budget")), rst.getString("Contrepartie"), InfosCréateur, img));

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
        TableView.setItems(oblist);
    }
// Récuperation de l'id projet à partir la ligne séléctionée du tableau
    @FXML
    private void GetIndex(MouseEvent event) {
        Statement prepared;
        ResultSet rst;
        try {
            ModeleView Mv = (ModeleView) TableView.getSelectionModel().getSelectedItem();
            String NomP = Mv.getNomProjet();
            String query = "select idProjet from projet where NomProjet='" + NomP + "'";
            prepared = con.createStatement();
            rst = prepared.executeQuery(query);
            while (rst.next()) {
                HomeController.idRec = rst.getInt("idProjet");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VisualisationProjet.fxml"));
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
    private void close(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
 
        Scene scene;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/financementparticipatif/Home.fxml")));
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
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
//plein écran
    @FXML
    private void max(MouseEvent event) {
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.setFullScreen(true);
    }

    @FXML
    private void min(MouseEvent event) {
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

}
