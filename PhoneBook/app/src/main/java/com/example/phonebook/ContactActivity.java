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

import org.w3c.dom.Text;

public class ContactActivity extends AppCompatActivity {
    private EditText etName, etPhoneNum;
    private Button btnDelete, btnEdit, btnSave, btnCancel;
    private boolean changed, clickedEdit = false;
    private DataBaseHelper dh = new DataBaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initViews();

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

        Intent intent = getIntent();
        if (null != intent) {
            int id = intent.getIntExtra("id", -1);
            if (id != -1)   {
                Contact contact = dh.getContactById(id);
                if (null != contact)    {
                    setData(contact);
                    String name = contact.getName();
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ContactActivity.this);
                            builder.setMessage("Do you really want to delete "+name+" ?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   if (dh.deleteContact(id))    {
                                        returnMain();
                                        Toast.makeText(ContactActivity.this, "Deleted " + name, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.create().show();
                        }
                    });

                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changed = false;
                            clickedEdit = true;
                            etName.setEnabled(true);
                            etPhoneNum.setEnabled(true);
                            btnEdit.setEnabled(false);
                            btnSave.setVisibility(View.VISIBLE);
                            btnCancel.setVisibility(View.VISIBLE);

                            btnSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ContactActivity.this);
                                    builder.setMessage("Do you want to save changes?");
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (dh.editContact(contact.getId(), etName.getText().toString(), etPhoneNum.getText().toString()))  {
                                                btnSave.setVisibility(View.GONE);
                                                btnCancel.setVisibility(View.GONE);
                                                btnEdit.setEnabled(true);
                                                etName.setEnabled(false);
                                                etPhoneNum.setEnabled(false);

                                                changed = true;
                                                clickedEdit = false;
                                            }
                                        }
                                    });
                                    builder.create().show();
                                }
                            });

                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    etName.setText(contact.getName());
                                    etPhoneNum.setText(contact.getPhoneNumber());

                                    btnSave.setVisibility(View.GONE);
                                    btnCancel.setVisibility(View.GONE);
                                    btnEdit.setEnabled(true);
                                    etName.setEnabled(false);
                                    etPhoneNum.setEnabled(false);

                                    clickedEdit = false;
                                }
                            });
                        }
                    });
                }
            }
        }

    }

    private void setData(Contact contact) {
        etName.setText(contact.getName());
        etPhoneNum.setText(contact.getPhoneNumber());
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etPhoneNum = findViewById(R.id.etPhoneNum);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void returnMain()   {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (clickedEdit)    {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please Save Changes!");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }else if (changed)    {
            returnMain();
        }else   {
            super.onBackPressed();
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}