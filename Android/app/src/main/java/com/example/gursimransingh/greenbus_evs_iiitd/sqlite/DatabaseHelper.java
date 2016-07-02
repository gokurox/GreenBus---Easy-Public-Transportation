package com.example.gursimransingh.greenbus_evs_iiitd.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.Bus_Coordinates;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.Edges;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.Feedback;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.UserInfo;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.Vertice;

import java.util.ArrayList;

/**
 * Created by Gursimran Singh on 09-04-2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper DATABASE_INSTANCE = null;

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "greenbus.db";

    // TABLES
    public static final String TABLE_USER_LOGIN_INFO = "user_login_info";
    public static final String TABLE_FEEDBACK = "feedback";
    public static final String TABLE_BUS_COORDINATES = "bus_coordinates";
    public static final String TABLE_BUS = "bus";
    public static final String TABLE_VERTICES = "vertices";
    public static final String TABLE_EDGES = "edges";

    // Create Statements
    private static final String CREATE_TABLE_USER_LOGIN_INFO = "CREATE TABLE " + TABLE_USER_LOGIN_INFO + "("
                                                                         + "email TEXT PRIMARY KEY, "
                                                                         + "mobile TEXT NOT NULL, "
                                                                         + "name TEXT NOT NULL, "
                                                                         + "password TEXT NOT NULL, "
                                                                         + "login_status TEXT NOT NULL " +
                                                                           ")";
    private static final String CREATE_TABLE_FEEDBACK = "CREATE TABLE " + TABLE_FEEDBACK + "(" +
                                                                    "bus_number TEXT, " +
                                                                    "feedback TEXT, " +
                                                                    "rating REAL, " +
                                                                    "email TEXT " +
                                                                    ")";
    private static final String CREATE_TABLE_BUS_COORDINATES = "CREATE TABLE " + TABLE_BUS_COORDINATES + "(" +
                                                                            "bus_number TEXT, " +
                                                                            "stop_number INTEGER, " +
                                                                            "stop_name TEXT, " +
                                                                            "latitude REAL, " +
                                                                            "longitude REAL," +
                                                                            "FOREIGN KEY (stop_name) REFERENCES " + TABLE_VERTICES + "(vertex_name)" +
                                                                            ")";
    private static final String CREATE_TABLE_BUS = "CREATE TABLE " + TABLE_BUS + "(" +
                                                                "bus_number TEXT, " +
                                                                "path TEXT " +
                                                                ")";
    private static final String CREATE_TABLE_VERTICES = "CREATE TABLE " + TABLE_VERTICES + "(" +
                                                                    "vertex_name TEXT PRIMARY KEY" +
                                                                    ")";
    private static final String CREATE_TABLE_EDGES = "CREATE TABLE " + TABLE_EDGES + "(" +
                                                                "vertex_1 TEXT, " +
                                                                "vertex_2 TEXT, " +
                                                                "weight REAL, " +
                                                                "PRIMARY KEY (vertex_1, vertex_2, weight), " +
                                                                "FOREIGN KEY (vertex_1) REFERENCES " + TABLE_VERTICES + "(vertex_name), " +
                                                                "FOREIGN KEY (vertex_2) REFERENCES " + TABLE_VERTICES + "(vertex_name)" +
                                                                ")";

    /**********************************************************************************************/

    public static DatabaseHelper getInstance (Context context) {
        if (DATABASE_INSTANCE == null) {
            DATABASE_INSTANCE = new DatabaseHelper (context.getApplicationContext());
        }
        return DATABASE_INSTANCE;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER_LOGIN_INFO);
        db.execSQL(CREATE_TABLE_FEEDBACK);
        db.execSQL(CREATE_TABLE_BUS);
        db.execSQL(CREATE_TABLE_BUS_COORDINATES);
        db.execSQL(CREATE_TABLE_VERTICES);
        db.execSQL(CREATE_TABLE_EDGES);

        populateData_UserLoginInfo(db);
        populateData_Feedback(db);
        populateData_Vertices(db);
//        db.populateData_Bus(db);
        populateData_BusCoordinates(db);
        populateData_Edges(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL ("DROP TABLE IF EXISTS " + TABLE_USER_LOGIN_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUS_COORDINATES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERTICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDGES);

        onCreate(db);
    }

    public SQLiteDatabase getWritableDatabaseInstance() {
        return this.getWritableDatabase();
    }

    /**********************************************************************************************/

    public void populateData_UserLoginInfo (SQLiteDatabase db) {

        db.execSQL("INSERT INTO " + TABLE_USER_LOGIN_INFO + " VALUES " +
                   "('gursimran@iiitd.ac.in', '9654157119', 'Gursimran Singh', 'Demo@123', 'OFFLINE')" );
        db.execSQL("INSERT INTO " + TABLE_USER_LOGIN_INFO + " VALUES " +
                   "('harish@iiitd.ac.in', '9013459233', 'Harish Fulara', 'Demo@123', 'OFFLINE')" );
        db.execSQL("INSERT INTO " + TABLE_USER_LOGIN_INFO + " VALUES " +
                   "('mandeep@iiitd.ac.in', '9811273998', 'Mandeep Singh', 'Demo@123', 'OFFLINE')" );
        db.execSQL("INSERT INTO " + TABLE_USER_LOGIN_INFO + " VALUES " +
                   "('akash@iiitd.ac.in', '7065250678', 'Akashdeep', 'Demo@123', 'OFFLINE')" );
        db.execSQL("INSERT INTO " + TABLE_USER_LOGIN_INFO + " VALUES " +
                "('nayeem@iiitd.ac.in', '8527114478', 'Nayeem', 'Demo@123', 'OFFLINE')");

    }

    public boolean ULI_insertData (UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("email", userInfo.email);
        contentValues.put("mobile", userInfo.mobile);
        contentValues.put("name", userInfo.name);
        contentValues.put("password", userInfo.password);
        contentValues.put("login_status", userInfo.login_status);

        long rc = db.insert(TABLE_USER_LOGIN_INFO, null, contentValues);
        return (rc > 0);
    }

    public boolean ULI_updateData (UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put ("email", userInfo.email);
        contentValues.put ("mobile", userInfo.mobile);
        contentValues.put ("name", userInfo.name);
        contentValues.put("password", userInfo.password);
        contentValues.put ("login_status", userInfo.login_status);

        long rc = db.update(TABLE_USER_LOGIN_INFO, contentValues, "email" + " = ?", new String[]{userInfo.email});
        return (rc > 0);
    }

    public UserInfo ULI_getData (Context context, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * " +
                "FROM " + TABLE_USER_LOGIN_INFO + " " +
                "WHERE " + "email" + " = '" + email + "'", null);

        int rowCount = cursor.getCount();

        if (rowCount == 0)
            return null;
        if (rowCount > 1)
        {
            Toast.makeText (context.getApplicationContext(), "INCONSISTENT DATABASE: Multiple records map to" +
                                                                                    " single email: " + email, Toast.LENGTH_LONG).show();
            return null;
        }

        UserInfo userInfo = null;
        while (cursor.moveToNext()) {
            userInfo = new UserInfo ((cursor.getString (cursor.getColumnIndex ("email"))),
                                     (cursor.getString (cursor.getColumnIndex ("mobile"))),
                                     (cursor.getString (cursor.getColumnIndex ("name"))),
                                     (cursor.getString (cursor.getColumnIndex ("password"))),
                                     (cursor.getString (cursor.getColumnIndex ("login_status"))));
        }

        return userInfo;
    }

    public void ULI_setAllToOffline () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL ("UPDATE " + TABLE_USER_LOGIN_INFO + " " +
                    "SET " + "login_status" + " = '" + "OFFLINE" + "'");
    }

    public UserInfo ULI_getOnlineUser (Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * " +
                        "FROM " + TABLE_USER_LOGIN_INFO + " " +
                        "WHERE " + "login_status" + " = '" + "ONLINE" + "'",
                null);

        int rowCount = cursor.getCount();

        if (rowCount == 0)
            return null;
        if (rowCount > 1)
        {
            Toast.makeText (context.getApplicationContext(),
                            "INCONSISTENT DATABASE: Too many Logged in Users.. Setting all to offline",
                            Toast.LENGTH_LONG).show();
            ULI_setAllToOffline();
            return null;
        }

        UserInfo userInfo = null;
        while (cursor.moveToNext()) {
            userInfo = new UserInfo ((cursor.getString (cursor.getColumnIndex ("email"))),
                                     (cursor.getString (cursor.getColumnIndex ("mobile"))),
                                     (cursor.getString (cursor.getColumnIndex ("name"))),
                                     (cursor.getString (cursor.getColumnIndex ("password"))),
                                     (cursor.getString (cursor.getColumnIndex ("login_status"))));
        }

        return userInfo;
    }

    public boolean ULI_login (Context context, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * " +
                                    "FROM " + TABLE_USER_LOGIN_INFO + " " +
                                    "WHERE " + "email" + " = '" + email + "' AND " + "password" + " = '" + password + "'",
                                    null);

        int rowCount = cursor.getCount();

        if (rowCount == 0)
            return false;
        else if (rowCount > 1)
        {
            Toast.makeText (context.getApplicationContext(), "INCONSISTENT DATABASE: Multipe records map to" +
                                                " single email: " + email + " and password: " + password, Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            ULI_setAllToOffline();
            db.execSQL ("UPDATE " + TABLE_USER_LOGIN_INFO + " " +
                        "SET " + "login_status" + " = '" + "ONLINE" + "' " +
                        "WHERE " + "email" + " = '" + email + "' AND " + "password" + " = '" + password + "'");
            return true;
        }
    }

    /**********************************************************************************************/

    public void populateData_Feedback (SQLiteDatabase db) {

        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('8A', \"Amazing travel experience. Found the journey to be a good one. Would love to travel again using GreenBus.\", " +
                "5, 'gursimran@iiitd.ac.in')");
        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('8A', \"Nice experience.\", " +
                "4, 'nayeem@iiitd.ac.in')");
        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('8A', \"Fun experience. Never had so much ease in bus travel.\", " +
                "4, 'mandeep@iiitd.ac.in')");

        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('306', \"Amazing travel experience. Found the journey to be a good one. Would love to travel again using GreenBus.\", " +
                "5, 'gursimran@iiitd.ac.in')");
        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('306', \"Nice experience.\", " +
                "4, 'nayeem@iiitd.ac.in')");
        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('306', \"Fun experience. Never had so much ease in bus travel.\", " +
                "4, 'mandeep@iiitd.ac.in')");

        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('374', \"Amazing travel experience. Found the journey to be a good one. Would love to travel again using GreenBus.\", " +
                "5, 'gursimran@iiitd.ac.in')");
        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('374', \"Nice experience.\", " +
                "4, 'nayeem@iiitd.ac.in')");
        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('374', \"Fun experience. Never had so much ease in bus travel.\", " +
                "4, 'mandeep@iiitd.ac.in')");

        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('375', \"Amazing travel experience. Found the journey to be a good one. Would love to travel again using GreenBus.\", " +
                "5, 'gursimran@iiitd.ac.in')");
        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('375', \"Nice experience.\", " +
                "4, 'nayeem@iiitd.ac.in')");
        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('375', \"Fun experience. Never had so much ease in bus travel.\", " +
                "4, 'mandeep@iiitd.ac.in')");

        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('492', \"Amazing travel experience. Found the journey to be a good one. Would love to travel again using GreenBus.\", " +
                "5, 'gursimran@iiitd.ac.in')");
        db.execSQL ("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('492', \"Nice experience.\", " +
                "4, 'nayeem@iiitd.ac.in')");
        db.execSQL("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('492', \"Fun experience. Never had so much ease in bus travel.\", " +
                "4, 'mandeep@iiitd.ac.in')");

    }

    public void insert_Feedback (Feedback feedback) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " + TABLE_FEEDBACK + " VALUES " +
                "('" + feedback.bus_number + "', \"" + feedback.feedback + "\", " + feedback.rating + ", '" + feedback.email + "')");

    }

    public ArrayList<Feedback> getData_Feedback () {
        ArrayList<Feedback> data = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * " +
                "FROM " + TABLE_FEEDBACK, null);

        while (cursor.moveToNext()) {
            Feedback feedback = new Feedback (cursor.getString (cursor.getColumnIndex ("bus_number")),
                                              cursor.getString (cursor.getColumnIndex ("feedback")),
                                              cursor.getFloat (cursor.getColumnIndex ("rating")),
                                              cursor.getString (cursor.getColumnIndex ("email")));
            data.add (feedback);
        }

        cursor.close();

        return data;
    }

    public ArrayList<Feedback> getData_Feedback (String bus_number) {
        ArrayList<Feedback> data = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery ("SELECT * " +
                "FROM " + TABLE_FEEDBACK + " " +
                "WHERE bus_number = '" + bus_number + "'", null);

        while (cursor.moveToNext()) {
            Feedback feedback = new Feedback (cursor.getString (cursor.getColumnIndex ("bus_number")),
                    cursor.getString (cursor.getColumnIndex ("feedback")),
                    cursor.getFloat (cursor.getColumnIndex ("rating")),
                    cursor.getString (cursor.getColumnIndex ("email")));
            data.add (feedback);
        }

        cursor.close();

        return data;
    }

    /**********************************************************************************************/

    public void populateData_BusCoordinates (SQLiteDatabase db) {
        String query = "INSERT INTO " + TABLE_BUS_COORDINATES + " VALUES ";

        db.execSQL (query + "('8A', 1, 'NEHRU PLACE TERMINAL', 28.547601, 77.254648)");
        db.execSQL (query + "('8A', 2, 'PARAS CINEMA', 28.548803, 77.256750)");
        db.execSQL (query + "('8A', 3, 'KALKAJI TEMPLE', 28.547764, 77.262811)");
        db.execSQL (query + "('8A', 4, 'GOVIND PURI', 28.545798, 77.263928)");
        db.execSQL (query + "('8A', 5, 'KALKAJI DEPOT', 28.539467, 77.266133)");
        db.execSQL (query + "('8A', 6, 'C-LAL CHOWK', 28.533999, 77.268657)");
        db.execSQL (query + "('8A', 7, 'OKHLA INDUSTRIES AREA', 28.533164, 77.269106)");
        db.execSQL (query + "('8A', 8, 'CROWN PLAZA', 28.529487, 77.270916)");
        db.execSQL (query + "('8A', 9, 'SARITA VIHAR X-LNG', 28.538460, 77.292200)");
        db.execSQL (query + "('8A', 10, 'JASOLA VIHAR', 28.540794, 77.297181)");
        db.execSQL (query + "('8A', 11, 'SHAHEEN BAGH', 28.543074, 77.302831)");
        db.execSQL (query + "('8A', 12, 'KALINDI KUNJ', 28.545282, 77.305924)");
        db.execSQL (query + "('8A', 13, 'SECTOR-94', 28.553635, 77.321089)");
        db.execSQL (query + "('8A', 14, 'SECTOR-18-37', 28.560622, 77.335227)");
        db.execSQL (query + "('8A', 15, 'NOIDA SECTOR-43', 28.561332, 77.346652)");
        db.execSQL (query + "('8A', 16, 'BAROLA MARKET', 28.560028, 77.370131)");
        db.execSQL (query + "('8A', 17, 'SECTOR-106', 28.534999, 77.391463)");
        db.execSQL (query + "('8A', 18, 'HOUSARY COMPLEX PHASE-2', 28.535331, 77.406943)");
        db.execSQL (query + "('8A', 19, 'NOIDA PHASE-2', 28.535775, 77.414138)");

        db.execSQL (query + "('306', 1, 'NEHRU PLACE TERMINAL', 28.547601, 77.254648)");
        db.execSQL (query + "('306', 2, 'PARAS CINEMA', 28.548803, 77.256750)");
        db.execSQL (query + "('306', 3, 'N.S.I.C', 28.551786, 77.264019)");
        db.execSQL (query + "('306', 4, 'MODI MILLS X-ING', 28.555441, 77.265098)");
        db.execSQL (query + "('306', 5, 'KALKA MOR', 28.557629, 77.269605)");
        db.execSQL (query + "('306', 6, 'ISHWAR NAGAR', 28.561504, 77.266183)");
        db.execSQL (query + "('306', 7, 'NEW FRIENDS COLONY', 28.567470, 77.269091)");
        db.execSQL (query + "('306', 8, 'ASHRAM', 28.571880, 77.258860)");
        db.execSQL (query + "('306', 9, 'MAHARANI BAGH', 28.576626, 77.264320)");
        db.execSQL (query + "('306', 10, 'GURUDWARA BALA SAHIB', 28.581214, 77.265907)");
        db.execSQL (query + "('306', 11, 'SARAI KALE KHAN', 28.587075, 77.258876)");
        db.execSQL (query + "('306', 12, 'EAST ROAD BRIDGE', 28.599080, 77.257741)");
        db.execSQL (query + "('306', 13, 'PWD OFFICE', 28.603432, 77.268679)");
        db.execSQL (query + "('306', 14, 'NOIDA X-ING', 28.612859, 77.282063)");
        db.execSQL (query + "('306', 15, 'SAMASPUR JAGIR VILLAGE', 28.615430, 77.284588)");
        db.execSQL (query + "('306', 16, 'PATPARGANJ X-ING', 28.616352, 77.287978)");
        db.execSQL (query + "('306', 17, 'PANDAV NAGAR POLICE STATION', 28.614860, 77.290040)");
        db.execSQL (query + "('306', 18, 'SASHI GARDEN X-ING', 28.613627, 77.293992)");
        db.execSQL (query + "('306', 19, 'MAYUR VIHAR PHASE-1 POCKET-5', 28.613310, 77.295798)");
        db.execSQL (query + "('306', 20, 'ITI KHICRIPUR', 28.612806, 77.297606)");
        db.execSQL (query + "('306', 21, 'TRILOKPURI 13 BLOCK', 28.612818, 77.308782)");
        db.execSQL (query + "('306', 22, 'CHAND CINEMA', 28.612732, 77.311182)");
        db.execSQL (query + "('306', 23, 'SUPER BAZAR', 28.613562, 77.316257)");
        db.execSQL (query + "('306', 24, 'KALYANPURI', 28.615522, 77.319499)");

        db.execSQL (query + "('374', 1, 'NEHRU PLACE TERMINAL', 28.547601, 77.254648)");
        db.execSQL (query + "('374', 2, 'SANT NAGAR', 28.552393, 77.246095)");
        db.execSQL (query + "('374', 3, 'MOOL CHAND KHARATI RAM HOSPITAL', 28.569244, 77.235580)");
        db.execSQL (query + "('374', 4, 'PANT NAGAR', 28.583844, 77.240467)");
        db.execSQL (query + "('374', 5, 'SUNDER NAGAR', 28.602000, 77.240365)");
        db.execSQL (query + "('374', 6, 'PRAGATI MAIDAN', 28.615262, 77.240022)");
        db.execSQL (query + "('374', 7, 'I.T.O(RING ROAD)', 28.628284, 77.251220)");
        db.execSQL (query + "('374', 8, 'RAINY WELL', 28.628269, 77.260857)");
        db.execSQL (query + "('374', 9, 'SHAKAR PUR', 28.627394, 77.279369)");
        db.execSQL (query + "('374', 10, 'JAGAT PURI A-BLOCK', 28.648399, 77.294742)");
        db.execSQL (query + "('374', 11, 'SWARN CINEMA', 28.662691, 77.286219)");
        db.execSQL (query + "('374', 12, 'SEELAM PUR', 28.670573, 77.265750)");
        db.execSQL (query + "('374', 13, 'BABARPUR', 28.690137, 77.282145)");
        db.execSQL (query + "('374', 14, 'JYOTI COLONY', 28.688005, 77.289720)");
        db.execSQL (query + "('374', 15, 'NAND NAGAR TERMINAL', 28.691459, 77.309269)");

        db.execSQL (query + "('375', 1, 'NEHRU PLACE TERMINAL', 28.547601, 77.254648)");
        db.execSQL (query + "('375', 2, 'SANT NAGAR', 28.552393, 77.246095)");
        db.execSQL (query + "('375', 3, 'L.S.R COLLEGE', 28.558726, 77.237263)");
        db.execSQL (query + "('375', 4, 'ANDREWS GANJ', 28.566153, 77.229331)");
        db.execSQL (query + "('375', 5, 'PT COLLEGE KOTLA', 28.569810, 77.226561)");
        db.execSQL (query + "('375', 6, 'SUKHDEV MARKET KOTLA', 28.574276, 77.228724)");
        db.execSQL (query + "('375', 7, 'JAWAHAR LAL NEHRU STADIUM', 28.583920, 77.229868)");
        db.execSQL (query + "('375', 8, 'KHAN MARKET', 28.603151, 77.227445)");
        db.execSQL (query + "('375', 9, 'BARODA HOUSE', 28.616750, 77.228748)");
        db.execSQL (query + "('375', 10, 'PATIALA HOUSE', 28.616513, 77.233698)");
        db.execSQL (query + "('375', 11, 'SUPREME COURT', 28.621749, 77.237625)");
        db.execSQL (query + "('375', 12, 'ITO AGCR', 28.627857, 77.244606)");
        db.execSQL (query + "('375', 13, 'RAINY WELL', 28.628279, 77.260870)");
        db.execSQL (query + "('375', 14, 'LAXMI NAGAR', 28.638131, 77.269527)");
        db.execSQL (query + "('375', 15, 'NIRMAN VIHAR', 28.636767, 77.286896)");
        db.execSQL (query + "('375', 16, 'NEW RAJDHANI ENCLAVE', 28.642147, 77.295779)");
        db.execSQL (query + "('375', 17, 'DDA FLATS KARKADOMA', 28.651801, 77.302062)");
        db.execSQL (query + "('375', 18, 'YAMUNA SPORTS COMPLEX', 28.660101, 77.311267)");
        db.execSQL (query + "('375', 19, 'RAMAPRASTH XING', 28.660739, 77.319916)");
        db.execSQL (query + "('375', 20, 'SHAHDARA BORDER', 28.676227, 77.321735)");
        db.execSQL (query + "('375', 21, 'DILSHAD GARDEN POCKET P', 28.686148, 77.321918)");

        db.execSQL (query + "('492', 1, 'NEHRU PLACE TERMINAL', 28.547601, 77.254648)");
        db.execSQL (query + "('492', 2, 'PARAS CINEMA', 28.548803, 77.256750)");
        db.execSQL (query + "('492', 3, 'MODI MILL', 28.555508, 77.265079)");
        db.execSQL (query + "('492', 4, 'GARHI VILLAGE', 28.562385, 77.251573)");
        db.execSQL (query + "('492', 5, 'P.G.D.A.V COLLEGE', 28.567422, 77.252884)");
        db.execSQL (query + "('492', 6, 'MAHARANI BAGH', 28.576228, 77.264385)");
        db.execSQL (query + "('492', 7, 'GURUDWARA SRI BANGLA SAHIB', 28.581249, 77.265951)");
        db.execSQL (query + "('492', 8, 'PWD OFFICE', 28.603710, 77.268667)");
        db.execSQL (query + "('492', 9, 'MAYUR VIHAR PHASE-1', 28.604031, 77.289195)");
        db.execSQL (query + "('492', 11, 'SECTOR 15', 28.583042, 77.313197)");
        db.execSQL (query + "('492', 12, 'RAJNI GANDHA CHOWL', 28.577767, 77.318813)");
        db.execSQL (query + "('492', 13, 'BSNL SECTOR-19', 28.583646, 77.327042)");
        db.execSQL (query + "('492', 14, 'NOIDA STADIUM', 28.590237, 77.335936)");
        db.execSQL (query + "('492', 15, 'NOIDA SECTOR 12-22', 28.599179, 77.345569)");
        db.execSQL (query + "('492', 16, 'ROYAL TOWERS', 28.600849, 77.368325)");
        db.execSQL (query + "('492', 17, 'SABJI MANDI BUS STOP', 28.605915, 77.372392)");
        db.execSQL (query + "('492', 18, 'RO APARTMENTS', 28.614742, 77.373471)");
        db.execSQL (query + "('492', 19, 'FORTIS HOSPITAL', 28.618387, 77.373865)");
        db.execSQL(query + "('492', 20, 'SECTOR-62', 28.620752, 77.363873)");

    }

    public ArrayList<Bus_Coordinates> getData_Bus_Coordinates () {
        ArrayList<Bus_Coordinates> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery ("SELECT * " +
                "FROM " + TABLE_BUS_COORDINATES + " " +
                "ORDER BY bus_number, stop_number ASC", null);

        while (cursor.moveToNext()) {
            Bus_Coordinates bus_coordinates = new Bus_Coordinates (cursor.getString (cursor.getColumnIndex ("bus_number")),
                    cursor.getInt(cursor.getColumnIndex("stop_number")),
                    cursor.getString(cursor.getColumnIndex("stop_name")),
                    cursor.getDouble (cursor.getColumnIndex ("latitude")),
                    cursor.getDouble (cursor.getColumnIndex ("longitude")));
            data.add (bus_coordinates);
        }

        cursor.close();

        return data;
    }

    public ArrayList<Bus_Coordinates> getData_Bus_Coordinates (String bus_number) {
        ArrayList<Bus_Coordinates> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery ("SELECT * " +
                "FROM " + TABLE_BUS_COORDINATES + " " +
                "WHERE bus_number = '" + bus_number + "' " +
                "ORDER BY stop_number ASC", null);

        while (cursor.moveToNext()) {
            Bus_Coordinates bus_coordinates = new Bus_Coordinates (cursor.getString (cursor.getColumnIndex ("bus_number")),
                    cursor.getInt(cursor.getColumnIndex("stop_number")),
                    cursor.getString(cursor.getColumnIndex("stop_name")),
                    cursor.getDouble (cursor.getColumnIndex ("latitude")),
                    cursor.getDouble (cursor.getColumnIndex ("longitude")));
            data.add (bus_coordinates);
        }

        cursor.close();

        return data;
    }

    /**********************************************************************************************/

    public void populateData_Vertices (SQLiteDatabase db) {
        String query = "INSERT INTO " + TABLE_VERTICES + " VALUES ";

        db.execSQL (query + "('ANDREWS GANJ')");
        db.execSQL (query + "('ASHRAM')");
        db.execSQL (query + "('BABARPUR')");
        db.execSQL (query + "('BARODA HOUSE')");
        db.execSQL (query + "('BAROLA MARKET')");
        db.execSQL (query + "('BSNL SECTOR-19')");
        db.execSQL (query + "('C-LAL CHOWK')");
        db.execSQL (query + "('CHAND CINEMA')");
        db.execSQL (query + "('CROWN PLAZA')");
        db.execSQL (query + "('DDA FLATS KARKADOMA')");
        db.execSQL (query + "('DILSHAD GARDEN POCKET P')");
        db.execSQL (query + "('EAST ROAD BRIDGE')");
        db.execSQL (query + "('FORTIS HOSPITAL')");
        db.execSQL (query + "('GARHI VILLAGE')");
        db.execSQL (query + "('GOVIND PURI')");
        db.execSQL (query + "('GURUDWARA BALA SAHIB')");
        db.execSQL (query + "('GURUDWARA SRI BANGLA SAHIB')");
        db.execSQL (query + "('HOUSARY COMPLEX PHASE-2')");
        db.execSQL (query + "('I.T.O(RING ROAD)')");
        db.execSQL (query + "('ISHWAR NAGAR')");
        db.execSQL (query + "('ITI KHICRIPUR')");
        db.execSQL (query + "('ITO AGCR')");
        db.execSQL (query + "('JAGAT PURI A-BLOCK')");
        db.execSQL (query + "('JASOLA VIHAR')");
        db.execSQL (query + "('JAWAHAR LAL NEHRU STADIUM')");
        db.execSQL (query + "('JYOTI COLONY')");
        db.execSQL (query + "('KALINDI KUNJ')");
        db.execSQL (query + "('KALKA MOR')");
        db.execSQL (query + "('KALKAJI DEPOT')");
        db.execSQL (query + "('KALKAJI TEMPLE')");
        db.execSQL (query + "('KALYANPURI')");
        db.execSQL (query + "('KHAN MARKET')");
        db.execSQL (query + "('L.S.R COLLEGE')");
        db.execSQL (query + "('LAXMI NAGAR')");
        db.execSQL (query + "('MAHARANI BAGH')");
        db.execSQL (query + "('MAYUR KUNJ')");
        db.execSQL (query + "('MAYUR VIHAR PHASE-1 POCKET-5')");
        db.execSQL (query + "('MAYUR VIHAR PHASE-1')");
        db.execSQL (query + "('MODI MILL')");
        db.execSQL (query + "('MODI MILLS X-ING')");
        db.execSQL (query + "('MOOL CHAND KHARATI RAM HOSPITAL')");
        db.execSQL (query + "('N.S.I.C')");
        db.execSQL (query + "('NAND NAGAR TERMINAL')");
        db.execSQL (query + "('NEHRU PLACE TERMINAL')");
        db.execSQL (query + "('NEW FRIENDS COLONY')");
        db.execSQL (query + "('NEW RAJDHANI ENCLAVE')");
        db.execSQL (query + "('NIRMAN VIHAR')");
        db.execSQL (query + "('NOIDA PHASE-2')");
        db.execSQL (query + "('NOIDA SECTOR 12-22')");
        db.execSQL (query + "('NOIDA SECTOR-43')");
        db.execSQL (query + "('NOIDA STADIUM')");
        db.execSQL (query + "('NOIDA X-ING')");
        db.execSQL (query + "('OKHLA INDUSTRIES AREA')");
        db.execSQL (query + "('P.G.D.A.V COLLEGE')");
        db.execSQL (query + "('PANDAV NAGAR POLICE STATION')");
        db.execSQL (query + "('PANT NAGAR')");
        db.execSQL (query + "('PARAS CINEMA')");
        db.execSQL (query + "('PATIALA HOUSE')");
        db.execSQL (query + "('PATPARGANJ X-ING')");
        db.execSQL (query + "('PRAGATI MAIDAN')");
        db.execSQL (query + "('PT COLLEGE KOTLA')");
        db.execSQL (query + "('PWD OFFICE')");
        db.execSQL (query + "('RAINY WELL')");
        db.execSQL (query + "('RAJNI GANDHA CHOWL')");
        db.execSQL (query + "('RAMAPRASTH XING')");
        db.execSQL (query + "('RO APARTMENTS')");
        db.execSQL (query + "('ROYAL TOWERS')");
        db.execSQL (query + "('SABJI MANDI BUS STOP')");
        db.execSQL (query + "('SAMASPUR JAGIR VILLAGE')");
        db.execSQL (query + "('SANT NAGAR')");
        db.execSQL (query + "('SARAI KALE KHAN')");
        db.execSQL (query + "('SARITA VIHAR X-LNG')");
        db.execSQL (query + "('SASHI GARDEN X-ING')");
        db.execSQL (query + "('SECTOR 15')");
        db.execSQL (query + "('SECTOR-106')");
        db.execSQL (query + "('SECTOR-18-37')");
        db.execSQL (query + "('SECTOR-62')");
        db.execSQL (query + "('SECTOR-94')");
        db.execSQL (query + "('SEELAM PUR')");
        db.execSQL (query + "('SHAHDARA BORDER')");
        db.execSQL (query + "('SHAHEEN BAGH')");
        db.execSQL (query + "('SHAKAR PUR')");
        db.execSQL (query + "('SUKHDEV MARKET KOTLA')");
        db.execSQL (query + "('SUNDER NAGAR')");
        db.execSQL (query + "('SUPER BAZAR')");
        db.execSQL (query + "('SUPREME COURT')");
        db.execSQL (query + "('SWARN CINEMA')");
        db.execSQL (query + "('TRILOKPURI 13 BLOCK')");
        db.execSQL (query + "('YAMUNA SPORTS COMPLEX')");

    }

    public void populateData_Edges (SQLiteDatabase db) {
        String query = "INSERT INTO " + TABLE_EDGES + " VALUES ";

        db.execSQL (query + "('ANDREWS GANJ', 'PT COLLEGE KOTLA', 0.6)");
        db.execSQL (query + "('ASHRAM', 'MAHARANI BAGH', 1)");
        db.execSQL (query + "('BABARPUR', 'JYOTI COLONY', 0.9)");
        db.execSQL (query + "('BARODA HOUSE', 'PATIALA HOUSE', 0.6)");
        db.execSQL (query + "('BAROLA MARKET', 'SECTOR-106', 3.7)");
        db.execSQL (query + "('BSNL SECTOR-19', 'NOIDA STADIUM', 1.1)");
        db.execSQL (query + "('C-LAL CHOWK', 'OKHLA INDUSTRIES AREA', 0.1)");
        db.execSQL (query + "('CHAND CINEMA', 'SUPER BAZAR', 0.5)");
        db.execSQL (query + "('CROWN PLAZA', 'SARITA VIHAR X-LNG', 2.25)");
        db.execSQL (query + "('DDA FLATS KARKADOMA', 'YAMUNA SPORTS COMPLEX', 2.3)");
        db.execSQL (query + "('EAST ROAD BRIDGE', 'PWD OFFICE', 1.4)");
        db.execSQL (query + "('FORTIS HOSPITAL', 'SECTOR-62', 1)");
        db.execSQL (query + "('GARHI VILLAGE', 'P.G.D.A.V COLLEGE', 0.8)");
        db.execSQL (query + "('GOVIND PURI', 'KALKAJI DEPOT', 0.8)");
        db.execSQL (query + "('GURUDWARA BALA SAHIB', 'SARAI KALE KHAN', 0.95)");
        db.execSQL (query + "('GURUDWARA SRI BANGLA SAHIB', 'PWD OFFICE', 3.9)");
        db.execSQL (query + "('HOUSARY COMPLEX PHASE-2', 'NOIDA PHASE-2', 0.8)");
        db.execSQL (query + "('I.T.O(RING ROAD)', 'RAINY WELL', 1.8)");
        db.execSQL (query + "('ISHWAR NAGAR', 'NEW FRIENDS COLONY', 0.9)");
        db.execSQL (query + "('ITI KHICRIPUR', 'TRILOKPURI 13 BLOCK', 1.1)");
        db.execSQL (query + "('ITO AGCR', 'RAINY WELL', 1.6)");
        db.execSQL (query + "('JAGAT PURI A-BLOCK', 'SWARN CINEMA', 1.9)");
        db.execSQL (query + "('JASOLA VIHAR', 'SHAHEEN BAGH', 0.6)");
        db.execSQL (query + "('JAWAHAR LAL NEHRU STADIUM', 'KHAN MARKET', 2.1)");
        db.execSQL (query + "('JYOTI COLONY', 'NAND NAGAR TERMINAL', 2)");
        db.execSQL (query + "('KALINDI KUNJ', 'SECTOR-94', 2.6)");
        db.execSQL (query + "('KALKA MOR', 'ISHWAR NAGAR', 1)");
        db.execSQL (query + "('KALKAJI DEPOT', 'C-LAL CHOWK', 0.6)");
        db.execSQL (query + "('KALKAJI TEMPLE', 'GOVIND PURI', 0.3)");
        db.execSQL (query + "('KHAN MARKET', 'BARODA HOUSE', 2)");
        db.execSQL (query + "('L.S.R COLLEGE', 'ANDREWS GANJ', 1.5)");
        db.execSQL (query + "('LAXMI NAGAR', 'NIRMAN VIHAR', 2.8)");
        db.execSQL (query + "('MAHARANI BAGH', 'GURUDWARA BALA SAHIB', 1.1)");
        db.execSQL (query + "('MAHARANI BAGH', 'GURUDWARA SRI BANGLA SAHIB', 1.1)");
        db.execSQL (query + "('MAYUR KUNJ', 'SECTOR 15', 1.2)");
        db.execSQL (query + "('MAYUR VIHAR PHASE-1 POCKET-5', 'ITI KHICRIPUR', 0.2)");
        db.execSQL (query + "('MAYUR VIHAR PHASE-1', 'MAYUR KUNJ', 2.7)");
        db.execSQL (query + "('MODI MILL', 'GARHI VILLAGE', 1.7)");
        db.execSQL (query + "('MODI MILLS X-ING', 'KALKA MOR', 1.9)");
        db.execSQL (query + "('MOOL CHAND KHARATI RAM HOSPITAL', 'PANT NAGAR', 2.2)");
        db.execSQL (query + "('N.S.I.C', 'MODI MILLS X-ING', 0.45)");
        db.execSQL (query + "('NEHRU PLACE TERMINAL', 'PARAS CINEMA', 0.4)");
        db.execSQL (query + "('NEHRU PLACE TERMINAL', 'SANT NAGAR', 1)");
        db.execSQL (query + "('NEW FRIENDS COLONY', 'ASHRAM', 1.6)");
        db.execSQL (query + "('NEW RAJDHANI ENCLAVE', 'DDA FLATS KARKADOMA', 2)");
        db.execSQL (query + "('NIRMAN VIHAR', 'NEW RAJDHANI ENCLAVE', 1.1)");
        db.execSQL (query + "('NOIDA SECTOR 12-22', 'ROYAL TOWERS', 3)");
        db.execSQL (query + "('NOIDA SECTOR-43', 'BAROLA MARKET', 2.3)");
        db.execSQL (query + "('NOIDA STADIUM', 'NOIDA SECTOR 12-22', 1.5)");
        db.execSQL (query + "('NOIDA X-ING', 'SAMASPUR JAGIR VILLAGE', 0.4)");
        db.execSQL (query + "('OKHLA INDUSTRIES AREA', 'CROWN PLAZA', 0.5)");
        db.execSQL (query + "('P.G.D.A.V COLLEGE', 'MAHARANI BAGH', 2.1)");
        db.execSQL (query + "('PANDAV NAGAR POLICE STATION', 'SASHI GARDEN X-ING', 0.4)");
        db.execSQL (query + "('PANT NAGAR', 'SUNDER NAGAR', 2.4)");
        db.execSQL (query + "('PARAS CINEMA', 'KALKAJI TEMPLE', 0.7)");
        db.execSQL (query + "('PARAS CINEMA', 'MODI MILL', 1.4)");
        db.execSQL (query + "('PARAS CINEMA', 'N.S.I.C', 0.9)");
        db.execSQL (query + "('PATIALA HOUSE', 'SUPREME COURT', 0.8)");
        db.execSQL (query + "('PATPARGANJ X-ING', 'PANDAV NAGAR POLICE STATION', 0.3)");
        db.execSQL (query + "('PRAGATI MAIDAN', 'I.T.O(RING ROAD)', 2.7)");
        db.execSQL (query + "('PT COLLEGE KOTLA', 'SUKHDEV MARKET KOTLA', 0.7)");
        db.execSQL (query + "('PWD OFFICE', 'MAYUR VIHAR PHASE-1', 3.7)");
        db.execSQL (query + "('PWD OFFICE', 'NOIDA X-ING', 2.1)");
        db.execSQL (query + "('RAINY WELL', 'LAXMI NAGAR', 2.6)");
        db.execSQL (query + "('RAINY WELL', 'SHAKAR PUR', 1.9)");
        db.execSQL (query + "('RAJNI GANDHA CHOWL', 'BSNL SECTOR-19', 1.3)");
        db.execSQL (query + "('RAMAPRASTH XING', 'SHAHDARA BORDER', 3.2)");
        db.execSQL (query + "('RO APARTMENTS', 'FORTIS HOSPITAL', 0.4)");
        db.execSQL (query + "('ROYAL TOWERS', 'SABJI MANDI BUS STOP', 0.9)");
        db.execSQL (query + "('SABJI MANDI BUS STOP', 'RO APARTMENTS', 1.1)");
        db.execSQL (query + "('SAMASPUR JAGIR VILLAGE', 'PATPARGANJ X-ING', 0.8)");
        db.execSQL (query + "('SANT NAGAR', 'L.S.R COLLEGE', 1.7)");
        db.execSQL (query + "('SANT NAGAR', 'MOOL CHAND KHARATI RAM HOSPITAL', 2.7)");
        db.execSQL (query + "('SARAI KALE KHAN', 'EAST ROAD BRIDGE', 1.6)");
        db.execSQL (query + "('SARITA VIHAR X-LNG', 'JASOLA VIHAR', 0.55)");
        db.execSQL (query + "('SASHI GARDEN X-ING', 'MAYUR VIHAR PHASE-1 POCKET-5', 0.3)");
        db.execSQL (query + "('SECTOR 15', 'RAJNI GANDHA CHOWL', 0.6)");
        db.execSQL (query + "('SECTOR-106', 'HOUSARY COMPLEX PHASE-2', 5.6)");
        db.execSQL (query + "('SECTOR-18-37', 'NOIDA SECTOR-43', 3)");
        db.execSQL (query + "('SECTOR-94', 'SECTOR-18-37', 2)");
        db.execSQL (query + "('SEELAM PUR', 'BABARPUR', 2.3)");
        db.execSQL (query + "('SHAHDARA BORDER', 'DILSHAD GARDEN POCKET P', 2.3)");
        db.execSQL (query + "('SHAHEEN BAGH', 'KALINDI KUNJ', 0.45)");
        db.execSQL (query + "('SHAKAR PUR', 'JAGAT PURI A-BLOCK', 3.6)");
        db.execSQL (query + "('SUKHDEV MARKET KOTLA', 'JAWAHAR LAL NEHRU STADIUM', 1.1)");
        db.execSQL (query + "('SUNDER NAGAR', 'PRAGATI MAIDAN', 2.2)");
        db.execSQL (query + "('SUPER BAZAR', 'KALYANPURI', 0.4)");
        db.execSQL (query + "('SUPREME COURT', 'ITO AGCR', 1.2)");
        db.execSQL (query + "('SWARN CINEMA', 'SEELAM PUR', 2.7)");
        db.execSQL (query + "('TRILOKPURI 13 BLOCK', 'CHAND CINEMA', 0.24)");
        db.execSQL (query + "('YAMUNA SPORTS COMPLEX', 'RAMAPRASTH XING', 1)");

    }

    public ArrayList<Vertice> getData_Vertice () {
        ArrayList<Vertice> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery ("SELECT * " +
                "FROM " + TABLE_VERTICES, null);

        while (cursor.moveToNext()) {
            Vertice vert = new Vertice (cursor.getString (cursor.getColumnIndex ("vertex_name")));
            data.add (vert);
        }

        cursor.close();

        return data;
    }

    public ArrayList<Edges> getData_Edges () {
        ArrayList<Edges> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery ("SELECT * " +
                "FROM " + TABLE_EDGES, null);

        while (cursor.moveToNext()) {
            Edges edge = new Edges (cursor.getString (cursor.getColumnIndex ("vertex_1")),
                                    cursor.getString (cursor.getColumnIndex ("vertex_2")),
                                    cursor.getDouble (cursor.getColumnIndex ("weight")));
            data.add (edge);
        }

        cursor.close();

        return data;
    }
}
