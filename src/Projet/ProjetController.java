package Projet;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import financementparticipatif.LoginDocumentController;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import utils.ConnectionUtil;

public class ProjetController implements Initializable {

    @FXML
    private Pane paneLeft;
    // rassemble les radiobutton dans un seul button permettre la séléction d'un seul button
    @FXML
    private ToggleGroup Contrepartie;
    @FXML
    private JFXTextField NomProjet, Budget;
    @FXML
    private JFXComboBox<String> CatégorieProjet, TypeFinancement, Statut;
    @FXML
    private JFXDatePicker DateCréation;
    @FXML
    private JFXTextArea DescriptionProjet;
    @FXML
    private JFXRadioButton ContrepartieConcret, ContrepartieSymb, PasContrepartie;
    @FXML
    private JFXCheckBox PaiementCarte, PaiementEspece, PaiementChéque;
    @FXML
    private Button EnregistrerProjet;
    @FXML
    private JFXButton ImageProjet;
    private FileInputStream fileInp;
    private File file;
    private Image image;
    private double xOffset = 0;
    private double yOffset = 0;
    private String ContrepartieLabel;

    ObservableList<String> listCheck = FXCollections.observableArrayList();

    @FXML
    private ImageView ImageView;
    @FXML
    private FontAwesomeIconView close;
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Statut.getItems().addAll("Entreprise", "Association", "Personnel");
        TypeFinancement.getItems().addAll("Don pur", "Don Avec récompense", "Prêt avec intérêt", "Prêt sans intérêt");
        CatégorieProjet.getItems().addAll("Solidaire", "Educatif", "Culturel", "Sportif", "Santé", "Animaux", "Art");
        //Gestion des RadioButton
        ContrepartieConcret.setOnAction(e -> {
            ContrepartieLabel = ContrepartieConcret.getText();
        });
        ContrepartieSymb.setOnAction(e -> {
            ContrepartieLabel = ContrepartieSymb.getText();
        });
        PasContrepartie.setOnAction(e -> {
            ContrepartieLabel = PasContrepartie.getText();
        });
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
        MoveScene();
    }

    public ProjetController() {
        con = ConnectionUtil.conBD();
    }

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
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
// Ajoue d'image à la base donnée

    @FXML
    public void AjouterImage(ActionEvent event) {
        FileChooser filechooser = new FileChooser();
        filechooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.jpg", "*.png", "*.bitmap", "*.gif"));
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        file = filechooser.showOpenDialog(stage);

        if (file != null) {
            image = new Image(file.toURI().toString(), 169, 98, true, true);
            //Pour s'assurer
            System.out.println(file.toURI().toString());
            ImageView.setImage(image);
            ImageView.setFitWidth(169);
            ImageView.setFitHeight(98);

        }
    }
    
    // Ajout du projet à la base donnée   

    @FXML
    public void AjouterProjet(ActionEvent event) throws SQLException {
        String emailUtil = LoginDocumentController.emailRecup;
        String motdepasseUtil = LoginDocumentController.motDepasseRecup;
        String type = "SELECT * FROM utilisateur where email='" + emailUtil + "' and motdepasse= '" + motdepasseUtil + "'";
        Statement prepared;
        ResultSet rt;

        prepared = con.createStatement();

        rt = prepared.executeQuery(type);
        while (rt.next()) {
            String sql = "INSERT INTO projet(NomProjet,Catégorie,Statut,DateCréation,Description,Budget,TypeDeFinancement,Contrepartie,MoyenDePaiement,Image,CinUtilisateur) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, Character.toUpperCase(NomProjet.getText().charAt(0)) + NomProjet.getText().substring(1));
                preparedStatement.setString(2, CatégorieProjet.getValue());
                preparedStatement.setString(3, Statut.getValue());
                preparedStatement.setString(4, DateCréation.getValue().toString());
                preparedStatement.setString(5, DescriptionProjet.getText());
                preparedStatement.setString(6, Budget.getText());
                preparedStatement.setString(7, TypeFinancement.getValue());
                preparedStatement.setString(8, ContrepartieLabel);
                preparedStatement.setString(9, listCheck.toString());
                fileInp = new FileInputStream(file);
                preparedStatement.setBinaryStream(10, (InputStream) fileInp, (int) file.length());
                preparedStatement.setString(11, rt.getString("cin"));
                preparedStatement.execute();

                System.out.println("Votre projet a bien été enregistré");
                JOptionPane.showMessageDialog(null, "Votre projet a bien été lancer.\n Veuillez attendre la validation de l'administration!!");
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                Scene scene;
                try {
                    scene = new Scene(FXMLLoader.load(getClass().getResource("/financementparticipatif/Home.fxml")));
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }

            } catch (SQLException ex) {
                System.err.println(ex.getMessage());

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

        }
    }

    @FXML

    private void close(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Scene scene;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/financementparticipatif/Home.fxml")));
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
