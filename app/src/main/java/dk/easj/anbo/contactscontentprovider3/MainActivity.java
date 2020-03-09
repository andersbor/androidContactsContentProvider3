package dk.easj.anbo.contactscontentprovider3;

import android.Manifest;
import android.content.CursorLoader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageView = findViewById(R.id.mainMessageTextView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Android 6 requires permission in manifest + at runtime
        // https://developer.android.com/training/permissions/requesting.html
        // http://stackoverflow.com/questions/35572694/android-6-0-marshmallow-read-contacts-permission-allows-to-read-contacts-name


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            messageView.setText("Need permission: Read contacts");
            return;
        }

        //Uri allContacts = Uri.parse("content://contacts/people");
        Uri allContacts = ContactsContract.Contacts.CONTENT_URI;

        String[] columns = { // aka projection
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        };

        String selection = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " like ?";
        String[] selectionArgs = {"An%"};
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC";
        // https://stackoverflow.com/questions/51408098/what-is-the-appropriate-replacer-of-deprecated-getsupportloadermanager
        CursorLoader cursorLoader =
                new CursorLoader(this, allContacts, columns, selection, selectionArgs, sortOrder);
        // select ID, DISPLAY_NAME from contacts where DISPLAY NAME like 'An%' order by DISPLAY_NAME asc;
        Cursor cursor = cursorLoader.loadInBackground();
        int[] views = {R.id.list_item_contactID, R.id.list_item_contactName};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item_layout, cursor,
                columns, views, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ListView listView = findViewById(R.id.mainListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), "Yoy clicked no. " + id, Toast.LENGTH_LONG).show();
                Log.d("CONTACT", "You clicked no. " + id);
            }
        });
    }

}
