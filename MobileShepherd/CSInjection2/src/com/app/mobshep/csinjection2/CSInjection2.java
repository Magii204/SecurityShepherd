package com.app.mobshep.csinjection2;

import java.io.File;
import java.io.IOException;

import net.sqlcipher.database.SQLiteDatabase;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class CSInjection2 extends Activity implements OnClickListener {

	TabHost th;
	Button Login;
	EditText username;
	EditText password;
	EditText key;
	String dbPassword = "P93Eid3D33DE0ZanbffGpo101Sirjw2";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.csi);
		th = (TabHost) findViewById(R.id.tabhost);
		populateTable(this, dbPassword);
		referenceXML();
		th.setup();

		TabSpec specs = th.newTabSpec("tag1");
		specs.setContent(R.id.tab1);
		specs.setIndicator("Login");
		th.addTab(specs);

		specs = th.newTabSpec("tag2");
		specs.setContent(R.id.tab2);
		specs.setIndicator("Key");
		th.addTab(specs);
	}

	private void referenceXML() {
		// TODO Auto-generated method stub
		Login = (Button) findViewById(R.id.bLogin);
		// Login.setFilterTouchesWhenObscured(true);
		username = (EditText) findViewById(R.id.etName);
		password = (EditText) findViewById(R.id.etPass);
		key = (EditText) findViewById(R.id.etKey);
		Login.setOnClickListener(this);

	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case (R.id.bLogin):

			String unsanitizeName = username.getText().toString();
			String unsanitizePass = password.getText().toString();

			String sanitizeName = unsanitizeName.replace("OR", "/* */");
			sanitizeName = sanitizeName.replace("or", "/* */");
			sanitizeName = sanitizeName.replace("SELECT", "/* */");
			sanitizeName = sanitizeName.replace("AND", "/* */");
			sanitizeName = sanitizeName.replace("UPDATE", "/* */");
			sanitizeName = sanitizeName.replace("DROP", "/* */");
			sanitizeName = sanitizeName.replace("1=1", "/* */");
			sanitizeName = sanitizeName.replace("1 = 1", "/* */");

			String sanitizePass = unsanitizePass.replace("OR", "/* */");
			sanitizePass = sanitizePass.replace("or", "/* */");
			sanitizePass = sanitizePass.replace("SELECT", "/* */");
			sanitizePass = sanitizePass.replace("AND", "/* */");
			sanitizePass = sanitizePass.replace("UPDATE", "/* */");
			sanitizePass = sanitizePass.replace("DROP", "/* */");
			sanitizePass = sanitizePass.replace("1=1", "/* */");
			sanitizePass = sanitizePass.replace("1 = 1", "/* */");

			try {
				if (login(sanitizeName, sanitizePass) == true) {
					key.setText("The Key is: SourHatsAndAngryCats.");
					Toast toast = Toast.makeText(CSInjection2.this,
							"Logged in as:" + sanitizeName, Toast.LENGTH_LONG);
					toast.show();

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				if (login(sanitizeName, sanitizePass) == false) {
					Toast toast = Toast.makeText(CSInjection2.this,
							"Invalid Credentials, " + sanitizeName,
							Toast.LENGTH_LONG);
					toast.show();

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (sanitizeName.contentEquals("")
					|| sanitizePass.contentEquals("")) {
				Toast toast2 = Toast.makeText(CSInjection2.this,
						"Empty Fields Detected.", Toast.LENGTH_SHORT);
				toast2.show();

			}

		}

	}

	private boolean login(String username, String password) throws IOException {

		try {
			String dbPath = this.getDatabasePath("Users.db").getPath();

			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,
					dbPassword, null);

			String query = ("SELECT * FROM Users WHERE memName='" + username
					+ "' AND memPass = '" + password + "';");

			Cursor cursor = db.rawQuery(query, null);
			if (cursor != null) {
				if (cursor.getCount() <= 0) {
					return false;

				}
			}

		} catch (SQLiteException e) {
			Toast error = Toast.makeText(CSInjection2.this,
					"An database error occurred.", Toast.LENGTH_LONG);
			error.show();
		}

		return true;

	}

	public void populateTable(Context context, String dbpassword) {
		try {

			try {
				SQLiteDatabase.loadLibs(context);

				String dbPath = context.getDatabasePath("Users.db").getPath();

				File dbPathFile = new File(dbPath);
				if (!dbPathFile.exists())
					dbPathFile.getParentFile().mkdirs();

				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,
						dbpassword, null);

				db.execSQL("DROP TABLE IF EXISTS Users");
				db.execSQL("CREATE TABLE Users(memID INTEGER PRIMARY KEY AUTOINCREMENT, memName TEXT, memAge INTEGER, memPass VARCHAR)");

				db.execSQL("INSERT INTO Users VALUES( 1,'Admin',20,'49c3a17ad8d8ccd93885e6a28661480d')");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast error = Toast.makeText(CSInjection2.this,
						"An error occurred.", Toast.LENGTH_LONG);
				error.show();

			}

		} catch (SQLiteException e) {
			Toast error = Toast.makeText(CSInjection2.this,
					"An database error occurred.", Toast.LENGTH_LONG);
			error.show();
		}
	}
}
