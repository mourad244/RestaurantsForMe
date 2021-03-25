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
    private static final String TABLE_RESTAURANT = "restaurant";
    private static final String TABLE_FOOD_RESTAURANT = "food_restaurant";
    private static final String TABLE_MENU = "menu";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NOM = "nom";
    private static final String KEY_IMAGE = "image";

    // restaurant Table - column names
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_H_OUVERTURE = "h_ouverture";
    private static final String KEY_H_FERMETURE = "h_fermeture";
    private static final String KEY_TELEPHONE = "telephone";
    private static final String KEY_QR_CODE = "QR_code";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";

    // foodRestaurant Table - column names
    private static final String KEY_CATEGORIE_FOOD_ID = "categorieFood_id";
    private static final String KEY_RESTAURANT_ID = "restaurant_id";

    // menu Table - column names
    private static final String KEY_PRIX = "prix";

    // Table Create Statements
    // Categorie_food table create statement
    private static final String CREATE_TABLE_CATEGORIE_FOOD = "CREATE TABLE "+
            TABLE_CATEGORIE_FOOD + "("+
            KEY_ID + "INTEGER PRIMARY KEY,"+
            KEY_NOM + "TEXT," +
            KEY_IMAGE + "INTEGER"+")";

    // Restaurant table create statement
    private static final String CREATE_TABLE_RESTAURANT = "CREATE TABLE " +
            TABLE_RESTAURANT + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_NOM + " TEXT," +
            KEY_IMAGE + " INTEGER," +
            KEY_DESCRIPTION + " TEXT,"+
            KEY_H_OUVERTURE + " DATETIME,"+
            KEY_H_FERMETURE + " DATETIME,"+
            KEY_TELEPHONE + " TEXT,"+
            KEY_QR_CODE + " INTEGER,"+
            KEY_LONGITUDE + " TEXT,"+
            KEY_LATITUDE + " TEXT"+")";

    // Food_restaurant table create statement
    private static final String CREATE_TABLE_FOOD_RESTAURANT = "CREATE TABLE " +
            TABLE_FOOD_RESTAURANT + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_CATEGORIE_FOOD_ID + " INTEGER," +
            KEY_RESTAURANT_ID + " INTEGER"+")";

    // Menu table create statement
    private static final String CREATE_TABLE_MENU = "CREATE TABLE " +
            TABLE_MENU + "(" +
            KEY_ID + "INTEGER PRIMARY KEY,"+
            KEY_NOM + "TEXT," +
            KEY_IMAGE + "INTEGER,"+
            KEY_DESCRIPTION + " TEXT,"+
            KEY_PRIX + "INTEGER"+")";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating required tables
    db.execSQL(CREATE_TABLE_CATEGORIE_FOOD);
    db.execSQL(CREATE_TABLE_RESTAURANT);
    db.execSQL(CREATE_TABLE_FOOD_RESTAURANT);
    db.execSQL(CREATE_TABLE_MENU);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // On upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_CATEGORIE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_RESTAURANT);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_FOOD_RESTAURANT);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_MENU);


        //Create new tables
        onCreate(db);
    }

    /*
    * Creating a categorie_food
     */

    public long createCategorieFood( CategorieFood categorieFood, long[] restaurant_ids){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOM,categorieFood.getNom());
        values.put(KEY_IMAGE, categorieFood.getImage());

        // insert row
        long categorieFood_id = db.insert(TABLE_CATEGORIE_FOOD,null,values);

        // Assigning restaurants to categorie of foods
        for (long restaurant_id : restaurant_ids){
            createFoodRestaurant(categorieFood_id,restaurant_id);
        }
        return categorieFood_id;
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
        categorie.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        categorie.setNom(c.getString(c.getColumnIndex(KEY_NOM)));
        categorie.setImage(c.getInt(c.getColumnIndex(KEY_IMAGE)));

        return categorie;
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
                categorie.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                categorie.setNom(c.getString(c.getColumnIndex(KEY_NOM)));
                categorie.setImage(c.getInt(c.getColumnIndex(KEY_IMAGE)));

                // adding to categorie food list
                categorieFoods.add(categorie);
            } while (c.moveToNext());
        }
        return categorieFoods;
    }

    public long createRestaurant(Restaurant restaurant){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(KEY_NOM, restaurant.getNom());
        values.put(KEY_IMAGE, restaurant.getImage());
        values.put(KEY_DESCRIPTION,restaurant.getDescription());
        values.put(KEY_H_OUVERTURE,restaurant.getH_ouverture());
        values.put(KEY_H_FERMETURE,restaurant.getH_fermeture());
        values.put(KEY_TELEPHONE,restaurant.getTelephone());
        values.put(KEY_QR_CODE, restaurant.getQR_code());
        values.put(KEY_LONGITUDE, restaurant.getLongitude());
        values.put(KEY_LATITUDE, restaurant.getLatitude());

        // insert row
        long restaurant_id = db.insert(TABLE_RESTAURANT,null, values);
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
                r.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                r.setNom(c.getString(c.getColumnIndex(KEY_NOM)));
                r.setImage(c.getInt(c.getColumnIndex(KEY_IMAGE)));
                r.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                r.setH_ouverture(c.getString(c.getColumnIndex(KEY_H_OUVERTURE)));
                r.setH_fermeture(c.getString(c.getColumnIndex(KEY_H_FERMETURE)));
                r.setTelephone(c.getString(c.getColumnIndex(KEY_TELEPHONE)));
                r.setQR_code(c.getInt(c.getColumnIndex(KEY_QR_CODE)));
                r.setLongitude(c.getString(c.getColumnIndex(KEY_LONGITUDE)));
                r.setLatitude(c.getString(c.getColumnIndex(KEY_LATITUDE)));

                // adding to restaurants list
                restaurants.add(r);
            } while (c.moveToNext());
        }
        return restaurants;
    }
}
