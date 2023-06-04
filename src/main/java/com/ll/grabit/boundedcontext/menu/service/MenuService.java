package com.ll.grabit.boundedcontext.menu.service;


import com.ll.grabit.boundedcontext.menu.dto.MenuUpdateDto;
import com.ll.grabit.boundedcontext.menu.entity.Menu;
import com.ll.grabit.boundedcontext.menu.repository.MenuRepository;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public void updateMenuList(Restaurant findRestaurant, List<MenuUpdateDto> menuUpdateList) {
        //1. 식당의 모든 메뉴 삭제
        List<Menu> menuList = findRestaurant.getMenuList();
        for (Menu menu : menuList) {
            menuRepository.delete(menu);
        }

        //2. 새로 받은 메뉴 리스트를 식당의 메뉴로 등록
        for (MenuUpdateDto menuUpdate : menuUpdateList) {
            if(!menuUpdate.getMenuName().isEmpty()){
                //Dto -> Entity
                Menu menu = menuUpdate.toEntity();
                menu.setRestaurant(findRestaurant);

                //메뉴 저장
                menuRepository.save(menu);
            }
        }
    }
}
