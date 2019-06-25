package com.presldn.pouch.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.presldn.pouch.R;
import com.presldn.pouch.ui.fragments.IntroFragment;
import com.presldn.pouch.viewmodels.IntroViewModel;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntroViewModel mModel = ViewModelProviders.of(this).get(IntroViewModel.class);

        List<SliderPage> sliderPages = new ArrayList<>();

        SliderPage tempPage = new SliderPage();
        tempPage.setTitle("Create goals (pouches)");
        tempPage.setImageDrawable(R.drawable.intro0);
        tempPage.setDescription("Create a goal and enter transactions to reach those goals.");
        sliderPages.add(tempPage);
        tempPage = new SliderPage();
        tempPage.setTitle("Track your goals");
        tempPage.setImageDrawable(R.drawable.intro1);
        tempPage.setDescription("See an overview of the progress towards your goals.");
        sliderPages.add(tempPage);
        tempPage = new SliderPage();
        tempPage.setTitle("Reach your goals");
        tempPage.setImageDrawable(R.drawable.intro2);
        tempPage.setDescription("Reach your goals, cash out, and have fun!");
        sliderPages.add(tempPage);

        mModel.setSliderPages(sliderPages);

        addSlide(IntroFragment.newInstance(0));
        addSlide(IntroFragment.newInstance(1));
        addSlide(IntroFragment.newInstance(2));

        showSkipButton(false);
    }

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        final Intent i = new Intent( IntroActivity.this, MainActivity.class);

        startActivity(i);
        finish();

    }
}
