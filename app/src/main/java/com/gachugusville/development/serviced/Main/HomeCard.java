package com.gachugusville.development.serviced.Main;

import com.gachugusville.development.serviced.Common.User;

import java.util.List;

public class HomeCard {

    public HomeCard() {
    }

    private String service_category_name, txt_see_all;
    private List<User> userList;

    public HomeCard(String service_category_name, String txt_see_all, List<User> userList) {
        this.service_category_name = service_category_name;
        this.txt_see_all = txt_see_all;
        this.userList = userList;
    }

    public String getservice_category_name() {
        return service_category_name;
    }

    public String getTxt_see_all() {
        return txt_see_all;
    }

    public List<User> getUserList() {
        return userList;
    }
}
