package com.sugarbeats.presenter;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.sugarbeats.SugarBeats;
import com.sugarbeats.service.AudioService;
import com.sugarbeats.view.BaseView;
import com.sugarbeats.view.HelpView;
import com.sugarbeats.view.IView;
import com.sugarbeats.view.SettingsView;

/**
 * Created by taphan on 11.04.2018.
 */

public class SettingsPresenter extends BasePresenter {
    SugarBeats game;
    IView view;
    Screen parent;
    private AudioService audioService; //TESTER


    public SettingsPresenter(SugarBeats game, Screen parent) {
        this.game = game;
        this.parent = parent;
        this.view = new SettingsView(game, new ViewController());
    }

    @Override
    public IView getView() {
        return view;
    }

    //TESTER
    public void onMuteSoundClick(boolean muted){
        if(muted){
           // audioService.setSoundVolume(0);
        }
    }

    public class ViewController {

        public void onBack() {
            game.setScreen(parent);

        }
    }
}
