package financementparticipatif;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import utils.ConnectionUtil;

public class HomeAdminController implements Initializable {

    public static String Caté;
    @FXML
    private Button btnExplorer, btnDéconnecter;

    public static int idRec;

    public static float nombre;

    @FXML
    private Button btnFinanceur;
    @FXML
    private Button btnlanceur;
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnValider;
    @FXML
    private AnchorPane root;

    public HomeAdminController() {

        con = ConnectionUtil.conBD();
    }
    Connection con = null;
    private double xOffset=0;
    private double yOffset=0;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        NombreProjet();
        // NombreBénificiare();
        NombreFinancé();
        nombre = (NombreFinancé() / NombreProjet());
        MoveScene();

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


    //      Récuperer le nombre des projets dans la base de données
    public float NombreProjet() {

        String nmbr1 = "SELECT COUNT(*) FROM projet";
        PreparedStatement preparedStatement;
        ResultSet rt;
        try {
            preparedStatement = con.prepareStatement(nmbr1);
            rt = preparedStatement.executeQuery();
            if (rt.next()) {

                return rt.getFloat(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    //      Récuperer le nombre des projets financés dans la base de données

    public float NombreFinancé() {

        String nmbr = "SELECT  COUNT(idProjet) FROM projet where SommeRécolté>=Budget";
        PreparedStatement preparedStatement2;
        ResultSet rt2;
        try {
            preparedStatement2 = con.prepareStatement(nmbr);
            rt2 = preparedStatement2.executeQuery();
            if (rt2.next()) {

                return rt2.getFloat(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Node node = (Node) event.getSource();
        Scene scene;
        Stage stage = (Stage) node.getScene().getWindow();
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/admins/TableLanceur.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
        }

    }
// se deriger vers le tableau qui contient les infos des financeurs
    @FXML
    private void ExplorerFinanceur(ActionEvent event) {
        Node node = (Node) event.getSource();
        Scene scene;
        Stage stage = (Stage) node.getScene().getWindow();
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/admins/TableFinanceur.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
        }

    }
// se deriger vers le tableau qui contient les infos des projets
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

    @FXML
    private void close(MouseEvent event) {
        Alert confirmer = new Alert(Alert.AlertType.CONFIRMATION);
        confirmer.setTitle("Confirmation");
        confirmer.setHeaderText(null);
        confirmer.setContentText("Voulez vous vraiment se déconnecter?");

        // Suppression des btn par défaur et création de nouveau btn de confirmation
        confirmer.getButtonTypes().removeAll(ButtonType.CANCEL, ButtonType.OK);
        ButtonType btnOui = new ButtonType("Oui");
        ButtonType btnNon = new ButtonType("Non");
        confirmer.getButtonTypes().addAll(btnOui, btnNon);
        Optional<ButtonType> résultat = confirmer.showAndWait();
        if (résultat.get() == btnOui) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            
            Scene scene;
            try {
                scene = new Scene(FXMLLoader.load(getClass().getResource("LoginDocument.fxml")));
                stage.setScene(scene);
                
                stage.show();
                

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    @FXML
    private void TableProjets(ActionEvent event) {

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/admins/TableProjets_Admin.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void ViewDashboard(ActionEvent event) {
         FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/admins/Dashboard.fxml"));
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

    @FXML
    private void Valider(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/admins/ValidationProjets.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root1));
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
