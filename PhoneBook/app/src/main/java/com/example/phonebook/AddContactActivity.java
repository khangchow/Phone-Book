package com.example.phonebook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddContactActivity extends AppCompatActivity {
    private EditText etName, etPhoneNum;
    private Button btnAdd;
    private TextView txtWarnName, txtWarnNum;
    private DataBaseHelper dh = new DataBaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        initViews();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().equals("")) {
                    txtWarnName.setText("Please enter Name!");
                    txtWarnName.setVisibility(View.VISIBLE);
                    etName.requestFocus();
                }else   {
                    txtWarnName.setVisibility(View.GONE);
                    etName.setFocusable(false);
                }

                if (etPhoneNum.getText().toString().equals("")) {
                    txtWarnNum.setText("Please enter Phone Number!");
                    txtWarnNum.setVisibility(View.VISIBLE);
                    if(!etName.isFocused())   {
                        etPhoneNum.requestFocus();
                    }
                }else   {
                    txtWarnNum.setVisibility(View.GONE);
                }

                if (!(etName.getText().toString().equals("")) && !(etPhoneNum.getText().toString().equals(""))) {
                    String name = etName.getText().toString();
                    String number = etPhoneNum.getText().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
                    builder.setMessage("Are you sure to add "+name+" to your phone book?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            if (Utils.getInstance().addNewContact(new Contact(id, name, number))) {
//                                returnMain();
//                                Toast.makeText(AddContactActivity.this, "Added "+name, Toast.LENGTH_SHORT).show();
//                            }
                            if (dh.addContact(new Contact(-1, name, number)))   {
                                returnMain();
                                Toast.makeText(AddContactActivity.this, "Added "+name, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.create().show();
                }else {
                    Toast.makeText(AddContactActivity.this, "Please fill all information", Toast.LENGTH_SHORT).show();
                }
            }

        });

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)  {
                    hideKeyboard(v);
                }
            }
        });

        etPhoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)  {
                    hideKeyboard(v);
                }
            }
        });
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etPhoneNum = findViewById(R.id.etPhoneNum);
        btnAdd = findViewById(R.id.btnAddNew);

        txtWarnName = findViewById(R.id.txtWarnName);
        txtWarnNum = findViewById(R.id.txtWarnNum);
    }

    private void returnMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}