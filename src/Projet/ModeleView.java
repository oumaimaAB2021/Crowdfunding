
package Projet;


import javafx.scene.image.ImageView;

//Il s'agit de la classe contenant les informations du tableau utilisé dans TableProjects et TableProjects_Catégorie
public class ModeleView {

    private String NomProjet, Catégorie, Contrepartie, InfosCréateur, DateCréation;
    private double Budget;
    private ImageView imagView;

    public ModeleView(String NomProjet, String Catégorie, String DateCréation, double Budget, String Contrepartie, String InfosCréateur, ImageView imagView) {
        this.NomProjet = NomProjet;
        this.Catégorie = Catégorie;
        this.Contrepartie = Contrepartie;
        this.InfosCréateur = InfosCréateur;
        this.DateCréation = DateCréation;
        this.Budget = Budget;
        this.imagView = imagView;
    }

    public String getContrepartie() {
        return Contrepartie;
    }

    public void setContrepartie(String Contrepartie) {
        this.Contrepartie = Contrepartie;
    }

    public String getDateCréation() {
        return DateCréation;
    }

    public void setDateCréation(String DateCréation) {
        this.DateCréation = DateCréation;
    }

    public double getBudget() {
        return Budget;
    }

    public void setBudget(double Budget) {
        this.Budget = Budget;
    }

    public String getNomProjet() {
        return NomProjet;
    }

    public void setNomProjet(String NomProjet) {
        this.NomProjet = NomProjet;
    }

    public String getCatégorie() {
        return Catégorie;
    }

    public void setCatégorie(String Catégorie) {
        this.Catégorie = Catégorie;
    }

    public String getInfosCréateur() {
        return InfosCréateur;
    }

    public void setInfosCréateur(String InfosCréateur) {
        this.InfosCréateur = InfosCréateur;
    }

    public ImageView getImagView() {
        return imagView;
    }

    public void setImagView(ImageView imagView) {
        this.imagView = imagView;
    }

   

}
