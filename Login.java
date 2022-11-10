package com.example.pinkcal;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {
	EditText password;
	CheckBox checkbox;

	private static final int RC_SIGN_IN = 100;
	private GoogleSignInClient googleSignInClient;

	private FirebaseAuth firebaseAuth;

	private static final String TAG = "GOOGLE_SIGN_IN_TAG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Configure the Google SignIn
		GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken("481869298056-mglrvjg8ogkpka9hfarotth5lhasu509.apps.googleusercontent.com").requestEmail()
				.build();

		googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

		// init firebase auth
		firebaseAuth = FirebaseAuth.getInstance();

		// Show - Hide Password
		password = findViewById(R.id.password);
		checkbox = findViewById(R.id.checkbox);

		checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					password.setSelection(password.getText().length());
				} else {
					password.setTransformationMethod(PasswordTransformationMethod.getInstance());
					password.setSelection(password.getText().length());
				}
			}
		});

	}

	// Go login
	public void Logup(View view) {
		// TODO move to google SignIn Button
		// Log.d(TAG, "onClick: beging google signin");
		// Intent intent = googleSignInClient.getSignInIntent();
		// startActivityForResult(intent, RC_SIGN_IN);
		Intent registro = new Intent(Login.this, RegisterUsers.class);
		startActivity(registro);
	}

	// Go MainActivity
	public void MainActivity(View view) {
		Intent MainActivity = new Intent(Login.this, MainActivity.class);
		startActivity(MainActivity);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			Log.d(TAG, "onActivityResult: Google SignIn intent result");
			Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				// auth with firebase
				GoogleSignInAccount account = accountTask.getResult(ApiException.class);
				firebaseAuthWithGoogleAccount(account);
				Intent MainActivity = new Intent(Login.this, MainActivity.class);
				startActivity(MainActivity);
			} catch (Exception e) {
				Log.d(TAG, "onActivityResult: " + e.getMessage());
			}
		}
	}

	private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
		Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");
		AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
		firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
			@Override
			public void onSuccess(AuthResult authResult) {

				Log.d(TAG, "onSuccess: Logged");

				// get logged in user
				FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

				// get user info
				String uid = firebaseUser.getUid();
				String email = firebaseUser.getEmail();

				Log.d(TAG, "onSuccess: Email " + email);
				Log.d(TAG, "onSuccess: Uid " + uid);

				// check if user is new or existing
				if (authResult.getAdditionalUserInfo().isNewUser()) {
					// user is new
					Log.d(TAG, "onSuccess: Account Created" + email);
					Toast.makeText(Login.this, "Account Created" + email, Toast.LENGTH_SHORT).show();
				} else {
					// existing user
					Log.d(TAG, "onSuccess: existing user" + email);
					Toast.makeText(Login.this, "existing user" + email, Toast.LENGTH_SHORT).show();

				}
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.d(TAG, "onFailure: LoggIn failed " + e.getMessage());
			}
		});
	}
}
