
package admins;
//Il s'agit de la classe contenant les informations du tableau utilisé dans les Personnes pour l'admin
public class ModeleViewPersonne {

    private String Nom, Cin, Email,  DateNaissance,Adresse;
    private int Tel ,nmbrProjet;

    public ModeleViewPersonne(String Nom, String DateNaissance,String Cin,int Tel, String Email,int nmbrProjet, String Adresse) {
        this.Nom = Nom;
        this.Cin = Cin;
        this.Tel = Tel;
        this.Email = Email;
        this.DateNaissance = DateNaissance;
        this.nmbrProjet = nmbrProjet;
        this.Adresse = Adresse;
     
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public String getCin() {
        return Cin;
    }

    public void setCin(String Cin) {
        this.Cin = Cin;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getDateNaissance() {
        return DateNaissance;
    }

    public void setDateNaissance(String DateNaissance) {
        this.DateNaissance = DateNaissance;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String Adresse) {
        this.Adresse = Adresse;
    }

    public int getTel() {
        return Tel;
    }

    public void setTel(int Tel) {
        this.Tel = Tel;
    }

    public int getNmbrProjet() {
        return nmbrProjet;
    }

    public String getNom() {
        return Nom;
    }

    public void setNmbrProjet(int nmbrProjet) {
        this.nmbrProjet = nmbrProjet;
    }

   

}
