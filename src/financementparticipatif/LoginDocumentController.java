package financementparticipatif;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import javax.swing.JOptionPane;
import utils.ConnectionUtil;

public class LoginDocumentController implements Initializable {

    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtMotdepasse;
    @FXML
    private Button btnConnecter, btn;
    @FXML
    private RadioButton btnClient;
    @FXML
    private JFXTextField btnErreur;
    @FXML
    private FontAwesomeIconView close;
    @FXML
    private ToggleGroup statuts;
    @FXML
    private RadioButton btnadmin;
    @FXML
    private JFXButton passwordForget;
    private AnchorPane root;
    public static String emailRecup, motDepasseRecup;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnConnecter) {
            if (SeConnecter().equals("SUCCES") && IsClient()) {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                Scene scene;
                try {
                    scene = new Scene(FXMLLoader.load(getClass().getResource("Home.fxml")));
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }

            } else {
                if (SeConnecter().equals("SUCCES") && IsAdmin()) {
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();
                    Scene scene;
                    try {
                        scene = new Scene(FXMLLoader.load(getClass().getResource("HomeAdmin.fxml")));
                        stage.setScene(scene);
                        stage.show();

                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }

                }
            }
        }
        if (event.getSource() == btn) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
            Scene scene;
            try {
                scene = new Scene(FXMLLoader.load(getClass().getResource("InscriptionScene.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }

    }

    public void start(Stage stage) throws Exception {

        stage.initStyle(StageStyle.UNDECORATED);

    }

    public LoginDocumentController() {
        con = ConnectionUtil.conBD();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    //Vérification s'il s'agit d'un client
    public boolean IsClient() {

        String type = "SELECT type FROM utilisateur where email='" + txtEmail.getText() + "'";
        Statement prepared;
        ResultSet rt;
        try {
            prepared = con.createStatement();

            rt = prepared.executeQuery(type);
            while (rt.next()) {
                if ("Client".equals(rt.getString("type"))) {
                    if (btnadmin.isSelected()) {
                        JOptionPane.showMessageDialog(null, "Vous n'êtes pas autorisé à utiliser ce service !Cet espace est réservé aux adminstrateurs.");
                        return false;
                    }

                    return true;
                } else {
                    return false;
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    //Vérification s'il s'agit d'un administrateur
    public boolean IsAdmin() {

        String type = "SELECT type FROM utilisateur where email='" + txtEmail.getText() + "'";
        Statement prepared;
        ResultSet rt;
        try {
            prepared = con.createStatement();

            rt = prepared.executeQuery(type);
            while (rt.next()) {
                if ("Administrateur".equals(rt.getString("type"))) {
                    if (btnClient.isSelected()) {
                        JOptionPane.showMessageDialog(null, "Vous n'êtes pas autorisé à utiliser ce service !Cet espace est réservé aux clients.");
                        return false;
                    }

                    return true;
                } else {
                    return false;
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }
//Connection
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    private String SeConnecter() {
        String email = txtEmail.getText();
        String motdepasse = txtMotdepasse.getText();
        emailRecup = txtEmail.getText();
        motDepasseRecup = txtMotdepasse.getText();

        //query
        String sql = "SELECT * FROM utilisateur WHERE email = ? AND motdepasse = ? ";
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, motdepasse);

            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                btnErreur.setText("Email ou mot de passe invalide!!");
                return "ERREUR";
            } else {
                System.out.println("Connexion réussie");
                return "SUCCES";
            }

        } catch (SQLException ex) {

            return "Exception";
        }

    }

    @FXML
    private void close(MouseEvent event) {
        //Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
        //s.close();
        // Méthode de faouzi
        Alert confirmer = new Alert(Alert.AlertType.CONFIRMATION);
        confirmer.setTitle("Confirmation");
        confirmer.setHeaderText(null);
        confirmer.setContentText("Voulez vous vraiment quitter?");
        // Suppression des btn par défaur et création de nouveau btn de confirmation
        confirmer.getButtonTypes().removeAll(ButtonType.CANCEL, ButtonType.OK);
        ButtonType btnOui = new ButtonType("Oui");
        ButtonType btnNon = new ButtonType("Non");
        confirmer.getButtonTypes().addAll(btnOui, btnNon);
        Optional<ButtonType> résultat = confirmer.showAndWait();
        if (résultat.get() == btnOui) {
            System.exit(0);
        }
    }

    @FXML
    private void recoverPassword(MouseEvent event) {
        Node node = (Node) event.getSource();
        Scene scene;
        Stage stage = (Stage) node.getScene().getWindow();
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("PasswordRecovery.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
