package fr.caemur.delphinelauncher.ui;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public abstract class Page extends GridPane {
    protected GridPane layout;
    protected PageManager pageManager;
    private final String pageId;
    private final int tabIndex;

    public Page(PageManager pageManager, String pageId, int tabIndex) {
        layout = new GridPane();
        this.pageManager = pageManager;
        this.pageId = pageId;
        this.tabIndex = tabIndex;
    }

    public GridPane getLayout() {
        return layout;
    }

    public void init() {
        GridPane.setHgrow(layout, Priority.ALWAYS);
        GridPane.setVgrow(layout, Priority.ALWAYS);
    }

    public void onShow() {
    }

    public String getPageId() {
        return pageId;
    }

    public int getTabIndex() {
        return tabIndex;
    }
}
