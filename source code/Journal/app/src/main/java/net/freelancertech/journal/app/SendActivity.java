package net.freelancertech.journal.app;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import net.freelancertech.journal.app.model.InformationDTO;
import net.freelancertech.journal.app.sync.DoodlesArchiveSyncAdapter;
import net.freelancertech.journal.app.utils.Utility;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
public class SendActivity extends ActionBarActivity {

    private static final String LOG_TAG = SendActivity.class.getSimpleName();

    private EditText objetEditText;
    private EditText urlEditText;
    private Button sendButton;
    private Button fichierButton;
    private EmojiconEditText messageEditText;
    private EmojIconActions emojIcon;
    private ImageView emojiButton;
    private View rootView;

    private ProportionalImageView fichierJointImageView;
    private Uri builtUri;

    ProgressDialog prgDialog;
    String encodedString;
    RequestParams params = new RequestParams();
    String imgPath, fileName = "vide";
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.freelancertech.journal.app.R.layout.activity_send);
        // to display Icon launcher


        toolbar = (Toolbar) findViewById(net.freelancertech.journal.app.R.id.toolbar);
        setSupportActionBar(toolbar);

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
        actionBar.setCustomView(net.freelancertech.journal.app.R.layout.action_bar_title_send);


        objetEditText = (EditText) findViewById(net.freelancertech.journal.app.R.id.objet_editText);
        urlEditText = (EditText) findViewById(R.id.url_editText);
        emojiButton = (ImageView) findViewById(R.id.emoji_send_btn);
        messageEditText = (EmojiconEditText) findViewById(net.freelancertech.journal.app.R.id.message_editText);
        rootView = findViewById(R.id.root_view);
        emojIcon=new EmojIconActions(getApplicationContext(),rootView,messageEditText,emojiButton);
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard","open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard","close");
            }
        });


        fichierButton = (Button) findViewById(R.id.fichier_button);
        fichierButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_attach_file_black_24dp), null, null, null);
        fichierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                if (isOnline()) {


                    // Create intent to Open Image applications like Gallery, Google Photos
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);


                } else {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.system_message_internet2), Toast.LENGTH_SHORT).show();

                }

            }
        });

        fichierJointImageView  = (ProportionalImageView) findViewById(R.id.fichierjoint_imageview);

        prgDialog = new ProgressDialog(this);
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        sendButton = (Button) findViewById(net.freelancertech.journal.app.R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                // recuperation des valeurs saisis dans le formulaire
                String objet = objetEditText.getText().toString();
                String message = messageEditText.getText().toString();
                String url = urlEditText.getText().toString();

                if (objet.matches("") || message.matches("")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.send_activity_message5), Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    if (isOnline()) {

                        String email = Utility.getPreferredEmail(getApplicationContext());
                        String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(message);

                        if (url.matches("")) url="vide";

                        InformationDTO info = new InformationDTO(0,objet,toServerUnicodeEncoded," "," "," ",fileName,url);

                        String apiUrl = Utility.urlApiInfosFlash;

                       // Log.d(LOG_TAG, "******************************* apiUrl= " + apiUrl);

                        if (apiUrl != null) {

                            RestAdapter restAdapter = new RestAdapter.Builder()
                                    .setEndpoint(apiUrl).build();

                            InstantInterface instantDriverService = restAdapter.create(InstantInterface.class);

                            instantDriverService.diffuserInformation(email, info, new Callback<InformationDTO>() {
                                @Override
                                public void success(InformationDTO informationDTO, Response response) {

                                    Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.send_activity_message2), Toast.LENGTH_SHORT).show();

                                    try {
                                        // syncauto notes local with server
                                        DoodlesArchiveSyncAdapter.syncImmediately(getApplicationContext());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.system_message_server), Toast.LENGTH_SHORT).show();

                                }
                            });
                        } else {
                            Utility.initialiseUrl(getApplicationContext());
                        }
                    }else{

                        Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet2), Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

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
                fichierJointImageView.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");

                Date localTime = new Date();
                //creating DateFormat for converting time from local timezone to GMT
                SimpleDateFormat converter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
                //getting GMT timezone, you can get any timezone e.g. UTC
                converter.setTimeZone(TimeZone.getTimeZone("GMT"));

                fileName = Utility.getPreferredTelephone(getApplicationContext())+"_"+converter.format(localTime).toString();
                // Put file name in Async Http Post Param which will used in Php web app
                params.put("filename", fileName+".jpg");


                // When Image is selected from Gallery
                if (imgPath != null && !imgPath.isEmpty()) {
                    prgDialog.setMessage(getResources().getString(R.string.fichier_message_internet2));
                    prgDialog.show();
                    // Convert image to String using Base64
                    encodeImagetoString();
                    // When Image is not selected from Gallery
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            getResources().getString(R.string.fichier_message_internet3),
                            Toast.LENGTH_LONG).show();
                }



            } else {
                Toast.makeText(this, getResources().getString(R.string.fichier_message_internet4),
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, getResources().getString(R.string.photo_message_internet5), Toast.LENGTH_LONG)
                    .show();
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
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage(getResources().getString(R.string.photo_message_internet6));
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
        prgDialog.setMessage(getResources().getString(R.string.fichier_message_internet7));
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post(Utility.urlserveurimage+"/facinfosflash/upload_image.php",
                params, new AsyncHttpResponseHandler() {
                    // response '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.fichier_message_internet8),
                                Toast.LENGTH_LONG).show();

                        // URL image
                        String urlImage ="/facinfosflash/imagesprofils/"+fileName+".jpg";
                        // Construct the URL photo
                        // Log.e(LOG_TAG, "URLPHOTO = " + Utility.urlserveurimage+urlPhotoprofile);
                        builtUri  = new Uri.Builder()
                                .scheme("http")
                                .authority(Utility.urlserveurimage.replaceFirst("http://",""))
                                .path(urlImage)
                                .build();

                        //mise en cache de la photo
                        Picasso.with(getApplicationContext()).invalidate(builtUri);
                        Picasso.with(getApplicationContext()).load(builtUri.toString()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(fichierJointImageView);



                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        fileName = "vide";
                        // URL image
                        String urlImage ="/facinfosflash/imagesprofils/"+fileName+".jpg";
                        // Construct the URL photo
                        // Log.e(LOG_TAG, "URLPHOTO = " + Utility.urlserveurimage+urlPhotoprofile);
                        builtUri  = new Uri.Builder()
                                .scheme("http")
                                .authority(Utility.urlserveurimage.replaceFirst("http://",""))
                                .path(urlImage)
                                .build();

                        // Hide Progress Dialog
                        prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.fichier_message_internet1), Toast.LENGTH_SHORT).show();
                            Picasso.with(getApplicationContext()).load(builtUri.toString()).error(R.drawable.nophoto).into(fichierJointImageView);
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.fichier_message_internet1), Toast.LENGTH_SHORT).show();

                            Picasso.with(getApplicationContext()).load(builtUri.toString()).error(R.drawable.nophoto).into(fichierJointImageView);
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.fichier_message_internet1), Toast.LENGTH_SHORT).show();

                            Picasso.with(getApplicationContext()).load(builtUri.toString()).error(R.drawable.nophoto).into(fichierJointImageView);
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



}
