package com.presldn.pouch.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.github.paolorotolo.appintro.model.SliderPage;

import java.util.List;

public class IntroViewModel extends ViewModel {
    private List<SliderPage> sliderPages;


    public void setSliderPages(List<SliderPage> sliderPages) {
        this.sliderPages = sliderPages;
    }

    public SliderPage getSliderPage(int i) {
        return sliderPages.get(i);
    }
}
