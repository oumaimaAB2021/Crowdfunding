
package admins;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ConnectionUtil;

public class TableFinanceurController implements Initializable {

    @FXML
    private TableView<ModeleViewPersonne> TableView;
    ObservableList<ModeleViewPersonne> oblist = FXCollections.observableArrayList();
    @FXML
    private FontAwesomeIconView close, pleinEcran, MINUS;
     @FXML
    private TableColumn<?, ?> colName,colDate,colCin,colTel,colEmail,colProjet,colAdresse;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ViewFinanceur();
    }

    public TableFinanceurController() {
        con = ConnectionUtil.conBD();

    }
    Connection con = null;
    PreparedStatement preparedStatement = null;
    // affichage des informations des financeurs dans la table pour admin

    public void ViewFinanceur() {
        String cin = "SELECT  Distinct(CinFinanceur) FROM financement ";
        Statement pr, prepared,nombre;
        ResultSet rt, rst,total;
        try {
            pr = con.createStatement();
            prepared = con.createStatement();
             nombre = con.createStatement();
            rt = pr.executeQuery(cin);
            while (rt.next()) {
                String type = "SELECT * FROM utilisateur WHERE cin= '"+rt.getString("CinFinanceur")+"' ";

                rst = prepared.executeQuery(type);
                String nbr = "SELECT count(DISTINCT(idProjetFinancé) ) From financement  WHERE CinFinanceur='"+rt.getString("CinFinanceur")+"'";
                total = nombre.executeQuery(nbr);

                while (rst.next()&& total.next() ) {
                  
                    oblist.add(new ModeleViewPersonne(rst.getString("nom").toUpperCase()+ " "+rst.getString("prénom"), rst.getString("datedenaissance"), rst.getString("cin"),
                            rst.getInt("tel"), rst.getString("email"),total.getInt(1), rst.getString("adresse")));

                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Erreur d'affichage des Financeurs");
        }
        colName.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("DateNaissance"));
        colCin.setCellValueFactory(new PropertyValueFactory<>("Cin"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("Tel"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colProjet.setCellValueFactory(new PropertyValueFactory<>("nmbrProjet"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
        
        TableView.setItems(oblist);
    }

  

    @FXML
    private void close(MouseEvent event) {
         Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        
        Scene scene;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/financementparticipatif/HomeAdmin.fxml")));
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
       
    }

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
