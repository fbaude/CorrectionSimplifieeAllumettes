package fb.correctionsimplifieeallumettes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import fb.correctionsimplifieeallumettes.controle.Controleur;
import fb.correctionsimplifieeallumettes.moteur.JeuxDesAllumettes;
import fb.correctionsimplifieeallumettes.moteur.Joueur;
import fb.correctionsimplifieeallumettes.view.Allumettes;

public class MainActivity extends Activity {
    private Allumettes all;
    private JeuxDesAllumettes règles = new JeuxDesAllumettes();
    private Controleur controleur;
    private Joueur[] joueurs=new Joueur[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allumettes);
        Log.d("ALLUMETTES DEBUG", "création");
        all = findViewById(R.id.allumettes);
        all.sélectionnerAllumettes(1); // pour tester la Q2

        TextView msg = findViewById(R.id.message);
        ScrollView scroll = findViewById(R.id.scroll);
        Button demarrer = (Button) findViewById(R.id.demarrer);
        JeuxDesAllumettes regles=new JeuxDesAllumettes();
        controleur=new Controleur(regles,all,msg,scroll);

        joueurs[0]=new Joueur();
        joueurs[0].changerNom("Francoise");
        joueurs[1]=new Joueur();
        joueurs[1].changerNom("Claire");
        regles.ajouterJoueur(joueurs[0]);
        regles.ajouterJoueur(joueurs[1]);
    }
    public void démarrer(View v) {
        Log.d("ALLUMETTES DEBUG", "bouton démarrerPartie");
        controleur.demarrerPartie();
    }

}
