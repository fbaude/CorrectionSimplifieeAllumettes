package fb.correctionsimplifieeallumettes.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import fb.correctionsimplifieeallumettes.R;

public class Allumettes extends View {
    Drawable allumette;
    int padding=30;
    int largall=30;
    int hautall=200;
    int nombreTotalAllumettes = 21;
    int nombreAllumettesVisibles = 21; // pour tester
    int nbSelectionnées = 0;
    Paint pPlein,pTiret;
    public Allumettes(Context context ,AttributeSet attrs) { // dessiner toutes les allumettes
        super(context,attrs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            allumette = context.getDrawable(R.drawable.allumette);
        } else {
            allumette = context.getResources().getDrawable(R.drawable.allumette);
        }
        pPlein=new Paint();
        pPlein.setColor(Color.rgb(0, 128, 0));
        pPlein.setStrokeWidth(4);
        pPlein.setAntiAlias(true);
        pPlein.setStyle(Paint.Style.STROKE);
        pPlein.setTextSize(18);
        nbSelectionnées=0; //  ou 1, juste pour tester l'affichage
        DashPathEffect effet = new DashPathEffect(new float[]{10, 25}, 0);
        pTiret = new Paint(pPlein);
        pTiret.setPathEffect(effet);
        pTiret.setStrokeWidth(2);
        pTiret.setColor(Color.GRAY);
    }
    @Override
    protected void onSizeChanged (int w, int h,  int oldw,  int oldh) {
        calculDimensionAllumette();
    }
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String msg="Bienvenue dans le jeu des Allumettes";
        canvas.drawText(msg,(float)padding,(float)padding,pPlein);
        Rect dim = new Rect ();
        pPlein.getTextBounds(msg, 0, msg.length(),dim);
        float x = padding;
        float y = padding;
        float epaisseur = padding/2;// pPlein.getStrokeWidth() / 2;
        //Q0: dessiner juste une allumette
        allumette.setBounds(padding*2,padding*2,padding*2+largall,padding*2+hautall);
        allumette.draw(canvas);
        // Q1  A FINIR : Dessiner toutes les allumettes, en distinguant celles déjà retirées,
        // celles sélectionnées, et celles non sélectionnées
        // On selectionne et prend les allumettes en partant de la fin


        //Ce rectangle qui délimite la zone de dessin des allumettes a une hauteur déjà définie par défaut
        // dans le xml: getHeight renvoie ne effet la hauteur donnée "en dur" à la view Allumette
        canvas.drawRect(0, 0, getWidth() - 1, getHeight() - 1, pPlein);
        Log.d("ALLUMETTES DEBUG", "dessin du rectangle  de largeur "+ (getWidth()));

    }
    @Override
    protected  void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        // voir la docu. de onMeasure
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
        Log.d("affiche width ", "avant calcul dimension allumette suggérée " + getWidth());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("affiche width ", "avant calcul dimension allumette suggérée et apres le superonmeasure " + getWidth());
        calculDimensionAllumette();
    }
    protected void calculDimensionAllumette() {
        Log.d("affiche width écran", "Largeur écran est:" + getWidth());
        largall=(getWidth()-2*padding)/nombreTotalAllumettes;
        if (largall<=0)largall=30;
        hautall = (getHeight() - padding * 3) / 2;
        // Q1 A FINIR: ici le calcul de largall est hautall est à reprendre selon l'énoncé Q1
        // Deux lignes, et par ligne, alterner l'espace par allumette et un espace entre allumettes
    }

    public int obtenirNombreAllumettesSélectionnées() {
        return nbSelectionnées;
    }
    public void sélectionnerAllumettes(int nb) {
        if (nb >= 0) this.nbSelectionnées = nb;//ou mieux, Math.min(nb, nombreAllumettesVisibles);
    }
    public boolean retirerAllumettes(int nb) {
        boolean result = false;
        if ((nb >= 0) && (nombreAllumettesVisibles > 0)) {
            nombreAllumettesVisibles = nombreAllumettesVisibles - nb;
            if (nombreAllumettesVisibles < 0) nombreAllumettesVisibles = 0;
        }
        return result;
    }
}

