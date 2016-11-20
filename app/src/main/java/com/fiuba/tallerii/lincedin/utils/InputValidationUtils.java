package com.fiuba.tallerii.lincedin.utils;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.fiuba.tallerii.lincedin.R;

import java.util.regex.Pattern;

public class InputValidationUtils {

    private static final String TAG = "InputValidation";

    public static boolean validateThatAllFieldsAreFilled(Context context, EditText... fields) {
        boolean allFieldsAreFilled = true;
        for (EditText field : fields) {
            field.setError(null);   // Clears error icon.
            if (field.getText() == null || field.getText().toString().equals("")) {
                field.requestFocus();
                field.setError(context.getString(R.string.field_is_required));
                if (field.getHint() != null) {
                    Log.e(TAG, "'" + field.getHint().toString() + "'" + " cannot be empty.");
                } else {
                    if (!field.getText().toString().equals("")) {
                        Log.e(TAG, "Field with text '" + field.getText().toString() + "' cannot be empty.");
                    } else {
                        Log.e(TAG, "Field cannot be empty.");
                    }
                }
                allFieldsAreFilled = false;
                break;
            }
        }
        return allFieldsAreFilled;
    }

    public static boolean validateEmail(Context context, EditText emailField) {
        // See http://stackoverflow.com/questions/8204680/java-regex-email for pattern reference
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        String email = emailField.getText().toString();
        if (!pattern.matcher(email).matches()) {
            emailField.requestFocus();
            emailField.setError(context.getString(R.string.invalid_email));
            Log.e(TAG, "The user e-mail " + email + " does not match the pattern for valid e-mails. Cannot create user account.");
            return false;
        } else {
            Log.d(TAG, "E-mail is valid.");
            return true;
        }
    }

    public static boolean validatePasswords(Context context, EditText passwordField, EditText repeatPasswordField) {
        if (!passwordField.getText().toString().equals(repeatPasswordField.getText().toString())) {
            repeatPasswordField.requestFocus();
            repeatPasswordField.setError(context.getString(R.string.passwords_do_not_match));
            Log.e(TAG, "The password and confirmation password do not match.");
            return false;
        } else {
            Log.d(TAG, "Password and confirmation password do match.");
            return true;
        }
    }
}
