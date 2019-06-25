package com.presldn.pouch.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.presldn.pouch.R;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        PaperOnboardingPage scr1 = new PaperOnboardingPage("Goals",
                "Create goals (pouches)",
                Color.parseColor("#678FB4"), R.drawable.flag_aed, R.drawable.ic_chevron_right_black_24dp);
        PaperOnboardingPage scr2 = new PaperOnboardingPage("Banks",
                "We carefully verify all banks before add them into the app",
                Color.parseColor("#65B0B4"), R.drawable.flag_bgn, R.drawable.ic_chevron_right_black_24dp);
        PaperOnboardingPage scr3 = new PaperOnboardingPage("Stores",
                "All local stores are categorized for your convenience",
                Color.parseColor("#9B90BC"), R.drawable.flag_bnd, R.drawable.ic_check_white_24dp);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);

        PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(elements);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, onBoardingFragment);
        fragmentTransaction.commit();

        onBoardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                //got to main activity
                final Intent i = new Intent( TutorialActivity.this, MainActivity.class);

                startActivity(i);
                finish();
            }
        });
    }
}
