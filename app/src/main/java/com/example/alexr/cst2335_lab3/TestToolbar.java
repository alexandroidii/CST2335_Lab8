package com.example.alexr.cst2335_lab3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "1234 TestToolbar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(findViewById(R.id.testToolbarview), "Menu item 1 was selected... yummy!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.cameraMenuItem:
                Log.i(ACTIVITY_NAME, "Camera menu item selected");
                Snackbar.make(findViewById(R.id.testToolbarview), "mmmm snackbar notifications are tasty", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.shoppingCartMenuItem:
                Log.i(ACTIVITY_NAME, "Shopping Cart menu item selected");
                DialogFragment pizzaOrder = new WannaOrderPizzaDialogFragment();
                pizzaOrder.show(getSupportFragmentManager(), "Zaaaa");
                break;
            case R.id.musicMenuItem:
                Log.i(ACTIVITY_NAME,"Music menu item selected");
                DialogFragment foodPreference = new SnackbarMessageFragment();
                foodPreference.show(getSupportFragmentManager(), "yummm");

                break;
            case R.id.aboutMenutItem:
                Log.i(ACTIVITY_NAME, "About menu item selected");
                CharSequence versionInfo = "Version 1.0 by Alexander Riccio";
                Toast toast = Toast.makeText(this, versionInfo, Toast.LENGTH_LONG);
                toast.show();
                break;
        }
        return true;
    }

}
