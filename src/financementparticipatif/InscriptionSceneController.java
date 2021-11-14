package financementparticipatif;

import com.jfoenix.controls.*;
import javafx.scene.control.*;
import com.jfoenix.validation.NumberValidator;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.*;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import utils.ConnectionUtil;

public class InscriptionSceneController implements Initializable {

    @FXML
    private JFXTextField txtTel, ErrorEmail, ErrorConfi, ErrorDate;
    @FXML
    private TextField txtCin, txtEmailInsc, txtPrenom, txtNom;
    @FXML
    private PasswordField txtMotdepasseInsc, txtMotdepasseConf;
    @FXML
    private TextArea txtAdresse;
    @FXML
    private Button btnInscription;
    @FXML
    private DatePicker btnDate;
    @FXML
    private ComboBox btnType;
    @FXML
    private JFXButton authentification;

    /**
     * Initializes the controller class.
     */
    public InscriptionSceneController() {
        con = ConnectionUtil.conBD();
    }
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnType.getItems().addAll("Client", "Administrateur");
        // seleument les numéros
        NumberValidator numvalidator = new NumberValidator();
        txtTel.getValidators().add(numvalidator);
        numvalidator.setMessage("le numéro de téléphone est invalide!!");
        txtTel.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtTel.validate();
            }
        });

    }

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if (event.getSource() == authentification) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
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
// valider la date de naissance
    @FXML
    private boolean DateNaissance(ActionEvent event) {
        LocalDate today = LocalDate.now();
        LocalDate bdate = btnDate.getValue();
        ErrorDate.setVisible(true);
        if (bdate == null || bdate.isAfter(today)) {

            ErrorDate.setText(" La date est incorrecte!!");
            return false;

        } else {
            ErrorDate.setVisible(false);
            return true;
        }
    }
//  valider la confirmation de mot de passe
    private boolean Confirmation() {

        if (!txtMotdepasseConf.getText().equals(txtMotdepasseInsc.getText())) {
            ErrorConfi.setText("Le mot de passe est invalide");
            return false;
        } else {
            ErrorConfi.setVisible(true);
            return true;
        }

    }
// valider la forme d'email
    @FXML
    private void ValidateEmail(KeyEvent ev) {
        String e = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern em = Pattern.compile(e, Pattern.CASE_INSENSITIVE);
        Matcher mat = em.matcher(txtEmailInsc.getText());
        ErrorEmail.setVisible(true);
        if (!mat.find()) {
            ErrorEmail.setText("L'adresse email est invalide");

        } else {
            ErrorEmail.setVisible(false);
        }

    }
// valider le remplissage de tous les champs de formulaire
    public boolean ValidateFields() {

        if (txtNom.getText().isEmpty() | txtPrenom.getText().isEmpty() | txtCin.getText().isEmpty()
                | btnDate.getValue().toString() == null | txtEmailInsc.getText().isEmpty() | txtEmailInsc.getText().isEmpty()
                | txtMotdepasseInsc.getText().isEmpty() | txtTel.getText().isEmpty() | txtAdresse.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Un ou plusieurs champs sont vide. Remplissez tous les champs!");
            return false;
        }
        return true;
    }

    // Vérification de l'existence du client
    @FXML
    public boolean ClientExist() {

        String type = "SELECT * FROM utilisateur ";
        Statement prepared;
        ResultSet rt;
        try {
            prepared = con.createStatement();
            rt = prepared.executeQuery(type);
            while (rt.next()) {
                if (txtCin.getText().toUpperCase().equals(rt.getString("cin"))) {
                    JOptionPane.showMessageDialog(null, "Vous êtes déjà inscrit. Connectez-vous SVP !");

                    return false;
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return true;
    }

    @FXML
    public void AjouterClient(ActionEvent event) {
        String sql = "INSERT INTO utilisateur(nom,prénom,cin,datedenaissance,email,motdepasse,tel,type,adresse) VALUES(?,?,?,?,?,?,?,?,?)";
        if (ValidateFields() && Confirmation() && DateNaissance(event) && ClientExist()) {
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, txtNom.getText().toUpperCase());
                preparedStatement.setString(2, Character.toUpperCase(txtPrenom.getText().charAt(0)) + txtPrenom.getText().substring(1));
                preparedStatement.setString(3, txtCin.getText().toUpperCase());
                preparedStatement.setString(4, btnDate.getValue().toString());
                preparedStatement.setString(5, txtEmailInsc.getText());
                preparedStatement.setString(6, txtMotdepasseInsc.getText());
                preparedStatement.setString(7, txtTel.getText());
                preparedStatement.setString(8, btnType.getValue().toString());
                preparedStatement.setString(9, txtAdresse.getText());
                preparedStatement.execute();

                System.out.println("Bien enregistré");
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                Scene scene;
                try {
                    scene = new Scene(FXMLLoader.load(getClass().getResource("LoginDocument.fxml")));
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }

            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }

    }

}
