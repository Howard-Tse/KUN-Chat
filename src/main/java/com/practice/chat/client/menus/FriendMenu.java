package com.practice.chat.client.menus;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class FriendMenu extends ContextMenu {
    private static FriendMenu instance = null;

    private FriendMenu() {
        MenuItem openChatDialog = new MenuItem("开始聊天");
        MenuItem showFriendProfile = new MenuItem("查看好友资料");
        MenuItem showFriendCircle = new MenuItem("查看朋友圈");
        MenuItem deleteFriend = new MenuItem("删除好友");

        getItems().addAll(openChatDialog, showFriendProfile, showFriendCircle, deleteFriend);
    }

    public static FriendMenu getInstance() {
        if (instance == null)
            instance = new FriendMenu();
        return instance;
    }
}
