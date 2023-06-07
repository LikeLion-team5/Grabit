package com.ll.grabit.boundedcontext.menu.dto;


import com.ll.grabit.boundedcontext.menu.entity.Menu;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class MenuRegisterDto {
    @NotBlank(message = "메뉴 이름을 입력해주세요.")
    private String menuName;

    @Range(min = 500, max = 1000000, message = "메뉴 가격은 500원 ~ 1,000,000원 입니다.")
    private Integer price;

    public Menu toEntity() {
        return Menu.builder()
                .menuName(menuName)
                .price(price)
                .build();
    }
}
