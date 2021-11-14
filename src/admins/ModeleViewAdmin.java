/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admins;

import javafx.scene.image.ImageView;

//Il s'agit de la classe contenant les informations du tableau utilisé dans TableProjects pour l'admin
public class ModeleViewAdmin {

    private String NomProjet, Catégorie, Contrepartie, InfosCréateur, DateCréation,etat;
    private double Budget;
    private ImageView imagView;

    public ModeleViewAdmin(String NomProjet, String Catégorie, String DateCréation, double Budget, String Contrepartie, String InfosCréateur, ImageView imagView,String etat) {
        this.NomProjet = NomProjet;
        this.Catégorie = Catégorie;
        this.Contrepartie = Contrepartie;
        this.InfosCréateur = InfosCréateur;
        this.DateCréation = DateCréation;
        this.Budget = Budget;
        this.imagView = imagView;
        this.etat=etat;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
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
