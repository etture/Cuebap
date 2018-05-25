package com.jinoolee.cuebap.Data;

import com.jinoolee.cuebap.R;
import com.jinoolee.cuebap.RecyclerViewItems.BuildingItem;
import com.jinoolee.cuebap.RecyclerViewItems.CategoryItem;
import com.jinoolee.cuebap.RecyclerViewItems.FoodItem;
import com.jinoolee.cuebap.RecyclerViewItems.RestaurantItem;

import java.util.ArrayList;
import java.util.List;

public class BuildingsAndRestaurants {

    public static final int NEW_MILLENNIUM_HALL = 0;
    public static final int STUDENTS_BUILDING = 1;
    public static final int THE_COMMONS = 2;
    public static final int DAEWOO_HALL = 3;
    public static final int SAMSUNG_HALL = 4;
    public static final int ENGINEERING_HALL_A = 5;
    public static final int ENGINEERING_HALL_B = 6;
    public static final int ENGINEERING_HALL_C = 7;
    public static final int ENGINEERING_HALL_D = 8;
    public static final int WIDANG_HALL = 9;
    public static final int HANKYOUNG_HALL = 10;

    public static final int BEVERAGE = 0;
    public static final int FOOD = 1;

    //BuildingItem(int building_string_code, int km_away_from_building)
    public List<BuildingItem> buildings = new ArrayList<BuildingItem>();

    public BuildingsAndRestaurants(){
        //All buildings in school
        buildings.add(new BuildingItem(R.string.new_millennium_hall, 0, NEW_MILLENNIUM_HALL));
        buildings.add(new BuildingItem(R.string.students_building, 1, STUDENTS_BUILDING));
        buildings.add(new BuildingItem(R.string.the_commons, 1, THE_COMMONS));
        buildings.add(new BuildingItem(R.string.daewoo_hall, 1, DAEWOO_HALL));
        buildings.add(new BuildingItem(R.string.samsung_hall, 1, SAMSUNG_HALL));
        buildings.add(new BuildingItem(R.string.engineering_hall_a, 1, ENGINEERING_HALL_A));
        buildings.add(new BuildingItem(R.string.engineering_hall_b, 1, ENGINEERING_HALL_B));
        buildings.add(new BuildingItem(R.string.engineering_hall_c, 1, ENGINEERING_HALL_C));
        buildings.add(new BuildingItem(R.string.engineering_hall_d, 1, ENGINEERING_HALL_D));
        buildings.add(new BuildingItem(R.string.widang_hall, 1, WIDANG_HALL));
        buildings.add(new BuildingItem(R.string.hankyoung_hall, 1, HANKYOUNG_HALL));

        //All restaurants
        RestaurantItem crazyBrown = new RestaurantItem(R.string.crazy_brown, 11, 14, 30, R.drawable.crazy_brown);
        RestaurantItem veryBerry = new RestaurantItem(R.string.very_berry, 11, 14, 30, R.drawable.very_berry);
        RestaurantItem cafeAaa = new RestaurantItem(R.string.cafe_aaa, 11, 14, 30, R.drawable.cafe_aaa);

        //New Millennium Hall
        buildings.get(NEW_MILLENNIUM_HALL).addRestaurantItem(crazyBrown);
        buildings.get(NEW_MILLENNIUM_HALL).addRestaurantItem(veryBerry);
        buildings.get(NEW_MILLENNIUM_HALL).addRestaurantItem(cafeAaa);

        //Students Building


        //The Commons

        //Daewoo Hall

        //Add menus to restaurants
        //Crazy Brown
        crazyBrown.addCategoryItem(new CategoryItem(R.string.beverage));
        crazyBrown.addCategoryItem(new CategoryItem(R.string.food));
        crazyBrown.getCategoryItem(BEVERAGE).addMenus(
                new FoodItem(R.string.bubble_milk_tea, 5500, R.drawable.bubble_milk_tea),
                new FoodItem(R.string.lemon_tea, 4500, R.drawable.lemon_tea),
                new FoodItem(R.string.strawberry_yogurt, 6000, R.drawable.strawberry_yogurt),
                new FoodItem(R.string.americano, 4000, R.drawable.americano),
                new FoodItem(R.string.cafe_latte, 4500, R.drawable.cafe_latte),
                new FoodItem(R.string.cafe_mocha, 4800, R.drawable.cafe_mocha),
                new FoodItem(R.string.vanilla_latte, 5000, R.drawable.vanilla_latte),
                new FoodItem(R.string.green_tea_latte, 5000, R.drawable.green_tea_latte)
        );
        crazyBrown.getCategoryItem(FOOD).addMenus(
                new FoodItem(R.string.lemon_shrimp_linguine, 8000, R.drawable.lemon_shrimp_linguine),
                new FoodItem(R.string.vongole_pasta, 7000, R.drawable.vongole_pasta),
                new FoodItem(R.string.egg_sandwich, 6000, R.drawable.egg_sandwich),
                new FoodItem(R.string.baguette_pizza, 6500, R.drawable.baguette_pizza),
                new FoodItem(R.string.oven_spaghetti_carbonara, 8000, R.drawable.oven_spaghetti_carbonara),
                new FoodItem(R.string.oven_spaghetti_chicken_barbecue, 7500, R.drawable.oven_spaghetti_chicken_barbecue),
                new FoodItem(R.string.salmon_wine_salad, 6500, R.drawable.salmon_wine_salad),
                new FoodItem(R.string.chicken_breast_salad, 5500, R.drawable.chicken_breast_salad)
        );

        //Very Berry
        veryBerry.addCategoryItem(new CategoryItem(R.string.beverage));
        veryBerry.getCategoryItem(BEVERAGE).addMenus(
                new FoodItem(R.string.blackberry_yogurt, 5000, R.drawable.blackberry_yogurt),
                new FoodItem(R.string.blueberry_yogurt, 5300, R.drawable.blueberry_yogurt),
                new FoodItem(R.string.granola_yogurt, 5500, R.drawable.granola_yogurt),
                new FoodItem(R.string.mango_yogurt, 4800, R.drawable.mango_yogurt),
                new FoodItem(R.string.raspberry_yogurt, 5000, R.drawable.raspberry_yogurt)
        );
    }

    public List<BuildingItem> getBuildings(){
        return buildings;
    }

}
