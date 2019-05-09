package com.gnusl.actine.enums;

public enum FragmentTags {

    LoginFragment(0),
    RegisterFragment(1),
    GuestFragment(2),
    HomeFragment(3),
    SearchFragment(4),
    ComingSoonFragment(5),
    DownloadsFragment(6),
    MoreFragment(7);


    int type;

    FragmentTags(int i) {
        this.type = i;
    }

    public int getType() {
        return type;
    }
}
