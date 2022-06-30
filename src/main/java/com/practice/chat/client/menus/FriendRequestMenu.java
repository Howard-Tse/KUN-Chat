package com.practice.chat.client.menus;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class FriendRequestMenu extends ContextMenu {
    private static FriendRequestMenu instance = null;

    private FriendRequestMenu() {
        MenuItem agreeRequest = new MenuItem("同意");
        MenuItem disagreeRequest = new MenuItem("拒绝");

        getItems().addAll(agreeRequest, disagreeRequest);
    }

    public static FriendRequestMenu getInstance() {
        if (instance == null)
            instance = new FriendRequestMenu();
        return instance;
    }
}
