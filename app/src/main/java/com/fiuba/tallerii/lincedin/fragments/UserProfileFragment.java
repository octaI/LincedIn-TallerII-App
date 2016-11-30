package com.fiuba.tallerii.lincedin.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.activities.BiographyActivity;
import com.fiuba.tallerii.lincedin.activities.EducationActivity;
import com.fiuba.tallerii.lincedin.activities.LogInActivity;
import com.fiuba.tallerii.lincedin.activities.RecommendationsActivity;
import com.fiuba.tallerii.lincedin.activities.SkillsActivity;
import com.fiuba.tallerii.lincedin.activities.WorkExperienceActivity;
import com.fiuba.tallerii.lincedin.adapters.UserEducationAdapter;
import com.fiuba.tallerii.lincedin.adapters.UserJobsAdapter;
import com.fiuba.tallerii.lincedin.adapters.UserSkillsAdapter;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.fiuba.tallerii.lincedin.model.user.UserJob;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.utils.ClipboardManager;
import com.fiuba.tallerii.lincedin.utils.DateUtils;
import com.fiuba.tallerii.lincedin.utils.ImageUtils;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;
import com.fiuba.tallerii.lincedin.utils.ViewUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment {

    private static final String TAG = "UserProfile";

    private static final String ARG_USER_ID = "USER_ID";
    private static final String TEMP_IMAGE_NAME = "tempImage" ;
    private static final int ACTIVITY_SELECT_IMAGE = 1 ;

    private static final int DEFAULT_MIN_WIDTH = 100 ;
    private static int minWidthQuality = DEFAULT_MIN_WIDTH;

    private View convertView;

    private User user;
    private boolean isOwnProfile;

    private UserJobsAdapter userJobsAdapter;
    private UserEducationAdapter userEducationAdapter;
    private UserSkillsAdapter userSkillsAdapter;

    public UserProfileFragment() {}

    public static UserProfileFragment newInstance(String userId) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setIsOwnProfileFlag();

        userSkillsAdapter = new UserSkillsAdapter(getContext());
        userJobsAdapter = new UserJobsAdapter(getContext());
        userEducationAdapter = new UserEducationAdapter(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        // Profile could be updated from other activities, e.g. WorkExperience, Skills, etc.
        requestUserProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        setAdapters(convertView);
        setButtonsListeners(convertView);
        setButtonsVisibility(convertView);
        requestUserProfile();

        return convertView;
    }

    private void setIsOwnProfileFlag() {
        isOwnProfile = getArguments() == null
                || getArguments().getString(ARG_USER_ID) == null
                || getArguments().getString(ARG_USER_ID).equals(SharedPreferencesUtils.getStringFromSharedPreferences(getContext(), SharedPreferencesKeys.USER_ID, ""));
    }

    private void setButtonsListeners(final View parentView) {
        parentView.findViewById(R.id.user_profile_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        parentView.findViewById(R.id.user_own_profile_edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserBiography();
            }
        });

        parentView.findViewById(R.id.user_profile_work_experience_see_more_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserWorkExperience();
            }
        });

        parentView.findViewById(R.id.user_profile_education_see_more_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserEducation();
            }
        });

        parentView.findViewById(R.id.user_profile_see_recommendations_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecommendations();
            }
        });

        parentView.findViewById(R.id.user_profile_skills_see_more_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserSkills();
            }
        });

        parentView.findViewById(R.id.user_profile_picture_imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOwnProfile) {

                    promptGalleryChoice();
                }
            }
        });
    }

    private void promptGalleryChoice() {
        Intent chooserIntent = null;
        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data",true);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(getTempFile(getContext())));
        intentList = addIntentsToList(getContext(),intentList,pickIntent);
        intentList = addIntentsToList(getContext(),intentList,takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size()-1),getContext().getString(R.string.pick_image_intent_text));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }
         startActivityForResult(chooserIntent,ACTIVITY_SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ACTIVITY_SELECT_IMAGE:
                Bitmap bitmap = getImageFromResult(getContext(), resultCode, data);
                if (bitmap != null) {


                    byte[] bmByteArray = ImageUtils.returnByteArrayFromBitmap(bitmap);
                    String b64encode = ImageUtils.encodeByteArrayToBase64(bmByteArray);
                    user.profilePicture = b64encode;
                    LincedInRequester.editUserProfile(user, getContext(),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, response.toString());
                                    Log.i(TAG, "Succesfully updated profile picture!");

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Log.e(TAG, error.toString());
                                    error.printStackTrace();

                                }
                            });

                    Toast.makeText(getContext(), "Imagen cambiada exitosamente.", Toast.LENGTH_SHORT).show();

                }
        }
    }

    private Bitmap getImageFromResult(Context Context, int resultCode, Intent imageReturnedIntent) {
        Log.d(TAG, "getImageFromResult, resultCode: " + resultCode);
        Bitmap bm = null;
        File imageFile = getTempFile(Context);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage;
            boolean isCamera = (imageReturnedIntent == null ||
                    imageReturnedIntent.getData() == null  ||
                    imageReturnedIntent.getData().toString().contains(imageFile.toString()));
            if (isCamera) {     /** CAMERA **/
                selectedImage = Uri.fromFile(imageFile);
            } else {            /** ALBUM **/
                selectedImage = imageReturnedIntent.getData();
            }
            Log.d(TAG, "selectedImage: " + selectedImage);

            bm = getImageResized(getContext(), selectedImage);
            int rotation = getRotation(getContext(), selectedImage, isCamera);
            bm = rotate(bm, rotation);
        }
        return bm;
    }

    private static Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bm = null;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            Log.d(TAG, "resizer: new bitmap width = " + bm.getWidth());
            i++;
        } while (bm.getWidth() < minWidthQuality && i < sampleSizes.length);
        return bm;
    }

    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);

        Log.d(TAG, options.inSampleSize + " sample method bitmap ... " +
                actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());

        return actuallyUsableBitmap;
    }


    private static int getRotation(Context context, Uri imageUri, boolean isCamera) {
        int rotation;
        if (isCamera) {
            rotation = getRotationFromCamera(context, imageUri);
        } else {
            rotation = getRotationFromGallery(context, imageUri);
        }
        Log.d(TAG, "Image rotation: " + rotation);
        return rotation;
    }

    private static int getRotationFromCamera(Context context, Uri imageFile) {
        int rotate = 0;
        try {

            context.getContentResolver().notifyChange(imageFile, null);
            ExifInterface exif = new ExifInterface(imageFile.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static int getRotationFromGallery(Context context, Uri imageUri) {
        int result = 0;
        String[] columns = {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int orientationColumnIndex = cursor.getColumnIndex(columns[0]);
                result = cursor.getInt(orientationColumnIndex);
            }
        } catch (Exception e) {
            //Do nothing
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }//End of try-catch block
        return result;
    }


    private static Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bmOut = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            return bmOut;
        }
        return bm;
    }


    private static File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
            Log.d(TAG, "Intent: " + intent.getAction() + " package: " + packageName);
        }
        return list;
    }


    private void setButtonsVisibility(View v) {
        if (isOwnProfile) {
            v.findViewById(R.id.user_profile_public_buttons_layout).setVisibility(View.GONE);
            v.findViewById(R.id.user_own_profile_edit_button).setVisibility(View.VISIBLE);
        } else {
            v.findViewById(R.id.user_profile_public_buttons_layout).setVisibility(View.VISIBLE);
            v.findViewById(R.id.user_own_profile_edit_button).setVisibility(View.GONE);
        }
    }

    private void requestUserProfile() {
        if (convertView != null) {
            boolean isUserLogged = SharedPreferencesUtils.getBooleanFromSharedPreferences(getContext(), SharedPreferencesKeys.USER_LOGGED_IN, false);
            refreshUserNotLoggedMessage(convertView, isUserLogged);
            if (isUserLogged) {
                refreshLoadingIndicator(convertView, true);
                LincedInRequester.getUserProfile(
                        getArguments().getString(ARG_USER_ID) != null ? getArguments().getString(ARG_USER_ID) : "me",
                        getContext(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Gson gson = new Gson();
                                Log.d(TAG, gson.toJson(response));

                                user = gson.fromJson(response.toString(), User.class);
                                populateProfile(convertView, user);
                                refreshLoadingIndicator(convertView, false);
                                hideErrorScreen(convertView);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, error.toString());
                                error.printStackTrace();
                                refreshLoadingIndicator(convertView, false);
                                setErrorScreen(convertView);
                            }
                        }
                );
            }
        }
    }

    private void setAdapters(View v) {
        ((ListView) v.findViewById(R.id.user_profile_work_experience_listview)).setAdapter(userJobsAdapter);
        ((ListView) v.findViewById(R.id.user_profile_education_listview)).setAdapter(userEducationAdapter);
        ((ListView) v.findViewById(R.id.user_profile_skills_listview)).setAdapter(userSkillsAdapter);
    }

    private void populateProfile(View v, User user) {
        populateBasicInfo(v, user);
        populateCurrentWork(v, user);
        populateBiography(v, user);
        populateWorkExperience(v, user);
        populateEducation(v, user);
        populateSkills(v, user);
    }

    private void populateBasicInfo(View v, User user) {
        final ImageView userImageView = (ImageView) v.findViewById(R.id.user_profile_picture_imageview);
        LincedInRequester.getUserProfileImage(getContext(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    String b64str = response.getJSONObject("content").toString();
                    ImageUtils.setBase64ImageFromString(getContext(),b64str,userImageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                error.printStackTrace();
                refreshLoadingIndicator(convertView, false);
            }
        }
        ,user.profilePicture);

        //String baseliteral = getResources().getString(R.string.literal_riquelme);


        //ImageUtils.setBase64ImageFromString(getContext(),baseliteral,userImageView);

        ((TextView) v.findViewById(R.id.user_profile_username_textview)).setText(user.fullName);

        TextView descriptionTextView = (TextView) v.findViewById(R.id.user_profile_user_description_textview);
        if (user.description != null) {
            descriptionTextView.setText(user.description);
        }
    }

    private void populateBiography(View v, User user) {
        ((TextView) v.findViewById(R.id.user_profile_biography_fullname_textview)).setText(user.fullName);

        setDateOfBirthInfo(v, user);

        ((TextView) v.findViewById(R.id.user_profile_biography_email_textview)).setText(user.email);
        setEmailListeners(v);

        // TODO: 05/11/16 Set location when API supports it.
    }

    private void setDateOfBirthInfo(View v, User user) {
        TextView dateOfBirhtTextView = (TextView) v.findViewById(R.id.user_profile_biography_date_of_birth_textview);
        TextView ageTextView = (TextView) v.findViewById(R.id.user_profile_biography_age_textview);
        if (user.dateOfBirth != null && !user.dateOfBirth.equals("")) {
            dateOfBirhtTextView.setVisibility(View.VISIBLE);
            dateOfBirhtTextView.setText("(" + DateUtils.parseDatetimeToLocalDate(getContext(), user.dateOfBirth) + ")");

            ageTextView.setVisibility(View.VISIBLE);
            ageTextView.setText(
                    ((TextView) v.findViewById(R.id.user_profile_biography_age_textview)).getText().toString()
                            .replace(":1", DateUtils.getAgeFromDatetime(user.dateOfBirth))
            );
        } else {
            dateOfBirhtTextView.setVisibility(View.GONE);
            ageTextView.setVisibility(View.GONE);
        }
    }

    private void setEmailListeners(View v) {
        final TextView emailTextView = (TextView) v.findViewById(R.id.user_profile_biography_email_textview);

        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: " + emailTextView.getText().toString()));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
            }
        });

        emailTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager.copyToClipboard(getContext(), emailTextView.getText().toString(), getString(R.string.email));
                ViewUtils.setToast(getContext(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT);
                return true;
            }
        });
    }

    private void populateWorkExperience(View v, User user) {
        List<UserJob> jobsToShow = user.jobs != null && !user.jobs.isEmpty() ?
                user.jobs.subList(0, user.jobs.size() - 1) : user.jobs;
        if (user.isCurrentlyWorking()) {
            jobsToShow.remove(user.getCurrentWork());
        }
        userJobsAdapter.setDataset(jobsToShow);
        userJobsAdapter.notifyDataSetChanged();

        if (isOwnProfile && userJobsAdapter.isEmpty()) {
            ((Button) v.findViewById(R.id.user_profile_work_experience_see_more_button)).setText(getString(R.string.add));
        }
    }

    private void populateCurrentWork(View v, User user) {
        if (user.isCurrentlyWorking()) {
            v.findViewById(R.id.user_profile_current_job_cardview).setVisibility(View.VISIBLE);

            ((TextView) v.findViewById(R.id.user_profile_current_job_company_textview)).setText(user.getCurrentWork().company);
            ((TextView) v.findViewById(R.id.user_profile_current_job_position_textview)).setText(user.getCurrentWork().position.name);
            ((TextView) v.findViewById(R.id.user_profile_current_job_since_date_textview))
                    .setText(
                            ((TextView) v.findViewById(R.id.user_profile_current_job_since_date_textview)).getText().toString()
                                    .replace(":1", DateUtils.extractYearFromDatetime(user.getCurrentWork().since))
                    );
        } else {
            v.findViewById(R.id.user_profile_current_job_cardview).setVisibility(View.GONE);
        }
    }

    private void populateEducation(View v, User user) {
        userEducationAdapter.setDataset(user.education);
        userEducationAdapter.notifyDataSetChanged();

        if (isOwnProfile && userEducationAdapter.isEmpty()) {
            ((Button) v.findViewById(R.id.user_profile_education_see_more_button)).setText(getString(R.string.add));
        }
    }

    private void populateSkills(View v, User user) {
        userSkillsAdapter.setDataset(user.skills);
        userSkillsAdapter.notifyDataSetChanged();

        if (isOwnProfile && userSkillsAdapter.isEmpty()) {
            ((Button) v.findViewById(R.id.user_profile_skills_see_more_button)).setText(getString(R.string.add));
        }
    }

    private void refreshLoadingIndicator(View v, boolean loading) {
        if (loading) {
            v.findViewById(R.id.user_profile_main_container_nestedscrollview).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.user_profile_loading_circular_progress).setVisibility(View.VISIBLE);
            v.findViewById(R.id.user_profile_network_error_layout).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.user_profile_main_container_nestedscrollview).setVisibility(View.VISIBLE);
            v.findViewById(R.id.user_profile_loading_circular_progress).setVisibility(View.GONE);
        }
    }

    private void refreshUserNotLoggedMessage(View v, boolean isUserLogged) {
        if (isUserLogged) {
            v.findViewById(R.id.user_profile_main_container_nestedscrollview).setVisibility(View.VISIBLE);
            v.findViewById(R.id.user_profile_user_not_logged_layout).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.user_profile_main_container_nestedscrollview).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.user_profile_user_not_logged_layout).setVisibility(View.VISIBLE);
            v.findViewById(R.id.user_profile_network_error_layout).setVisibility(View.GONE);
        }
    }

    private void setErrorScreen(final View parentView) {
        parentView.findViewById(R.id.user_profile_main_container_nestedscrollview).setVisibility(View.INVISIBLE);

        RelativeLayout errorScreen = (RelativeLayout) parentView.findViewById(R.id.user_profile_network_error_layout);
        errorScreen.setVisibility(View.VISIBLE);
        errorScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideErrorScreen(parentView);
                requestUserProfile();
            }
        });
    }

    private void hideErrorScreen(View v) {
        v.findViewById(R.id.user_profile_network_error_layout).setVisibility(View.GONE);
    }

    private void openLogin() {
        Intent loginIntent = new Intent(getContext(), LogInActivity.class);
        startActivity(loginIntent);
    }

    private void openUserBiography() {
        Intent biographyIntent = new Intent(getContext(), BiographyActivity.class);
        if (user != null) {
            biographyIntent.putExtra(BiographyActivity.ARG_USER, new Gson().toJson(user));
        }
        startActivity(biographyIntent);
    }

    private void openUserWorkExperience() {
        Intent workExperienceIntent = new Intent(getContext(), WorkExperienceActivity.class);
        if (user != null) {
            workExperienceIntent.putExtra(WorkExperienceActivity.ARG_USER, new Gson().toJson(user));
        }
        workExperienceIntent.putExtra(WorkExperienceActivity.ARG_IS_OWN_PROFILE, isOwnProfile);
        startActivity(workExperienceIntent);
    }

    private void openUserEducation() {
        Intent educationIntent = new Intent(getContext(), EducationActivity.class);
        if (user != null) {
            educationIntent.putExtra(EducationActivity.ARG_USER, new Gson().toJson(user));
        }
        educationIntent.putExtra(EducationActivity.ARG_IS_OWN_PROFILE, isOwnProfile);
        startActivity(educationIntent);
    }

    private void openRecommendations() {
        Intent recommendationsIntent = new Intent(getContext(), RecommendationsActivity.class);
        if (user != null) {
            recommendationsIntent.putExtra(RecommendationsActivity.ARG_USER_ID, user.id);
        }
        recommendationsIntent.putExtra(RecommendationsActivity.ARG_IS_OWN_PROFILE, isOwnProfile);
        startActivity(recommendationsIntent);
    }

    private void openUserSkills() {
        Intent skillsIntent = new Intent(getContext(), SkillsActivity.class);
        if (user != null) {
            skillsIntent.putExtra(SkillsActivity.ARG_USER, new Gson().toJson(user));
        }
        skillsIntent.putExtra(SkillsActivity.ARG_IS_OWN_PROFILE, isOwnProfile);
        startActivity(skillsIntent);
    }
}
