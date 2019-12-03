package com.selectionarts.projectcensio.apicontroller.viewmodel;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class DashboardViewModel {
    private Set<AppViewModel> appViewModelSet;

    public DashboardViewModel()
    {
        this.appViewModelSet = new HashSet<>();
    }
}
