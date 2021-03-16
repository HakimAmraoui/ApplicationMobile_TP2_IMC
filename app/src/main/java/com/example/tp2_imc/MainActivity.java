package com.example.tp2_imc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button envoyer = null;
    Button reset = null;
    EditText taille = null;
    EditText poids = null;
    CheckBox commentaire = null;
    RadioGroup group = null;
    TextView result = null;
    private static final String TAG = "MyActivity";


//    private final String texteInit = getString(R.string.texteInit);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // On récupère toutes les vues dont on a besoin
        envoyer = (Button) findViewById(R.id.calcul);
        reset = (Button) findViewById(R.id.reset);
        taille = (EditText) findViewById(R.id.taille);
        poids = (EditText) findViewById(R.id.poids);
        commentaire = (CheckBox) findViewById(R.id.commentaire);
        group = (RadioGroup) findViewById(R.id.group);
        result = (TextView) findViewById(R.id.result);

        // On attribue un listener adapté aux vues qui en ont besoin
        envoyer.setOnClickListener(envoyerListener);
        reset.setOnClickListener(resetListener);
        commentaire.setOnClickListener(checkedListener);
        taille.setOnKeyListener(modifListener);
        poids.setOnKeyListener(modifListener);


    }

    // Listener du bouton envoyer
    private View.OnClickListener envoyerListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //  on récupère la taille
            String t = taille.getText().toString();
            // On récupère le poids
            String p = poids.getText().toString();
            float tValue = Float.valueOf(t);

            // Puis on vérifie que la taille est cohérente
            if (tValue <= 0)
                Toast.makeText(MainActivity.this, R.string.tailleNeg, Toast.LENGTH_SHORT).show();
            else {
                float pValue = Float.valueOf(p);
                if (pValue <= 0)
                    Toast.makeText(MainActivity.this, R.string.poidsNeg, Toast.LENGTH_SHORT).show();
                else {
                    // Si l'utilisateur a indiqué que la taille était en centimètres
                    // On vérifie que la Checkbox sélectionnée est la deuxième à l'aide de son identifiant
                    if (group.getCheckedRadioButtonId() == R.id.radio_centimetre)
                        tValue = tValue / 100;
                    float imc = pValue / (tValue * tValue);
                    String resultat = getString(R.string.textResult) + imc + " . ";

                    // On ajoute une interpretation de l'imc
                    if (commentaire.isChecked()) resultat += interpreteIMC(imc);

                    result.setText(resultat);
                }
            }
        }
    };

    // Listener du bouton reset
    private View.OnClickListener resetListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            poids.getText().clear();
            taille.getText().clear();
            result.setText(getString(R.string.texteInit));
        }
    };

    // Listener du bouton commentaire
    private View.OnClickListener checkedListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (((CheckBox) v).isChecked()) {
                result.setText(getString(R.string.texteInit));
            }
        }
    };

    // Le textWatcher, qui permet d’avoir un contrôle sur les éditeurs de texte
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            result.setText(getString(R.string.texteInit));
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    // Ex 1
    private String interpreteIMC(float imc) {
        if (imc < 16.5) {
            return getResources().getString(R.string.famine);
        } else if (16.5 <= imc && imc < 18.5) {
            return getResources().getString(R.string.maigre);
        } else if (18.5 <= imc && imc < 25) {
            return getResources().getString(R.string.corpulenceNormale);
        } else if (25 <= imc && imc < 30) {
            return getResources().getString(R.string.surpoids);
        } else if (30 <= imc && imc < 35) {
            return getResources().getString(R.string.obesiteModeree);
        } else if (35 <= imc && imc < 40) {
            return getResources().getString(R.string.obesiteSevere);
        } else if (40 <= imc) {
            return getResources().getString(R.string.obesiteMorbide);
        }
        return "";

    }

    // Ex 2
    // Se lance à chaque fois qu'on appuie sur une touche en étant sur un EditText
    private View.OnKeyListener modifListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // On remet le texte à sa valeur par défaut
            if (taille.getText().toString().contains(".")) {
                group.check(R.id.radio_metre);
            } else {
                group.check(R.id.radio_centimetre);
            }
            return false;
        }


    };
}
