package net.freelancertech.journal.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.freelancertech.journal.app.R;
import net.freelancertech.journal.app.api.InstantInterface;
import net.freelancertech.journal.app.custom.ProportionalImageView;
import net.freelancertech.journal.app.model.EtudiantDTO;
import net.freelancertech.journal.app.utils.Utility;

import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public class ProfilActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfilActivity.class.getSimpleName();


    private TextView telTextView;
    private TextView operateurTextView;
    private TextView nomTextView;
    private TextView prenomTextView;
    private TextView matriculeTextView;
    private TextView emailTextView;
    private ProportionalImageView photoImageView;

    private ImageView nomImageView;
    private ImageView prenomImageView;

    private ImageView edtiPhotoImageView;
    private Uri builtUri;

    private Toolbar toolbar;

    private EtudiantDTO etudiant;

    private int numeroEdit;

    ProgressDialog prgDialog;
    String encodedString;
    RequestParams params = new RequestParams();
    String imgPath, fileName;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        toolbar = (Toolbar) findViewById(net.freelancertech.journal.app.R.id.toolbar);
        setSupportActionBar(toolbar);

        // to display Icon launcher
        // to display Icon launcher
        ActionBar actionBar = getSupportActionBar();

        //icon back
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(net.freelancertech.journal.app.R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        // set title bar
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(net.freelancertech.journal.app.R.layout.action_bar_title_profil);

        Utility.initialiseUrl(getApplicationContext());

        etudiant = new EtudiantDTO();
        telTextView = (TextView) findViewById(net.freelancertech.journal.app.R.id.telephone_profil_textview);
        telTextView.setText(Utility.getPreferredTelephone(this));
        etudiant.setTelephone(Utility.getPreferredTelephone(this));

        operateurTextView = (TextView) findViewById(net.freelancertech.journal.app.R.id.operateur_profile_textview);
        operateurTextView.setText(Utility.getPreferredOperateur(this));
        etudiant.setOperateurtel(Utility.getPreferredOperateur(this));

        nomTextView = (TextView) findViewById(net.freelancertech.journal.app.R.id.nom_profile_textview);
        nomTextView.setText(Utility.getPreferredNom(this));
        etudiant.setNom(Utility.getPreferredNom(this));

        prenomTextView = (TextView) findViewById(net.freelancertech.journal.app.R.id.prenom_profile_textview);
        prenomTextView.setText(Utility.getPreferredPrenom(this));
        etudiant.setPrenom(Utility.getPreferredPrenom(this));

        matriculeTextView = (TextView) findViewById(net.freelancertech.journal.app.R.id.matricule_profile_textview);
        matriculeTextView.setText(Utility.getPreferredMatricule(this));
        etudiant.setPays(Utility.getPreferredMatricule(this));

        emailTextView = (TextView) findViewById(net.freelancertech.journal.app.R.id.email_profile_textview);
        emailTextView.setText(Utility.getPreferredEmail(this));
        etudiant.setEmail(Utility.getPreferredEmail(this));

        photoImageView  = (ProportionalImageView) findViewById(net.freelancertech.journal.app.R.id.photo_imageview);

        // URL image
        String urlPhotoprofile ="/facinfosflash/imagesprofils/"+Utility.getPreferredEmail(this)+".jpg";
        // Construct the URL photo
       // Log.e(LOG_TAG, "URLPHOTO = " + Utility.urlserveurimage+urlPhotoprofile);
        builtUri  = new Uri.Builder()
                .scheme("http")
                .authority(Utility.urlserveurimage.replaceFirst("http://",""))
                .path(urlPhotoprofile)
                .build();
       // Log.e(LOG_TAG, "URI = " + builtUri.toString());


        Picasso.with(this).load(builtUri.toString()).placeholder(net.freelancertech.journal.app.R.drawable.nophoto).error(net.freelancertech.journal.app.R.drawable.nophoto).into(photoImageView);



        nomImageView = (ImageView) findViewById(net.freelancertech.journal.app.R.id.nom_imageview);
        nomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(LOG_TAG, "******************************* edit nom ");
                numeroEdit = 1;
                AlertDialog diaBox = EditAskOption();
                diaBox.show();

            }
        });


        prenomImageView = (ImageView) findViewById(net.freelancertech.journal.app.R.id.prenom_imageview);
        prenomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numeroEdit = 2;
                AlertDialog diaBox = EditAskOption();
                diaBox.show();

            }
        });




        edtiPhotoImageView = (ImageView) findViewById(net.freelancertech.journal.app.R.id.edit_photo_imageview);
        edtiPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline()) {
                    // Create intent to Open Image applications like Gallery, Google Photos
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                }else {

                    Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet2), Toast.LENGTH_SHORT).show();
                }
            }
        });

        prgDialog = new ProgressDialog(this);
        // Set Cancelable as False
        prgDialog.setCancelable(false);


    }


    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
               // ImageView imgView = (ImageView) findViewById(R.id.photo_imageview);
                // Set the Image in ImageView
                photoImageView.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");

                //fileName = fileNameSegments[fileNameSegments.length - 1];
                fileName = Utility.getPreferredEmail(getApplicationContext());
                // Put file name in Async Http Post Param which will used in Php web app
                params.put("filename", fileName+".jpg");


                // When Image is selected from Gallery
                if (imgPath != null && !imgPath.isEmpty()) {
                    prgDialog.setMessage(getResources().getString(net.freelancertech.journal.app.R.string.photo_message_internet2));
                    prgDialog.show();
                    // Convert image to String using Base64
                    encodeImagetoString();
                    // When Image is not selected from Gallery
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            getResources().getString(net.freelancertech.journal.app.R.string.photo_message_internet3),
                            Toast.LENGTH_LONG).show();
                }



            } else {
                Toast.makeText(this, getResources().getString(net.freelancertech.journal.app.R.string.photo_message_internet4),
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, getResources().getString(net.freelancertech.journal.app.R.string.photo_message_internet5), Toast.LENGTH_LONG)
                    .show();
        }

    }


    public boolean isOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()== NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()== NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;
    }

    @Override
    protected void onStart() {
        super.onStart();

       // Picasso.with(this).load(builtUri.toString()).into(photoImageView);
    }

    public AlertDialog EditAskOption()
    {
        final EditText edit = new EditText(this);
        final  String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String oldValue ;

        switch (numeroEdit) {
            case 1:  oldValue = nomTextView.getText().toString();
                break;
            case 2:  oldValue = prenomTextView.getText().toString();
                break;
            case 3:  oldValue = matriculeTextView.getText().toString();
                break;
            case 4:  oldValue = emailTextView.getText().toString();
                break;
            case 5:  oldValue = operateurTextView.getText().toString();
                break;
            default:
                oldValue = getResources().getString(net.freelancertech.journal.app.R.string.titleabonner);
                break;
        }

        edit.setText(oldValue);

        AlertDialog myEditDialogBox =new AlertDialog.Builder(this)

                //set message, title, and icon
                .setTitle(getResources().getString(net.freelancertech.journal.app.R.string.edition))
                .setView(edit)
                .setPositiveButton(getResources().getString(net.freelancertech.journal.app.R.string.editer), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        final String newValue = edit.getText().toString();
                        if (!newValue.matches("")) {

                            switch (numeroEdit) {
                                case 1:
                                    etudiant.setNom(newValue);
                                    break;
                                case 2:
                                    etudiant.setPrenom(newValue);
                                    break;
                                case 3:
                                    etudiant.setPays(newValue);
                                    break;
                                case 4: {

                                    break;
                                }
                                case 5: {

                                    break;
                                }
                            }

                            updateProfil(etudiant);

                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.vide), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }

                })

                .setNegativeButton(getResources().getString(net.freelancertech.journal.app.R.string.inscription_activity_annuler), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myEditDialogBox;

    }

    void updateProfil(EtudiantDTO etudiant) {

        if (isOnline()) {

        String apiUrl = Utility.urlApiInfosFlash;

        if (apiUrl != null) {

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(apiUrl).build();

            InstantInterface instantDriverService = restAdapter.create(InstantInterface.class);

            instantDriverService.updateEtudiant(etudiant, new Callback<EtudiantDTO>() {
                @Override
                public void success(EtudiantDTO etudiantDTO, Response response) {
                    if (etudiantDTO != null) {

                        Utility.setPreferredOperateur(getApplicationContext(), etudiantDTO.getOperateurtel());
                        Utility.setPreferredNom(getApplicationContext(), etudiantDTO.getNom());
                        Utility.setPreferredPrenom(getApplicationContext(), etudiantDTO.getPrenom());
                        Utility.setPreferredMatricule(getApplicationContext(), etudiantDTO.getPays());
                        Utility.setPreferredEmail(getApplicationContext(), etudiantDTO.getEmail());
                        operateurTextView.setText(etudiantDTO.getOperateurtel());
                        nomTextView.setText(etudiantDTO.getNom());
                        prenomTextView.setText(etudiantDTO.getPrenom());
                        matriculeTextView.setText(etudiantDTO.getPays());
                        emailTextView.setText(etudiantDTO.getEmail());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet), Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Utility.initialiseUrl(getApplicationContext());
        }

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet2), Toast.LENGTH_SHORT).show();
        }

    }


    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage(getResources().getString(net.freelancertech.journal.app.R.string.photo_message_internet6));
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);

                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    public void triggerImageUpload() {
        makeHTTPCall();
    }

    public void makeHTTPCall() {
        prgDialog.setMessage(getResources().getString(net.freelancertech.journal.app.R.string.photo_message_internet7));
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post(Utility.urlserveurimage+"/facinfosflash/upload_image.php",
                params, new AsyncHttpResponseHandler() {
                    // response '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.photo_message_internet8),
                                Toast.LENGTH_LONG).show();
                        //mise en cache de la photo
                        Picasso.with(getApplicationContext()).invalidate(builtUri);
                        Picasso.with(getApplicationContext()).load(builtUri.toString()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(photoImageView);



                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.photo_message_internet1), Toast.LENGTH_SHORT).show();
                            Picasso.with(getApplicationContext()).load(builtUri.toString()).error(net.freelancertech.journal.app.R.drawable.nophoto).into(photoImageView);
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.photo_message_internet1), Toast.LENGTH_SHORT).show();

                            Picasso.with(getApplicationContext()).load(builtUri.toString()).error(net.freelancertech.journal.app.R.drawable.nophoto).into(photoImageView);
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.photo_message_internet1), Toast.LENGTH_SHORT).show();

                            Picasso.with(getApplicationContext()).load(builtUri.toString()).error(net.freelancertech.journal.app.R.drawable.nophoto).into(photoImageView);
                        }
                    }

                });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            prgDialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }




}
