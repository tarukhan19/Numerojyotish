package com.numerojyotish.Interface;

import com.numerojyotish.Model.UserListDTO;

public interface UserListInterface {
    void onUserSelected(UserListDTO detailDTO);

    void filterProduct(String query) ;
}
