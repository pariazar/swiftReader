
package com.pariazar.swiftReader;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import androidx.fragment.app.Fragment;

public class MainIntroActivity extends AppIntro {

    int bg = Color.parseColor("#2481a1");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        SliderPage first = new SliderPage();
        first.setTitle(getString(R.string.title_intro));
        first.setDescription(getString(R.string.description__intro));
        first.setImageDrawable(R.mipmap.ic_launcher);
        first.setBgColor(bg);
        addSlide(AppIntroFragment.newInstance(first));

        SliderPage second = new SliderPage();
        second.setTitle(getString(R.string.title_open));
        second.setDescription(getString(R.string.description_open));
        second.setImageDrawable(R.drawable.opensource_wide);
        second.setBgColor(bg);
        addSlide(AppIntroFragment.newInstance(second));

        SliderPage third = new SliderPage();
        third.setTitle(getString(R.string.title_permission));
        third.setDescription(getString(R.string.description__permission));
        third.setImageDrawable(R.drawable.patterns_permissions);
        third.setBgColor(bg);
        addSlide(AppIntroFragment.newInstance(third));

        showSkipButton(false);
        askForPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        showStatusBar(false);
        setNavBarColor("#2481a1");
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}