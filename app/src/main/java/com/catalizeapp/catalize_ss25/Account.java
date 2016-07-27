package com.catalizeapp.catalize_ss25;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;


public class Account extends AppCompatActivity {

    boolean cancel = false;
    Context context = null;
    boolean flag = false;
    private String result;
    private String result2;
    private SharedPreferences sharedPreferences;
    private String firstName;
    private String lastName;
    private String personEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        context = this;
        sharedPreferences = this.getSharedPreferences("com.catalizeapp.catalize_ss25", Context.MODE_PRIVATE);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Your toolbar is now an action bar and you can use it like you always do, for example:
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //final Dialog dialog = new Dialog(Account.this);
        final EditText prompt = (EditText) findViewById(R.id.prompt);
        if (Contacts.person1.contains("@") && Contacts.person2.contains("@")) {
            TextView name = (TextView) findViewById(R.id.nameerror);
            //name.setText("Set a name for " + Contacts.person1 + ": ");
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_prompts, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);
            final EditText userInput2 = (EditText) promptsView
                    .findViewById(R.id.input2);

            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    flag = true;
                                    // get user input and set it to result
                                    // edit text
                                    result = userInput.getText().toString();
                                    result2 = userInput2.getText().toString();
                                    prompt.append("Hello " + result + ", meet " + result2 + ". I am introducing you two because ");
                                    //prompt.setText("Hello " + result + ", meet " + result2 + ". I am introducing you two because...");
                                    //Contacts.person1 = result;
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    cancel = true;
                                    startActivityForResult(new Intent(Account.this, Contacts.class), 10);
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        } else if (Contacts.person1.contains("@") && !Contacts.person2.contains("@") || Contacts.person2.contains("@") && !Contacts.person1.contains("@")) {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_prompt, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    flag = true;
                                    // get user input and set it to result
                                    // edit text
                                    result = userInput.getText().toString();
                                    if (Contacts.person1.contains("@")) {
                                        prompt.append("Hello " + result + ", meet " + Contacts.person2 + ". I am introducing you two because ");
                                        //prompt.setText("Hello " + result + ", meet " + Contacts.person2 + ". I am introducing you two because...");
                                    } else {

                                        prompt.append("Hello " + Contacts.person1 + ", meet " + result + ". I am introducing you two because ");
                                    }
                                    //Contacts.person1 = result;
                                }
                            });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        } else {
            prompt.append("Hello " + Contacts.person1 + ", meet " + Contacts.person2 + ". I am introducing you two because ");
        }

        if (cancel) {
            cancel = false;
            Toast.makeText(context, "HI",
                    Toast.LENGTH_SHORT).show();
        }
        final Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        final EditText et=(EditText)findViewById(R.id.prompt);
        final TextView people = (TextView) findViewById(R.id.people);
        Contacts.number2 = Contacts.number2.replaceAll("[^0-9]","");
        Contacts.number1 = Contacts.number1.replaceAll("[^0-9]","");
        //people.setText(Contacts.people);

        //Toast.makeText(context, Contacts.numbers,
        //      Toast.LENGTH_SHORT).show();
        //sendIntent.putExtra(et.getText().toString(), "default content");
        //sendIntent.setType("vnd.android-dir/mms-sms");

        Button send = (Button) findViewById(R.id.send);
       firstName = sharedPreferences.getString("first_name","");
        lastName = sharedPreferences.getString("last_name","");

        personEmail  = sharedPreferences.getString("email", "");
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    SmsManager.getDefault().sendTextMessage("9154719427 ", null, "Introduction made by: "+ firstName + " " + lastName + " " + personEmail+ "\n"+ "Contacts: " + Contacts.person1 + ": " + Contacts.number1 + "\n" + Contacts.person2 + " " + Contacts.number2 + "\n" + et.getText().toString(), null, null);
                    SmsManager.getDefault().sendTextMessage("2013751471 ", null, "Introduction made by: "+ firstName + " " + lastName + " " + personEmail+ "\n"+ "Contacts: " + Contacts.person1 + ": " + Contacts.number1 + "\n" + Contacts.person2 + " " + Contacts.number2 + "\n" + et.getText().toString(), null, null);
                    //SmsManager.getDefault().sendTextMessage("2013751471 ", null, Contacts.number1 + "\n" + Contacts.number2, null, null);

                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.sent, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    alertDialogBuilder.setView(promptsView);

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // get user input and set it to result
                                            // edit text
                                            finish();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();

                    //SmsManager.getDefault().sendTextMessage(Contacts.number2, null, "Hello " + Contacts.person2 + "! You've recieved an introduction from " + personName + " through Catalize, the networking and connections app:", null, null);
                    //SmsManager.getDefault().sendTextMessage(Contacts.number2, null, et.getText().toString(), null, null);
                    //SmsManager.getDefault().sendTextMessage(Contacts.number2, null, "Respond to this message to continue the conversation.", null, null);

                    //SmsManager.getDefault().sendTextMessage(Contacts.number1, null, "Hello " + Contacts.person1 + "! You've recieved an introduction from " + personName + " through Catalize, the networking and connections app:", null, null);
                    //SmsManager.getDefault().sendTextMessage(Contacts.number1, null, et.getText().toString(), null, null);
                    //SmsManager.getDefault().sendTextMessage(Contacts.number1, null, "Respond to this message to continue the conversation.", null, null);


                    try
                    {
                        Thread.sleep(000);//1sec
                    }
                    catch(InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }

                    //SmsManager.getDefault().sendTextMessage(Contacts.number2, null, "Meenal says: Hey " + Contacts.person2 +  "! Thanks for stopping by the Catalize booth at CreateX Product Day at Georgia Tech!", null, null);
                    //SmsManager.getDefault().sendTextMessage(Contacts.number2, null, "Want to know  more about us? Check us out at catalizeapp.com." + "\n" + "Happy connecting!", null, null);

                } catch (Exception e) {
                    AlertDialog.Builder alertDialogBuilder = new
                            AlertDialog.Builder(Account.this);
                    AlertDialog dialog = alertDialogBuilder.create();


                    dialog.setMessage(e.getMessage());


                    dialog.show();
                    startActivity(sendIntent);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
