package financementparticipatif;

import com.jfoenix.controls.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.*;
import javax.swing.JOptionPane;
import utils.ConnectionUtil;

public class HomeController implements Initializable {

    @FXML
    private Pane paneLeft;
    @FXML
    private  JFXComboBox<String> btnCat;
    public static String Caté;
    @FXML
    private JFXTextField btnRechercher, text1, text2, text3, text4;
    @FXML
    private JFXButton btnExplorer, btnDéconnecter,btnLancer,btnExplorer1, btnExplorer3, btnExplorer2, btnExplorer4;
    @FXML
    private Text labelprojet, labelclient;
    @FXML
    private ImageView image1, image2, image3, image4;
    @FXML
    private JFXTextArea desc1, desc2, desc3, desc4;

    public static int idRec;
    
    public static float nombre;
    @FXML
    private ProgressIndicator ProgressIndicator;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane root;
  

    public HomeController() {

        con = ConnectionUtil.conBD();
    }
    Connection con = null;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        NombreProjet();
        NombreBénificiare();
        GetInfoProjet1();
        GetInfoProjet2();
        GetInfoProjet3();
        GetInfoProjet4();
        btnCat.getItems().addAll("Solidaire", "Educatif", "Culturel", "Sportif", "Santé","Animaux","Art");
        NombreFinancé();
        nombre=(NombreFinancé()/NombreProjet());
        ProgressIndicator.setProgress(nombre);
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
                labelprojet.setText(rt.getString(1));
                return rt.getFloat(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
     //      Récuperer le nombre des projets financés dans la base de données
    public  float NombreFinancé() {

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

    //      Récuperer le nombre des bénéficiaires dans la base de données
    public void NombreBénificiare() {

       String nmbr2 = "SELECT  COUNT(DISTINCT(CinUtilisateur)) FROM projet where etat='Validé'";
        PreparedStatement preparedStatement2;
        ResultSet rt2;
        try {
            preparedStatement2 = con.prepareStatement(nmbr2);
            rt2 = preparedStatement2.executeQuery();
            if (rt2.next()) {
                labelclient.setText(rt2.getString(1));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public JFXComboBox<?> getBtnCat() {
        return btnCat;
    }
//Récuperer info projet 1

    public int GetInfoProjet1() {

        String test = "SELECT idProjet,SommeRécolté,Budget,NomProjet,Catégorie,DateCréation,Image FROM projet WHERE DateCréation>= ALL(SELECT"
                + " DateCréation From projet where etat='Validé')";
        PreparedStatement prepared1;
        ResultSet rst;
        try {
            prepared1 = con.prepareStatement(test);
            rst = prepared1.executeQuery();

            if (rst.next()) {

                text1.setText(rst.getString("SommeRécolté") + "/" + rst.getString("Budget"));
                desc1.setText("       " + rst.getString("NomProjet").toUpperCase() + "\n\n Catégorie: " + rst.getString("Catégorie")
                        + "\n\n Créé le: " + rst.getString("DateCréation"));
                InputStream is = rst.getBinaryStream("Image");
                Image imagetest = new Image(is, 160, 131, false, true);
                image1.setImage(imagetest);
                image1.setFitWidth(160);
                image1.setFitHeight(131);
                return rst.getInt("idProjet");

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    // Récuperer info projet 2

    public int GetInfoProjet2() {
        String test2 = "SELECT idProjet,SommeRécolté,Budget,NomProjet,Catégorie,DateCréation,Image FROM projet WHERE idProjet =1";
        PreparedStatement prepared2;
        ResultSet rst2;
        try {
            prepared2 = con.prepareStatement(test2);
            rst2 = prepared2.executeQuery();

            if (rst2.next()) {

                text2.setText(rst2.getString("SommeRécolté") + "/" + rst2.getString("Budget"));
                desc2.setText("       " + rst2.getString("NomProjet").toUpperCase() + "\n\n Catégorie: " + rst2.getString("Catégorie")
                        + "\n\n Créé le: " + rst2.getString("DateCréation"));
                InputStream is = rst2.getBinaryStream("Image");
                Image imagetest = new Image(is, 160, 131, false, true);
                image2.setImage(imagetest);
                image2.setFitWidth(160);
                image2.setFitHeight(131);
                return rst2.getInt("idProjet");

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    // Récuperer info projet 3

    public int GetInfoProjet3() {
        String test3 = "SELECT idProjet,SommeRécolté,Budget,NomProjet,Catégorie,DateCréation,Image FROM projet WHERE idProjet =2";
        PreparedStatement prepared3;
        ResultSet rst3;
        try {
            prepared3 = con.prepareStatement(test3);
            rst3 = prepared3.executeQuery();

            if (rst3.next()) {
                text3.setText(rst3.getString("SommeRécolté") + "/" + rst3.getString("Budget"));
                desc3.setText("       " + rst3.getString("NomProjet").toUpperCase() + "\n\n Catégorie: " + rst3.getString("Catégorie")
                        + "\n\n Créé le: " + rst3.getString("DateCréation"));
                InputStream is = rst3.getBinaryStream("Image");
                Image imagetest = new Image(is, 160, 131, false, true);
                image3.setImage(imagetest);
                image3.setFitWidth(160);
                image3.setFitHeight(131);
                return rst3.getInt("idProjet");

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    // Récuperer info projet 2

    public int GetInfoProjet4() {
        String test4 = "SELECT idProjet,SommeRécolté,Budget,NomProjet,Catégorie,DateCréation,Image FROM projet WHERE idProjet =3";
        PreparedStatement prepared4;
        ResultSet rst4;
        try {
            prepared4 = con.prepareStatement(test4);
            rst4 = prepared4.executeQuery();

            if (rst4.next()) {

                text4.setText(rst4.getString("SommeRécolté") + "/" + rst4.getString("Budget"));
                desc4.setText("       " + rst4.getString("NomProjet").toUpperCase() + "\n\n Catégorie: " + rst4.getString("Catégorie")
                        + "\n\n Créé le: " + rst4.getString("DateCréation"));
                InputStream is = rst4.getBinaryStream("Image");
                Image imagetest = new Image(is, 160, 131, false, true);
                image4.setImage(imagetest);
                image4.setFitWidth(160);
                image4.setFitHeight(131);
                return rst4.getInt("idProjet");

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
            scene = new Scene(FXMLLoader.load(getClass().getResource("/Projet/Projet.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
        }

    }

    @FXML
    public void ExplorerProjet(ActionEvent event) {
        if (event.getSource() == btnExplorer1) {
            idRec = GetInfoProjet1();
        }
        if (event.getSource() == btnExplorer2) {
            idRec = GetInfoProjet2();
        }
        if (event.getSource() == btnExplorer3) {
            idRec = GetInfoProjet3();
        }
        if (event.getSource() == btnExplorer4) {
            idRec = GetInfoProjet4();
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Projet/VisualisationProjet.fxml"));
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
//rechercher projet par nom
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
     // se dériger vers la table projet

    @FXML
    private void TableProjets(ActionEvent event) {
        
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/Projet/TableProjets.fxml")));
            stage.setScene(scene);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    // se dériger vers la table projet par catégorie
    @FXML
    private void TableProjetsCatégorie(ActionEvent event) {
        
                Caté =  btnCat.getSelectionModel().getSelectedItem();
        
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/Projet/TableProjets_Catégorie.fxml")));
            stage.setScene(scene);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }


   
    
    
}
