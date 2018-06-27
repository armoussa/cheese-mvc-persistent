package org.launchcode.controllers;


import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value= "menu")
public class MenuController {
    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title","Menus");

        return "menu/index";
    }



    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model) {
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());

        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddMenuForm(@ModelAttribute @Valid Menu newMenu, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title","Add Menu");
            return "menu/add";
        }
            menuDao.save(newMenu);
            return "redirect:view/" + newMenu.getId();
    }

    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(@PathVariable int menuId, Model model) {

        Menu menu = menuDao.findOne(menuId);
        model.addAttribute("menu", menu);
        model.addAttribute("title", menu.getName());

        return "menu/view";
    }

    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(@PathVariable int menuId, Model model) {

        Menu menu = menuDao.findOne(menuId);

        AddMenuItemForm form = new AddMenuItemForm(menu,cheeseDao.findAll());

        model.addAttribute("title", "Add item to menu: " + menu.getName());
        model.addAttribute("form", form);
        return "menu/add-item";



    }

    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String addItem(@ModelAttribute @Valid AddMenuItemForm form, Errors errors, Model model) {
        if (errors.hasErrors()){
            model.addAttribute("title","Add Item");

            return "menu/add-item/";
        }

        Menu menu = menuDao.findOne(form.getMenuId());

        Cheese cheese = cheeseDao.findOne(form.getCheeseId());

        menu.addItem(cheese);
        menuDao.save(menu);

        return "redirect:/menu/view/"+ menu.getId();


    }


}
