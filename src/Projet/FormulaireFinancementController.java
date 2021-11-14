package Projet;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import financementparticipatif.HomeController;
import financementparticipatif.LoginDocumentController;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.JOptionPane;
import utils.ConnectionUtil;

public class FormulaireFinancementController implements Initializable {

    @FXML
    private Pane paneLeft;
    @FXML
    private JFXButton APropos;
    @FXML
    private JFXTextField NomDuFinanceur, NumCin, Montant, NumTel, AdresseEmail;
    @FXML
    private JFXComboBox<String> TypeFinancement;
    @FXML
    private JFXDatePicker DateNaissance;
    @FXML
    private JFXComboBox<String> Statut;
    @FXML
    private JFXCheckBox PaiementCarte, PaiementEspece, PaiementChéque;
    @FXML
    private Button btnContribuer;
    @FXML
    private Button btnQuitter;
    @FXML
    private JFXTextArea Adresse;
    ObservableList<String> listCheck = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Statut.getItems().addAll("Employé", "Etudiant", "Entrepreneur", "Professeur", "Ingénieur", "Autre");
        TypeFinancement.getItems().addAll("Don pur", "Don Avec récompense", "Prêt avec intérêt", "Prêt sans intérêt");

        //Gestion des CheckBox
        PaiementCarte.setOnAction(e -> {
            listCheck.add(PaiementCarte.getText());
        });
        PaiementChéque.setOnAction(e -> {
            listCheck.add(PaiementChéque.getText());
        });
        PaiementEspece.setOnAction(e -> {
            listCheck.add(PaiementEspece.getText());
        });

        Validate();
    }

    public FormulaireFinancementController() {
        con = ConnectionUtil.conBD();
    }

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
// permet au finnanceur de contribuer au financement du projet en remplissant le formulaire de financement
    @FXML
    public void ContibuerProjet(ActionEvent event) throws SQLException {
        String emailUtil = LoginDocumentController.emailRecup;
        String motdepasseUtil = LoginDocumentController.motDepasseRecup;
        String type = "SELECT * FROM utilisateur where email='" + emailUtil + "' and motdepasse= '" + motdepasseUtil + "'";
        Statement prepared;
        ResultSet rt;

        prepared = con.createStatement();

        rt = prepared.executeQuery(type);
        while (rt.next()) {
            String sql = "INSERT INTO financement(montant,typeFinancement,modePaiement,CinFinanceur,idProjetFinancé,StatutFinanceur) VALUES(?,?,?,?,?,?)";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, Montant.getText());
                preparedStatement.setString(2, TypeFinancement.getValue());
                preparedStatement.setString(3, listCheck.toString());
                preparedStatement.setString(4, rt.getString("cin"));
                preparedStatement.setInt(5, HomeController.idRec);
                preparedStatement.setString(6, Statut.getValue());
                preparedStatement.execute();
                ajouterSomme();

                System.out.println("Le projet a bien été financé");
                JOptionPane.showMessageDialog(null, "Merci pour votre contribution.");
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                Scene scene;
                try {
                    scene = new Scene(FXMLLoader.load(getClass().getResource("/financementparticipatif/Home.fxml")));
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.show();

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }

            } catch (SQLException ex) {
                System.err.println(ex.getMessage());

            }
        }

    }
// mettre à jour la base de données de façon à ce que le montant devient dans la sommeRécolté 
    public void ajouterSomme() {
        String somme = "SELECT  SommeRécolté FROM projet where idProjet ='" + HomeController.idRec + "'";
        Statement prepared, pr;
        ResultSet rt;
        try {

            pr = con.createStatement();
            rt = pr.executeQuery(somme);
            if (rt.next()) {
                double s = Double.parseDouble(Montant.getText()) + rt.getDouble("SommeRécolté");

                String sql = "update projet set SommeRécolté=" + s + " where idProjet ='" + HomeController.idRec + "'";
                prepared = con.createStatement();
                prepared.executeUpdate(sql);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
// informations sur les createurs de l'application(nous)
    @FXML
    private void About(ActionEvent event) {
        JOptionPane.showMessageDialog(null,
                "Notre programme est développé pour collecter les fonds afin de financer des projets de differents types.\n Développé par:\n ABOUTARA BELRHITI Oumaima\n SADIK Oumaima \n GMI1", "A propos de nous!", JOptionPane.INFORMATION_MESSAGE);

    }
    // Validation de remplissage des chemps de formulaire

    private void Validate() {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Merci de remplir ce champ!!");
        // validator.setAwsomeIcon(new Icon(AwesomeIcon.WARNING, "2em", ";", "error") {});
        Montant.getValidators().add(validator);
        Montant.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                Montant.validate();

            }
        });
        NumCin.getValidators().add(validator);
        NumCin.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                NumCin.validate();

            }
        });
        NumCin.getValidators().add(validator);
        NumCin.focusedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldVal, Boolean newVal) -> {
            if (!newVal) {
                NumCin.validate();

            }
        });

        NumTel.getValidators().add(validator);
        NumTel.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                NumTel.validate();

            }
        });

    }
// Fermerture de la page 
    @FXML
    private void close(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        
    }
}
