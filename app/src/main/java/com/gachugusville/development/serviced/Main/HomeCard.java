package com.gachugusville.development.serviced.Main;

import com.gachugusville.development.serviced.Common.User;

import java.util.List;

public class HomeCard {

    public HomeCard() {
    }

    private String home_card_title, txt_see_all;
    private List<User> userList;

    public HomeCard(String home_card_title, String txt_see_all, List<User> userList) {
        this.home_card_title = home_card_title;
        this.txt_see_all = txt_see_all;
        this.userList = userList;
    }

    public String getHome_card_title() {
        return home_card_title;
    }

    public String getTxt_see_all() {
        return txt_see_all;
    }

    public List<User> getUserList() {
        return userList;
    }
}
