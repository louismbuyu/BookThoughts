package com.example.louisnelsonlevoride.bookthoughts.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.louisnelsonlevoride.bookthoughts.Books.Quotes.AddEditQuoteActivity;
import com.example.louisnelsonlevoride.bookthoughts.Books.Quotes.QuotesActivity;
import com.example.louisnelsonlevoride.bookthoughts.R;

public class EditProfileActivity extends AppCompatActivity {

    EditText displayNameEditText;
    Button doneBtn;
    String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle(getString(R.string.edit_profile));
        displayNameEditText = findViewById(R.id.edit_profile_displayname);
        String tempDisplayName = getIntent().getStringExtra("displayName");
        if(tempDisplayName!=null){
            displayName = tempDisplayName;
            displayNameEditText.setText(displayName);
        }

        doneBtn = findViewById(R.id.edit_profile_done_btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doneAction();
            }
        });
    }

    private void doneAction(){
        if(displayNameEditText.getText() != null){
            if (!displayNameEditText.getText().toString().isEmpty()){
                displayName = displayNameEditText.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("displayName",displayName);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
