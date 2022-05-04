package com.tambor.samples;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.tambor.samples.contract.ApplicationComponent;
import com.tambor.samples.contract.DaggerUserComponent;
import com.tambor.samples.contract.UserComponent;
import com.tambor.samples.database.models.User;
import com.tambor.samples.databinding.ActivityMainBinding;
import com.tambor.samples.module.UserModule;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private UserComponent userComponent;
    private User user;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        userComponent = DaggerUserComponent.builder()
                .applicationComponent(getApplicationComponent())
                .userModule(new UserModule()).build();
        user = userComponent.providerUser();
        userComponent.inject(user);

        //Settings session
        user.setUserName("Jairo de Almeida");
        //Toast.makeText(this,"User: " + user.getUserName(),Toast.LENGTH_SHORT).show();
        /*View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout,
                "User: " + user.getUserName(),
                Snackbar.LENGTH_LONG)
                .setAction("Info", null).show();*/
        setupPrefSettings(user);
        showMessage("User: " + user.getUserName());
    }
    private void showMessage(String message){
        View parentLayout = binding.getRoot();
        Snackbar snack = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);
        snack.setAction(R.string.information, null);
        View view = snack.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }
    private void setupPrefSettings(User user){
        pref = userComponent.providePreferences();
        pref.edit().putString(User.SESSION_KEY, user.getFullName()).apply();
    }
    private ApplicationComponent getApplicationComponent(){
        return ((AndroidApplication)getApplication()).getApplicationComponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}