package fr.caemur.delphinelauncher.ui.pages.login;

import fr.caemur.delphinelauncher.ui.Page;
import fr.caemur.delphinelauncher.ui.PageManager;
import fr.caemur.delphinelauncher.ui.utils.Dialog;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class LoginPage extends Page {
    private LoginForm loginForm;

    public LoginPage(PageManager pageManager) {
        super(pageManager, "login", -1);
    }

    @Override
    public void init() {
        super.init();

        loginForm = new LoginForm(pageManager, this, 300);

        layout.add(loginForm, 0, 0);
    }

    @Override
    public void onShow() {
        pageManager.setTabVisibility(false);
        for (Label tab : pageManager.getTabs()) {
            tab.setOpacity(0);
        }

        loginForm.onShow();
    }

    public void auth() {
        loginForm.auth();
    }

    public void auth(String email, String password) {
        if (!email.equals("") && !password.equals("")) {
            loginForm.disable();

            new Thread(() -> {
                // TODO REMOVE AUTH SKIP
                if (email.equals("sk") || pageManager.getApp().auth(email, password)) {
                    Animation formSlideAnimation = new Transition() {
                        {
                            setCycleDuration(Duration.millis(500));
                            setInterpolator(Interpolator.EASE_IN);
                        }

                        @Override
                        protected void interpolate(double progress) {
                            loginForm.setTranslateY(pageManager.getApp().getStage().getHeight() / 2 * progress);
                            loginForm.setOpacity(Math.pow(1 - progress, 10));
                        }
                    };

                    formSlideAnimation.setOnFinished(event1 -> {
                        if (pageManager.getApp().getOptions().isSaveEmail())
                            pageManager.getApp().getOptions().setEmail(email);

                        pageManager.showPage(pageManager.pageFromId("main"), PageManager.AnimationType.SLIDE_IN_UP);
                    });

                    formSlideAnimation.play();
                } else {
                    Platform.runLater(loginForm::enable);
                }
            }).start();
        } else {
            Dialog.errorDialog("Veuillez entrer une adresse mail et un mot de passe");
        }
    }
}
