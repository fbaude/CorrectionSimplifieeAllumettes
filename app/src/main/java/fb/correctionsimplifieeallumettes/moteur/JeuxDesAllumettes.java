package fb.correctionsimplifieeallumettes.moteur;

public class JeuxDesAllumettes {
    // les regles du jeu
    Joueur[] joueurs = new Joueur[2];
    protected static final int NB_PAR_DEFAUT = 21;
    private int nbInitial = NB_PAR_DEFAUT;
    private int nbAllumettesRestantes = NB_PAR_DEFAUT;
    int joueurCourant = -1;
    int gagnant = -1;
    void reinitialiser(){ // quand on voudra réinitialiser la partie
        nbAllumettesRestantes = nbInitial;
        joueurCourant = -1;
        gagnant = -1;
    }
    public int obtenirNbTotalAllumettes() {
        return nbInitial;
    }
    public int obtenirNbAllumettesRestantes() {
        return nbAllumettesRestantes;
    }
    public boolean ajouterJoueur(Joueur j) {
        boolean ajouté = !partieComplete();// a t on ou pas déjà les x (==2) joueurs?
        if (ajouté) {
            if (joueurs[0] == null) joueurs[0] = j;
            else joueurs[1] = j;
        }

        return ajouté;
    }
    public boolean démarrer(boolean premierEnPremier) {
        boolean démarrée = (nbAllumettesRestantes == nbInitial);
        if (démarrée) {
            if (premierEnPremier) joueurCourant = 0;
            else joueurCourant = 1;
        }
        return démarrée;
    }
    public Joueur aQuiDeJouer() {
        Joueur résultat = null;
        if ((joueurCourant >= 0) && (joueurCourant < joueurs.length))
            résultat = joueurs[joueurCourant];
        return résultat;
    }
    public boolean partieComplete() {//les deux joueurs sont créés
        return ((joueurs[0] != null) && (joueurs[1] != null));
    }
    public Joueur obtenirGagnant() {
        Joueur résultat = null;
        if ((gagnant >= 0) && (gagnant < joueurs.length)) résultat = joueurs[gagnant];
        return résultat;
    }

    public int jouerUnTour() throws InterruptedException {
        Joueur j = aQuiDeJouer();
        int nbJouée = 0;
        if ((j != null) && (obtenirGagnant() == null)) {
            nbJouée = j.jouer(obtenirNbAllumettesRestantes());
            nbAllumettesRestantes = nbAllumettesRestantes - nbJouée;
            if (nbAllumettesRestantes > 0) {
                joueurCourant = (joueurCourant + 1) % joueurs.length;
            } else {
                gagnant = (joueurCourant + 1) % joueurs.length; // celui qui prend en dernier a perdu
                joueurCourant = -1;
            }
        }
        //Ici Q2 simplifiée: immediatement, une fois joué un seul tour, on décide qu'il y a un gagnang à la partie:
        gagnant=0;// c'est toujours le joueur 0 qui gagne!! A améliorer dans la vraie Q2 !!
        return nbJouée;
    }

}
