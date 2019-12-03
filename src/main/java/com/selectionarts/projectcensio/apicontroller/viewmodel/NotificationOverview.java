package com.selectionarts.projectcensio.apicontroller.viewmodel;


import lombok.Data;

@Data
public class NotificationOverview {
    private int newmessages;
    private int numberofapps;

    public NotificationOverview() {
    }

    public NotificationOverview(int newmessages, int numberofapps) {
        this.newmessages = newmessages;
        this.numberofapps = numberofapps;
    }
}
