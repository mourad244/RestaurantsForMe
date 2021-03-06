package com.hfad.restaurantsforme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "Databasehepler";

    // Database Version
    private static final int DATABASE_VERSION =1;

    // Database Name
    private static final String DATABASE_NAME = "RestaurantInfo";

    //Table Names
    private static final String TABLE_CATEGORIE_FOOD = "categorie_food";
    private static final String TABLE_TAG_FOOD = "tags_food";
    private static final String TABLE_RESTAURANT = "restaurant";
    private static final String TABLE_FOOD_RESTAURANT = "food_restaurant";
    private static final String TABLE_FOOD = "food";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NOM = "nom";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_CATEGORIE_FOOD_ID = "categorieFood_id";


    // restaurant Table - column names
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_H_OUVERTURE = "h_ouverture";
    private static final String KEY_H_FERMETURE = "h_fermeture";
    private static final String KEY_TELEPHONE = "telephone";
    private static final String KEY_QR_CODE = "QR_code";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";

    // foodRestaurant Table - column names
    private static final String KEY_RESTAURANT_ID = "restaurant_id";

    // food Table - column names
    private static final String KEY_PRIX = "prix";

    // Table Create Statements
    // Categorie_food table create statement
    private static final String CREATE_TABLE_CATEGORIE_FOOD = "CREATE TABLE "+
            TABLE_CATEGORIE_FOOD + "("+
            KEY_ID + " TEXT PRIMARY KEY NOT NULL, "+
            KEY_NOM + " TEXT, " +
            KEY_IMAGE + " INTEGER"+")";

    // Tag_food table  create statement
    private static final String CREATE_TABLE_TAG_FOOD = "CREATE TABLE "+
            TABLE_TAG_FOOD + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            KEY_NOM + " TEXT, " +
            KEY_CATEGORIE_FOOD_ID + " INTEGER "+ ")";
//REFERENCES " + TABLE_CATEGORIE_FOOD +"("+ KEY_ID+")
    // Restaurant table create statement
    private static final String CREATE_TABLE_RESTAURANT = "CREATE TABLE " +
            TABLE_RESTAURANT + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            KEY_NOM + " TEXT, " +
            KEY_IMAGE + " INTEGER, " +
            KEY_DESCRIPTION + " TEXT, "+
            KEY_H_OUVERTURE + " DATETIME, "+
            KEY_H_FERMETURE + " DATETIME, "+
            KEY_TELEPHONE + " TEXT, "+
            KEY_QR_CODE + " INTEGER, "+
            KEY_LONGITUDE + " DOUBLE, "+
            KEY_LATITUDE + " DOUBLE"+")";

    // Food_restaurant table create statement
    private static final String CREATE_TABLE_FOOD_RESTAURANT = "CREATE TABLE " +
            TABLE_FOOD_RESTAURANT + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            KEY_CATEGORIE_FOOD_ID + " INTEGER, " +
            KEY_RESTAURANT_ID + " INTEGER"+")";

    // Menu table create statement
    private static final String CREATE_TABLE_FOOD = "CREATE TABLE " +
            TABLE_FOOD + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            KEY_NOM + " TEXT, " +
            KEY_IMAGE + " INTEGER, "+
            KEY_DESCRIPTION + " TEXT, "+
            KEY_PRIX + " INTEGER, " +
            KEY_RESTAURANT_ID + " INTEGER"+")";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating required tables
    db.execSQL(CREATE_TABLE_CATEGORIE_FOOD);
    db.execSQL(CREATE_TABLE_RESTAURANT);
    db.execSQL(CREATE_TABLE_FOOD_RESTAURANT);
    db.execSQL(CREATE_TABLE_FOOD);
    db.execSQL(CREATE_TABLE_TAG_FOOD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // On upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_CATEGORIE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_RESTAURANT);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_FOOD_RESTAURANT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG_FOOD);


        //Create new tables
        onCreate(db);
    }
    /*
    Create food
     */

    public long createFood(Food food){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(KEY_NOM, food.getNom());
        values.put(KEY_IMAGE, food.getImage());
        values.put(KEY_DESCRIPTION,food.getDescription());
        values.put(KEY_PRIX,food.getPrix());
        values.put(KEY_RESTAURANT_ID,food.getRestaurantId());

        // insert row
        long food_id = db.insert(TABLE_FOOD,null, values);
        // Assigning restaurants to categorie of foods
        sortRestaurantByCategorieeFood(food.getRestaurantId());
        db.close();
        return food_id;
    }
    /*
    get the menu of a restaurant;
     */
    public List<Food> getFoodsByRestaurant(long restaurantId){
        List<Food> foods = new ArrayList<>();
        String selectedQuery = "SELECT * FROM " + TABLE_FOOD + " WHERE "+ KEY_RESTAURANT_ID +
                " = "+restaurantId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectedQuery,null);

        // looping through all rows and adding to list
        if (c.moveToFirst()){
            do{
                Food food = new Food();
                food.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                food.setNom(c.getString(c.getColumnIndex(KEY_NOM)));
                food.setImage(c.getBlob(c.getColumnIndex(KEY_IMAGE)));
                food.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                food.setPrix(c.getInt(c.getColumnIndex(KEY_PRIX)));
                food.setRestaurantId(c.getInt(c.getColumnIndex(KEY_RESTAURANT_ID)));
                // adding to categorie food list
                foods.add(food);
            } while (c.moveToNext());
        }
        return foods;
    }

    // when creating a menu for a restaurant, it automatically adds it into a categorie of food
    // depending of its contents: by searching in menu if a food exists in a list of predefined tagfood,
    // and add it to the categorieFood if not already exists

    public void sortRestaurantByCategorieeFood(long restaurantId){
        List <TagFood> tagFoodList = getAllTagFoods();
        List <Food> foods;

        foods = getFoodsByRestaurant(restaurantId);

        for (int i =0; i<tagFoodList.size();i++){
            int j = 0;
            boolean found = false;

            while (!found && j<foods.size())  {
                // check if food is in the tag food
                if ((foods.get(j).getNom()).toLowerCase().contains(tagFoodList.get(i).getNom())){
                    // add the restaurant to the food_restaurant if not already exists
                    // 1. check if already exist
                    found = restaurantIsIncluded(
                            tagFoodList.get(i).getCategorieFoodId(),
                            restaurantId);
                    // if does not exist, add restaurant to categorie food
                    if (!found)
                        createFoodRestaurant( tagFoodList.get(i).getCategorieFoodId(),restaurantId);
                }
                j++;
            }
        }

    }

    /*
    check if a restaurant is in restaurant_food
     */
    public boolean restaurantIsIncluded(long categorieFoodId, long restaurantId){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectedQuery ="SELECT "+ KEY_ID + " FROM " + TABLE_FOOD_RESTAURANT + " WHERE " +
                KEY_CATEGORIE_FOOD_ID + " = " + categorieFoodId + " AND " + KEY_RESTAURANT_ID +
                " = " + restaurantId;
        Cursor c = db.rawQuery(selectedQuery,null);

        if(c.moveToFirst()){
            do{
                CategorieFood categorieFood = new CategorieFood();
                categorieFood.setId(c.getString(c.getColumnIndex(KEY_ID)));
                if (!categorieFood.getId().isEmpty()){
                    return true;
                }
                // add to restaurants list

            } while (c.moveToNext());
        }
        c.close();
        return false;
    }
    /*
    * Create a tag_food
     */
    public List createTagFood(List<TagFood> tagFood){
        SQLiteDatabase db = this.getWritableDatabase();
        List tagFoodIds = new ArrayList();
        for(int i=0;i<tagFood.size();i++){
            ContentValues values = new ContentValues();
            values.put(KEY_NOM, tagFood.get(i).getNom());
            values.put(KEY_CATEGORIE_FOOD_ID, tagFood.get(i).getCategorieFoodId());

            // insert row
            long tagFoodId = db.insert(TABLE_TAG_FOOD,null,values);
            tagFoodIds.add(tagFoodId);
        }
        
        db.close();
        return tagFoodIds;
    }

    /*
    get all tagFood
     */
    public List<TagFood> getAllTagFoods(){
        List<TagFood> tagFoods = new ArrayList<>();
        String selectedQuery = "SELECT * FROM " + TABLE_TAG_FOOD;

        Log.e(LOG, selectedQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectedQuery,null);

        // looping through all rows and adding to list
        if (c.moveToFirst()){
            do{
                TagFood tagFood = new TagFood();
                tagFood.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                tagFood.setNom(c.getString(c.getColumnIndex(KEY_NOM)));
                tagFood.setCategorieFoodId(c.getInt(c.getColumnIndex(KEY_CATEGORIE_FOOD_ID)));

                // adding to categorie food list
                tagFoods.add(tagFood);
            } while (c.moveToNext());
        }
        return tagFoods;
    }

    /*
    * Creating a categorie_food
     */

    public long createCategorieFood( CategorieFood categorieFood){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOM, categorieFood.getNom());
//        values.put(KEY_IMAGE, categorieFood.getUrlImage());

        // insert row
        long categorieFood_id = db.insert(TABLE_CATEGORIE_FOOD,null,values);

        db.close();
        return categorieFood_id;
    }


      /*
    get restaurant having of a categorie food
     */

    public List<Restaurant> getFoodRestaurants(long categorieFood_id){
        List<Restaurant> restaurants = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectedQuery =" SELECT * FROM " + TABLE_RESTAURANT + " INNER JOIN "
                + TABLE_FOOD_RESTAURANT + " ON " + TABLE_RESTAURANT + "." + KEY_ID +
                " = " + TABLE_FOOD_RESTAURANT + "." + KEY_RESTAURANT_ID + " AND " +
                KEY_CATEGORIE_FOOD_ID + " = " + categorieFood_id;

//        String selectedQuery = "SELECT * FROM " + TABLE_FOOD_RESTAURANT + " WHERE " +
//                KEY_CATEGORIE_FOOD_ID + " = " + categorieFood_id;

        Log.e(LOG, selectedQuery);

        Cursor c = db.rawQuery(selectedQuery,null);

        if(c.moveToFirst()){
            do{
                Restaurant restaurant = new Restaurant();
                restaurant.setId(c.getString(c.getColumnIndex(KEY_ID)));
                restaurant.setNom(c.getString(c.getColumnIndex(KEY_NOM)));
//                restaurant.setUrlImage(c.getBlob(c.getColumnIndex(KEY_IMAGE)));
                restaurant.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                restaurant.setH_fermeture(c.getString(c.getColumnIndex(KEY_H_FERMETURE)));
                restaurant.setH_ouverture(c.getString(c.getColumnIndex(KEY_H_OUVERTURE)));
                restaurant.setTelephone(c.getString(c.getColumnIndex(KEY_TELEPHONE)));
//                restaurant.setQR_code(c.getBlob(c.getColumnIndex(KEY_QR_CODE)));
                restaurant.setLongitude(c.getDouble(c.getColumnIndex(KEY_LONGITUDE)));
                restaurant.setLatitude(c.getDouble(c.getColumnIndex(KEY_LATITUDE)));

                // add to restaurants list
                restaurants.add(restaurant);
            } while (c.moveToNext());
        }
        c.close();
        return restaurants;
    }

    /*
    * get all categories of food
     */

    public List<CategorieFood> getAllCategorieFoods(){
        List<CategorieFood> categorieFoods = new ArrayList<>();
        String selectedQuery = "SELECT * FROM " + TABLE_CATEGORIE_FOOD;

        Log.e(LOG, selectedQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectedQuery,null);

        // looping through all rows and adding to list
        if (c.moveToFirst()){
            do{
                CategorieFood categorie = new CategorieFood();
                categorie.setId(c.getString(c.getColumnIndex(KEY_ID)));
                categorie.setNom(c.getString(c.getColumnIndex(KEY_NOM)));
//                categorie.setUrlImage(c.getString(c.getColumnIndex(KEY_IMAGE)));

                // adding to categorie food list
                categorieFoods.add(categorie);
            } while (c.moveToNext());
        }
        return categorieFoods;
    }



    public long createRestaurant(Restaurant restaurant, long[] categorieFood_ids){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(KEY_NOM, restaurant.getNom());
//        values.put(KEY_IMAGE, restaurant.getUrlImage());
        values.put(KEY_DESCRIPTION,restaurant.getDescription());
        values.put(KEY_H_OUVERTURE,restaurant.getH_ouverture());
        values.put(KEY_H_FERMETURE,restaurant.getH_fermeture());
        values.put(KEY_TELEPHONE,restaurant.getTelephone());
//        values.put(KEY_QR_CODE, restaurant.getQR_code());
        values.put(KEY_LONGITUDE, restaurant.getLongitude());
        values.put(KEY_LATITUDE, restaurant.getLatitude());
        // insert row
        long restaurant_id = db.insert(TABLE_RESTAURANT,null, values);
        // Assigning restaurants to categorie of foods
        for (long categorieFood_id : categorieFood_ids){
            createFoodRestaurant(categorieFood_id,restaurant_id);
        }
        db.close();
        return restaurant_id;
    }
    // create restaurant without defining its categorie
    public long createRestaurant(Restaurant restaurant){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values= new ContentValues();

        values.put(KEY_NOM, restaurant.getNom());
//        values.put(KEY_IMAGE, restaurant.getUrlImage());
        values.put(KEY_DESCRIPTION,restaurant.getDescription());
        values.put(KEY_H_OUVERTURE,restaurant.getH_ouverture());
        values.put(KEY_H_FERMETURE,restaurant.getH_fermeture());
        values.put(KEY_TELEPHONE,restaurant.getTelephone());
//        values.put(KEY_QR_CODE, restaurant.getQR_code());
        values.put(KEY_LONGITUDE, restaurant.getLongitude());
        values.put(KEY_LATITUDE, restaurant.getLatitude());
        // insert row
        long restaurant_id = db.insert(TABLE_RESTAURANT,null, values);
        db.close();
        return restaurant_id;
    }

    /*
    *get all restaurants
     */

    public List<Restaurant> getAllRestaurants(){
        List<Restaurant> restaurants = new ArrayList<>();
        String selectedQuery = "SELECT * FROM " + TABLE_RESTAURANT;

        Log.e(LOG, selectedQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectedQuery, null);

        // Looping through all rows and adding to list
        if(c.moveToFirst()){
            do{
                Restaurant r = new Restaurant();
                r.setId(c.getString(c.getColumnIndex(KEY_ID)));
                r.setNom(c.getString(c.getColumnIndex(KEY_NOM)));
//                r.setUrlImage(c.getBlob(c.getColumnIndex(KEY_IMAGE)));
                r.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                r.setH_ouverture(c.getString(c.getColumnIndex(KEY_H_OUVERTURE)));
                r.setH_fermeture(c.getString(c.getColumnIndex(KEY_H_FERMETURE)));
                r.setTelephone(c.getString(c.getColumnIndex(KEY_TELEPHONE)));
//                r.setQR_code(c.getBlob(c.getColumnIndex(KEY_QR_CODE)));
                r.setLongitude(c.getDouble(c.getColumnIndex(KEY_LONGITUDE)));
                r.setLatitude(c.getDouble(c.getColumnIndex(KEY_LATITUDE)));

                // adding to restaurants list
                restaurants.add(r);
            } while (c.moveToNext());
        }
        return restaurants;
    }

    /*
    Assigning a restaurant to a food categorie
     */

    public long createFoodRestaurant(long categorieFood_id, long restaurant_id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORIE_FOOD_ID, categorieFood_id);
        values.put(KEY_RESTAURANT_ID, restaurant_id);

        long id = db.insert(TABLE_FOOD_RESTAURANT,null, values);
        return id;
    }

    /*
     * get single categorie food
     */
    public CategorieFood getCategorieFood(long categorieFood_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectedQuery = "SELECT * FROM " + TABLE_CATEGORIE_FOOD + " WHERE " +
                KEY_ID  + " = " + categorieFood_id;
        Log.e(LOG, selectedQuery);
        Cursor c = db.rawQuery(selectedQuery,null);

        if(c != null)
            c.moveToFirst();

        CategorieFood categorie = new CategorieFood();
        categorie.setId(c.getString(c.getColumnIndex(KEY_ID)));
        categorie.setNom(c.getString(c.getColumnIndex(KEY_NOM)));
//        categorie.setUrlImage(c.getString(c.getColumnIndex(KEY_IMAGE)));

        return categorie;
    }

    /*
    get a restaurant
     */
    public Restaurant getRestaurant(long restaurant_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectedQuery = "SELECT * FROM " + TABLE_RESTAURANT + " WHERE " +
                KEY_ID  + " = " + restaurant_id;
        Log.e(LOG, selectedQuery);

        Cursor c = db.rawQuery(selectedQuery,null);

        if(c != null)
            c.moveToFirst();
        Restaurant restaurant = new Restaurant();
        restaurant.setId(c.getString(c.getColumnIndex(KEY_ID)));
        restaurant.setNom(c.getString(c.getColumnIndex(KEY_NOM)));
//        restaurant.setUrlImage(c.getBlob(c.getColumnIndex(KEY_IMAGE)));
        restaurant.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
        restaurant.setH_ouverture(c.getString(c.getColumnIndex(KEY_H_OUVERTURE)));
        restaurant.setH_fermeture(c.getString(c.getColumnIndex(KEY_H_FERMETURE)));
        restaurant.setTelephone(c.getString(c.getColumnIndex(KEY_TELEPHONE)));
//        restaurant.setQR_code(c.getBlob(c.getColumnIndex(KEY_QR_CODE)));
        restaurant.setLongitude(c.getDouble(c.getColumnIndex(KEY_LONGITUDE)));
        restaurant.setLatitude(c.getDouble(c.getColumnIndex(KEY_LATITUDE)));
        db.close();
        return restaurant;
    }
    /*
    get restaurantId from foodRestaurant
     */
    public long getRestaurantId(long foodRestaurantId){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectedQuery = "SELECT " + KEY_RESTAURANT_ID + " FROM " + TABLE_FOOD_RESTAURANT +
                " WHERE " + KEY_ID  + " = " + foodRestaurantId;
        Log.e(LOG, selectedQuery);

        Cursor c = db.rawQuery(selectedQuery,null);
        if(c != null)
            c.moveToFirst();

        int restaurantId =c.getInt(c.getColumnIndex(KEY_RESTAURANT_ID));
        c.close();
        return restaurantId;
    }

    /*
    Closing database connection
     */
    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if( db != null && db.isOpen())
            db.close();
    }
}
