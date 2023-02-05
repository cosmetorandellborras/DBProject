package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyOpenHelper dbHelper;
    private EditText commentTitleEdit;
    private EditText commentTextEdit;
    private TextView commentTitle;
    private TextView commentText;
    private Button createComment;
    private Button seeComment;
    private Button deleteComment;
    private Spinner spinner;

    private ArrayList<Comentari> comments = new ArrayList<>();
    private ArrayList<String> commentsTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyOpenHelper(this);
        commentTitleEdit = (EditText) findViewById(R.id.newComentTitleEditText);
        commentTextEdit = (EditText) findViewById(R.id.newCommentEditText);
        commentTitle = (TextView) findViewById(R.id.seeCommentTitleTextView);
        commentText = (TextView) findViewById(R.id.commentText);
        createComment = (Button) findViewById(R.id.createButton);
        seeComment = (Button) findViewById(R.id.seeButton);
        deleteComment = (Button) findViewById(R.id.deleteButton);
        spinner = (Spinner) findViewById(R.id.spinner);

        comments = dbHelper.getComments();
        updateSpinner();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,commentsTitles);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        createComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = commentTitleEdit.getText().toString();
                String comment = commentTextEdit.getText().toString();

                if(!title.equals("") && !comment.equals("")){
                    Comentari comentari = new Comentari();
                    comentari.setTitle(title);
                    comentari.setText(comment);
                    dbHelper.addComment(comentari);
                    updateSpinner();
                }
                commentTitleEdit.setText("");
                commentTextEdit.setText("");
            }
        });
        seeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItem() != null){
                    String title = spinner.getSelectedItem().toString();
                    int i = 0;
                    while(i<comments.size()){
                        if(comments.get(i).getTitle().equals(title)){
                            commentTitle.setText(comments.get(i).getTitle());
                            commentText.setText(comments.get(i).getText());
                            i = comments.size();
                        }i++;
                    }
                }
            }
        });
        deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItem() != null){
                    String title = commentTitle.getText().toString();
                    dbHelper.removeComment(title);
                    updateSpinner();
                    commentTitle.setText("");
                    commentText.setText("");
                }
            }
        });
    }
    private void updateSpinner(){
        comments.clear();
        comments = dbHelper.getComments();
        updateCommentsTitles();
    }
    private void updateCommentsTitles(){
        commentsTitles.clear();
        for(int i=0;i<comments.size();i++){
            commentsTitles.add(comments.get(i).getTitle());
        }
    }
}