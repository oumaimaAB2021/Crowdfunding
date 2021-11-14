package admins;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import financementparticipatif.HomeAdminController;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import utils.ConnectionUtil;

public class VisualisationProjet_AdminController implements Initializable {

    @FXML
    private ImageView imageView, imageProjet;
    @FXML
    private AnchorPane root;
    int count;
    @FXML
    private FontAwesomeIconView close;
    @FXML
    private Label Labelwelcome;
    @FXML
    private JFXTextField AffichageBudget, AffichageCat, AffichageDate, AffichageType, AffichageContrepartie,
            AffichageStatut, AffichagePaiement, AffichageCréateur, AffichageNom, AffichageCréateur1;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private JFXTextArea AffichageDescription;
    @FXML
    private Button valider;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        slideShow();
        GetData();
        MoveScene();

    }

    public VisualisationProjet_AdminController() {
        con = ConnectionUtil.conBD();
    }

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    //Récupération des données
    public void GetData() {
        try {

            //query
            String query = "SELECT * FROM projet Where idProjet='" + HomeAdminController.idRec + "'";
            preparedStatement = con.prepareStatement(query);
            ResultSet rst = preparedStatement.executeQuery();

            if (rst.next()) {
                AffichageNom.setText(rst.getString("NomProjet").toUpperCase());
                AffichageCat.setText(rst.getString("Catégorie"));
                AffichageStatut.setText(rst.getString("Statut"));
                AffichageDate.setText(rst.getString("DateCréation"));
                AffichageDescription.setText(rst.getString("Description"));
                AffichageBudget.setText(rst.getString("SommeRécolté") + "/" + rst.getString("Budget"));
                AffichageType.setText(rst.getString("TypeDeFinancement"));
                AffichagePaiement.setText(rst.getString("MoyenDePaiement"));
                AffichageContrepartie.setText(rst.getString("Contrepartie"));
                InputStream is = rst.getBinaryStream("Image");
                Image image = new Image(is, 160, 131, false, true);
                imageProjet.setImage(image);
                imageProjet.setFitWidth(160);
                imageProjet.setFitHeight(131);
                // nombre de projet par créateur
                String nbr = "SELECT count(*) From projet  WHERE CinUtilisateur='" + rst.getString("CinUtilisateur") + "'";
                PreparedStatement nombre = con.prepareStatement(nbr);
                ResultSet total = nombre.executeQuery();
                // info créateur
                String info = "SELECT  nom , prénom FROM utilisateur  WHERE cin='" + rst.getString("CinUtilisateur") + "'";
                PreparedStatement prepared = con.prepareStatement(info);
                ResultSet result = prepared.executeQuery();
                if (result.next() && total.next()) {
                    AffichageCréateur.setText(result.getString("nom") + " " + result.getString("prénom"));
                    AffichageCréateur1.setText("\n Nombre de projets: " + total.getInt(1));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
//animation des images

    public void slideShow() {
        ArrayList<Image> images = new ArrayList<Image>();
        images.add(new Image("/bannier/1.jpg"));
        images.add(new Image("/bannier/2.jpg"));
        images.add(new Image("/bannier/4.PNG"));
        images.add(new Image("/bannier/5.PNG"));
        images.add(new Image("/bannier/6.PNG"));
        images.add(new Image("/bannier/7.jpg"));
        images.add(new Image("/bannier/8.PNG"));
        images.add(new Image("/bannier/3.PNG"));
        images.add(new Image("/bannier/9.png"));
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4), event -> {
            imageView.setImage(images.get(count));
            count++;
            if (count == 9) {
                count = 0;
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

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

    @FXML
    private void close(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();

    }

    private void handleButtonAction(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("FormulaireFinancement.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
//  validation des projets par l'admin

    @FXML
    private void ValiderProjets(ActionEvent event) {
        String etat = "select etat from projet where idProjet='" + HomeAdminController.idRec + "'";
        try {

            preparedStatement = con.prepareStatement(etat);
            ResultSet rst = preparedStatement.executeQuery();

            if (rst.next()) {
                String sql = "update projet set etat='Validé' where idProjet='" + HomeAdminController.idRec + "'";

                try {
                    if (rst.getString("etat").equals("Non validé")) {

                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(sql);
                        JOptionPane.showMessageDialog(null, "Projet validé avec succes");
                    } else {
                        JOptionPane.showMessageDialog(null, "Projet déjà validé");
                    }
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

        }
    }
}
