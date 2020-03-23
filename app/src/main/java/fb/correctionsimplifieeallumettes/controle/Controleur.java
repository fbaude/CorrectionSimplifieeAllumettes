package fb.correctionsimplifieeallumettes.controle;

import android.os.AsyncTask;
import android.text.Editable;
import android.widget.ScrollView;
import android.widget.TextView;

import fb.correctionsimplifieeallumettes.moteur.Joueur;
import fb.correctionsimplifieeallumettes.view.Allumettes;
import fb.correctionsimplifieeallumettes.moteur.JeuxDesAllumettes;
public class Controleur {

    JeuxDesAllumettes regles;
    Tache t; // la tache en background pour dérouler le jeu qui a comme but de
    // simuler le choix de 1,2 ou 3 allumettes par le joueur courant, et donc modifie le nb total
    // restant d'allumettes encore dispo.
    Allumettes vueall;
    TextView texte;
    // pour que les messages de coups joués puissent se voir au fur à mesaure, il faudra
    // adjoindre au texte un listener: chaque fois qu'on ajoutera du texte,
    // il faudra faire défiler le ScrollView defile, et donc implémenter quelques méthodes;
    // on vous conseille d'implémenter un TextWatcher
    ScrollView defile;


    public Controleur(JeuxDesAllumettes regles, Allumettes vue, TextView dialogue, ScrollView scroll) {
        this.regles = regles;
        this.vueall = vue;
        this.texte = dialogue;
        this.defile = scroll;
    }
    public void demarrerPartie(){
        regles.démarrer(true); // suppose tjs que le 1er joueur des 2 commence
        t = new Tache();
        t.execute();
    }

    private class Tache extends AsyncTask<Void, Tache.MessageDeProgression, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                // pour démarrer, on affiche l'état du jeu
                MessageDeResolution messageResolution = new MessageDeResolution(regles.aQuiDeJouer(),
                        -1, regles.obtenirNbAllumettesRestantes());

                publishProgress(messageResolution);
                // Là, il faudra boucler, mais ici on ne joue qu'un seul coup
                   // sélection
                    MessageDeCoup messageCoup = new MessageDeCoup(regles.aQuiDeJouer(),
                            regles.jouerUnTour());
                    publishProgress(messageCoup);

                    // temps d'attente pour laisser le temps (à l'utilisateur) de voir préparation du coup !
                    // Utile si c'est un joueur "artificiel" et pas un humain
                    messageCoup.getJoueur().temporiser();

                    // application du coup, et effet de bord: changement de joueur (cf aQuiDeJouer)
                    MessageDeResolution messageResolution2 = new MessageDeResolution(regles.aQuiDeJouer(),
                            messageCoup.getNbJouées(), regles.obtenirNbAllumettesRestantes());
                    publishProgress(messageResolution2);

                    // temps d'attente pour laisser le temps (à l'utilisateur) de voir la résolution du coup joué
                    //  Utile si c'est un joueur "artificiel" (prochain tour, s'il y a)
                    messageCoup.getJoueur().temporiser();
                // là, fin de la boucle à rajouter
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // fin de partie
            String résultat = "Partie finie ou interrompue";
            if (regles.obtenirGagnant() != null)
                résultat = regles.obtenirGagnant().toString() + " a gagné !";

            return résultat;
        }


        @Override
        protected void onProgressUpdate(MessageDeProgression... msg) {
            msg[0].miseAJour();
        }

        @Override
        protected void onPostExecute(String s) {
            texte.append(s);
            //vueall.invalidate();
        }

        private abstract class MessageDeProgression {
            Joueur j;
            int nbJouées;
            int nbRestantes;

            public Joueur getJoueur() {
                return j;
            }

            public void setJoueur(Joueur j) {
                this.j = j;
            }

            public int getNbJouées() {
                return nbJouées;
            }

            public void setNbJouées(int nb) {
                this.nbJouées = nb;
            }

            public int getNbRestantes() {
                return nbRestantes;
            }

            public void setNbRestantes(int nb) {
                this.nbRestantes = nb;
            }

            abstract void miseAJour(); // de la view Allumettes et de la zone textuelle
        }

        private class MessageDeCoup extends MessageDeProgression {

            protected MessageDeCoup(Joueur j, int nbJouées) {
                setJoueur(j);
                setNbJouées(nbJouées);
            }

            public String toString() {
                return j.toString() + " a joué : " + getNbJouées() + "\n";
            }

            @Override
            void miseAJour() {
                vueall.sélectionnerAllumettes(getNbJouées());
                vueall.invalidate();// force la réexecution du onDraw donc des allumettes

                texte.append(this.toString());
                texte.invalidate(); //Est-ce utile ici de forcer la mise à jour de textView?

            }
        }

        private class MessageDeResolution extends MessageDeProgression {

            protected MessageDeResolution(Joueur j, int nbJouées, int nbRestantes) {
                setJoueur(j);
                setNbJouées(nbJouées);
                setNbRestantes(nbRestantes);
            }

            public String toString() {
                String résultat = "il reste " + getNbRestantes() + " allumette(s). ";
                if (j == null) {
                    résultat += "C'est fini ! \n";
                } else {
                    résultat += "C'est à " + j + " de jouer\n";
                }
                return résultat;
            }

            @Override
            void miseAJour() {
                vueall.retirerAllumettes(getNbJouées());
                vueall.sélectionnerAllumettes(0);
                vueall.invalidate(); // force la réexecution du onDraw

                texte.append(this.toString());
                texte.invalidate(); //Est-ce utile ici de forcer la mise à jour de textView?

            }
        }

    }

}