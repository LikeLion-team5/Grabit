package com.ll.grabit.boundedcontext.menu.repository;

import com.ll.grabit.boundedcontext.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
