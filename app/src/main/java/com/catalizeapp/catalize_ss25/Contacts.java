package com.catalizeapp.catalize_ss25;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentResolver;

//import com.google.firebase.FirebaseApp;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.FirebaseStorage;

public class Contacts extends AppCompatActivity {
    Context context = null;
    //public final View activityRootView2 = findViewById(R.id.root);
    public static String person1 = "";
    public static String person2 = "";
    public static String number1 = "";
    public static String number2 = "";
    boolean flag = false;
    public static boolean changed = false;
    public static boolean keyboard = false;
    boolean ok = false;
    public static boolean newbie = false;
    public static String newContact = "";
    ContactsAdapter objAdapter;
    ActionMenuItemView searchView2;
    public static SearchView searchView;
    ListView lv = null;
    LinearLayout llContainer = null;
    Button btnOK = null;
    RelativeLayout rlPBContainer = null;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        context = this;

        sharedPreferences = this.getSharedPreferences("com.catalizeapp.catalize_ss25", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_contacts);
        rlPBContainer = (RelativeLayout) findViewById(R.id.pbcontainer);
        llContainer = (LinearLayout) findViewById(R.id.data_container);
        btnOK = (Button) findViewById(R.id.connect);
        btnOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //keyboard = false;
                final View activityRootView = findViewById(R.id.root);
                activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        //r will be populated with the coordinates of your view that area still visible.
                        activityRootView.getWindowVisibleDisplayFrame(r);

                        int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                        if (heightDiff > 30) { // if more than 100 pixels, its probably a keyboard...
                            keyboard = true;
                        }
                    }
                });
                searchView.clearFocus();
                searchView.setQuery("", false);
                getSelectedContacts();
            }
        });
        if (ContextCompat.checkSelfPermission(Contacts.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Contacts.this,
                    Manifest.permission.READ_CONTACTS)) {

            } else {

                ActivityCompat.requestPermissions(Contacts.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        0);
            }
        }
        addContactsInList();
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Contacts.this, Contacts.class);
            startActivity(intent);
        } else {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.denied, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    flag = true;
                                    ok = true;
                                    Intent intentLogOut = new Intent(Contacts.this, LoginActivity.class);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    startActivity(intentLogOut);
                                    finish();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
        return;
    }

    private void getSelectedContacts() {
        number1 = "";
        number2 = "";
        person1 = "";
        person2 = "";
        newbie = false;
        ok = true;

        int total = 0;
        StringBuffer sb = new StringBuffer();
        for (ContactObject bean : ContactsListClass.phoneList) {
            if (bean.isSelected()) {
                total++;
            }
        }

        if (total ==0){
            newbie = false;
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.two, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    flag = true;
                                    ok = true;
                                    // get user input and set it to result
                                    // edit text

                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();

        } else if (total == 1) {

            final Dialog dialog = new Dialog(Contacts.this);
            //setting custom layout to dialog
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before

            dialog.setContentView(R.layout.addnumber);

            //adding text dynamically
            final EditText newperson = (EditText) dialog.findViewById(R.id.newperson);
            final EditText newname = (EditText) dialog.findViewById(R.id.newname);

            //adding button click event

            Button dismissButton = (Button) dialog.findViewById(R.id.buttonback);
            dismissButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }

            });
            Button enter = (Button) dialog.findViewById(R.id.next);
            enter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (newname.getText().toString() == "" || newperson.getText().toString() == "") {
                        Toast.makeText(context, "Please fill in both fields.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        person2 = newname.getText().toString();
                        number2 = newperson.getText().toString();
                        newbie = true;

                        startActivityForResult(new Intent(Contacts.this, Account.class), 10);
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        } else if (total != 2) {
            newbie = false;
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.three, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    flag = true;
                                    //ok = true;
                                    // get user input and set it to result
                                    // edit text
                                    CheckBox cb;
                                    for(int i=0; i<lv.getChildCount();i++)
                                    {
                                        cb = (CheckBox)lv.getChildAt(i).findViewById(R.id.contactcheck);
                                        cb.setChecked(false);
                                    }

                                }
                            });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }

        if (total == 2 || newbie) {
            for (ContactObject bean : ContactsListClass.phoneList) {
                if (bean.isSelected()) {
                    bean.setSelected(false);
                    sb.append(bean.getName());
                    if (number1 == "") {
                        number1 = bean.getNumber();
                    } else {
                        if (!newbie) {
                            number2 = bean.getNumber();
                        }
                    }
                    sb.append(",");
                    if (person1 == "") {
                        person1 = bean.getName();
                    } else {
                        if (!newbie) {
                            person2 = bean.getName();
                        }
                    }
                }
            }
            CheckBox cb;
            for(int i=0; i<lv.getChildCount();i++)
            {
                cb = (CheckBox)lv.getChildAt(i).findViewById(R.id.contactcheck);
                cb.setChecked(false);
            }
            startActivityForResult(new Intent(Contacts.this, Account.class), 10);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        searchView.clearFocus();
        searchView.setQuery("", false);
        return true;
    }

    private void addContactsInList() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                showPB();
                try {
                    Cursor cEmail = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            null, null, null);

                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, null, null, null);
                    try {
                        ContactsListClass.phoneList.clear();
                    } catch (Exception e) {
                    }
                    while (phones.moveToNext()) {
                        String phoneName = phones
                                .getString(phones
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNumber = phones
                                .getString(phones
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String phoneImage = phones
                                .getString(phones
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                        ContactObject cp = new ContactObject();

                        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
                        cp.setName(phoneName);
                        cp.setNumber(phoneNumber);
                        cp.setImage(phoneImage);
                        boolean found = false;

                        for (ContactObject o : ContactsListClass.phoneList) {
                            if (o.getNumber().equals(cp.getNumber())) {
                                found = true;
                            }
                        }
                        if (!found) {
                            ContactsListClass.phoneList.add(cp);
                        }
                    }
                    while (cEmail.moveToNext()) {
                        if (cEmail.getCount() > 0) {
                            String email = cEmail
                                    .getString(cEmail
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            ContactObject cp2 = new ContactObject();
                            cp2.setName(email);
                            cp2.setNumber("");
                            cp2.setImage(null);
                            ContactsListClass.phoneList.add(cp2);
                            //Toast.makeText(context, email,
                            //              Toast.LENGTH_SHORT).show();
                            //break;
                        }
                    }
                    phones.close();
                    cEmail.close();
                    //Toast.makeText(context, ContactsListClass.phoneList.size(),
                      //                    Toast.LENGTH_LONG).show();
                    if (ContactsListClass.phoneList.size() < 1) {
                        Toast.makeText(context, "hi",
                                                    Toast.LENGTH_LONG).show();
                       /* if (ContextCompat.checkSelfPermission(Contacts.this,
                                Manifest.permission.READ_CONTACTS)
                                != PackageManager.PERMISSION_GRANTED) {

                            if (ActivityCompat.shouldShowRequestPermissionRationale(Contacts.this,
                                    Manifest.permission.READ_CONTACTS)) {

                            } else {
                                ActivityCompat.requestPermissions(Contacts.this,
                                        new String[]{Manifest.permission.READ_CONTACTS},
                                        0);
                            }
                        }else {
                            Intent intent = new Intent(Contacts.this, Contacts.class);
                            startActivity(intent);
                        }*/
                    }

                    lv = new ListView(context);
                    lv.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            llContainer.addView(lv);
                        }
                    });
                    Collections.sort(ContactsListClass.phoneList,
                            new Comparator<ContactObject>() {
                                @Override
                                public int compare(ContactObject lhs,
                                                   ContactObject rhs) {
                                    return lhs.getName().compareTo(
                                            rhs.getName());
                                }
                            });

                    objAdapter = new ContactsAdapter(Contacts.this,
                            ContactsListClass.phoneList);
                    lv.setAdapter(objAdapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            final CheckBox chk = (CheckBox) view
                                    .findViewById(R.id.contactcheck);

                            ContactObject bean = ContactsListClass.phoneList
                                    .get(position);
                            if (bean.isSelected()) {
                                bean.setSelected(false);
                                chk.setChecked(false);
                            } else {
                                bean.setSelected(true);
                                chk.setChecked(true);
                            }
                        }
                    });
                    if (changed) {
                        //lv.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    //Toast.makeText(context, "10",
                      //      Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                hidePB();
            }
        };
        thread.start();
    }

    void showPB() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                rlPBContainer.setVisibility(View.VISIBLE);
                btnOK.setVisibility(View.GONE);
            }
        });
    }

    void hidePB() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                rlPBContainer.setVisibility(View.GONE);
                btnOK.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        /*CheckBox repeatChkBx = ( CheckBox ) findViewById( R.id.contactcheck );
        repeatChkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    searchView.clearFocus();
                    searchView.setQuery("", false);
                }
            }
        });*/

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                newText.toLowerCase(Locale.getDefault());
                objAdapter.filter(newText);
                flag = true;
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                searchView.setQuery("", false);
                searchView.clearFocus();
                //Here u can get the value "query" which is entered in the search box.
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case(R.id.menu_1):
                Intent intentReportBug = new Intent(Contacts.this, ReportBug.class); //
                startActivity(intentReportBug);
                break;
            case(R.id.menu_2):
                Intent intentLogOut = new Intent(Contacts.this, LoginActivity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(intentLogOut);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
