
package financementparticipatif;

import com.jfoenix.controls.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import utils.ConnectionUtil;

public class PasswordRecoveryController implements Initializable {

    @FXML
    private JFXTextField emailRecover;
    @FXML
    private JFXPasswordField NewPassword;
    @FXML
    private JFXButton BtnRecoverPassword, btnQuitter;

    public PasswordRecoveryController() {
        con = ConnectionUtil.conBD();
    }

    
    Connection con = null;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
// mise à jour du mot de passe
    @FXML
    private void RecoverPassword(MouseEvent event) {
        String emailR = emailRecover.getText();
        String  Newmotdepasse = NewPassword.getText();
        String sql = "update utilisateur set motdepasse="+ Newmotdepasse+" where email ='"+emailR+"'";
        try {
           Statement stmt=con.createStatement();
           stmt.executeUpdate(sql);
           JOptionPane.showMessageDialog(null,"Mot de passe mis à jour avec succés!\n vous pouvez se connecter avec ce nouveau mot de passe");
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
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
           
        }
    }

    @FXML
    private void Quitter(MouseEvent event) {
       
         System.exit(0);
    }
    
}
