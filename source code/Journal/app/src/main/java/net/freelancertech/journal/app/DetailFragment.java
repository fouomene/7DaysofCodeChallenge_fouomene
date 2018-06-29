package net.freelancertech.journal.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.BubbleImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.freelancertech.journal.app.adapter.CommentaireAdapter;
import net.freelancertech.journal.app.api.InstantInterface;
import net.freelancertech.journal.app.custom.CustomTouchListener;
import net.freelancertech.journal.app.custom.LinedEditText;
import net.freelancertech.journal.app.custom.ProportionalImageView;
import net.freelancertech.journal.app.data.InstantContract;
import net.freelancertech.journal.app.data.InstantContract.DoodleEntry;
import net.freelancertech.journal.app.model.CommentaireDTO;
import net.freelancertech.journal.app.model.Reponse;
import net.freelancertech.journal.app.sync.DoodlesArchiveSyncAdapter;
import net.freelancertech.journal.app.utils.Utility;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
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
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        CommentaireAdapter.CustomButtonListener {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    static final String DETAIL_INFORMATION_ID = "IDINFORMATION";


    private static final String DOODLE_SHARE_HASHTAG = "Journal Doodle";

    private ShareActionProvider mShareActionProvider;
    private String mDoodle;
    private Uri mUri;

    private long informationId = -1;
    private String fichier;
    private String url;
    private int action; //action commenter=1
    private boolean loadingFinished = true;
    private boolean redirect = false;



    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            DoodleEntry.TABLE_NAME + "." + DoodleEntry._ID,
            DoodleEntry.COLUMN_OBJET,
            DoodleEntry.COLUMN_MESSAGE,
            DoodleEntry.COLUMN_RUN_DATE,
            DoodleEntry.COLUMN_INFORMATION_ID,
            DoodleEntry.COLUMN_TELEPHONE_DELEGUE,
            DoodleEntry.COLUMN_PRENOM_DELEGUE,
            DoodleEntry.COLUMN_FICHIER,
            DoodleEntry.COLUMN_URL,

            // This works because the InstantProvider returns yearmonth data joined with
            // doodle data, even though they're stored in two different tables.
            InstantContract.YearMonthEntry.COLUMN_YEAR_SETTING,
            InstantContract.YearMonthEntry.COLUMN_MONTH_SETTING

    };

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    public static final int COLUMN_OBJET =1 ;
    public static final int COLUMN_MESSAGE = 2;
    public static final int COLUMN_COLUMN_RUN_DATE = 3;
    public static final int COLUMN_INFORMATION_ID = 4;
    public static final int COLUMN_TELEPHONE_DELEGUE = 5;
    public static final int COLUMN_PRENOM_DELEGUE = 6;
    public static final int COLUMN_FICHIER = 7;
    public static final int COLUMN_URL = 8;

    private EmojiconTextView mload_textview;
    private TextView mmessage_textview;
   // private TextView fonction_textview;
    private TextView date_information_textview;
    private TextView supprimer_information_textview;
    private TextView  detail_textview;
    private TextView  url_textview;
    private TextView  fichier_textview;
    private ImageView fichier_imageview;

    private FloatingActionButton fabCommentaire;

    private CommentaireAdapter mCommentaireAdapter;
    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    private CommentaireDTO commentaireSelect;
    private int positionCommentaireSelect;


    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    private long id;
    private String text;




    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
            informationId = Long.parseLong(arguments.getString(DETAIL_INFORMATION_ID));
        }


        View rootView = inflater.inflate(R.layout.fragment_detail, null);

        mload_textview = (EmojiconTextView) rootView.findViewById(net.freelancertech.journal.app.R.id.load_textview);
        //mmessage_textview = (ImageView) rootView.findViewById(R.id.detail_icon);
        mmessage_textview = (TextView) rootView.findViewById(net.freelancertech.journal.app.R.id.messages_textview);


     //   fonction_textview = (TextView) rootView.findViewById(net.freelancertech.journal.app.R.id.fonction_textview);

        date_information_textview = (TextView) rootView.findViewById(net.freelancertech.journal.app.R.id.date_info_detail_textview);

        detail_textview = (TextView) rootView.findViewById(R.id.detail_textview);
        url_textview = (TextView) rootView.findViewById(R.id.url_textview);

        url_textview.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_link_black_24dp), null, null, null);
        url_textview.setOnTouchListener(new CustomTouchListener());
        url_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline()){

                    //set up dialog
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.detail_dialog);
                    dialog.setTitle(getResources().getString(R.string.detail));
                    dialog.setCancelable(true);

                    final TextView load = (TextView) dialog.findViewById(R.id.load_textview);

                    /*WebView webView = (WebView) dialog.findViewById(R.id.webViewDetail);
                    webView.setWebChromeClient(new MyWebViewClient());
                    */
                    final ProgressBar progressbar = (ProgressBar) dialog.findViewById(R.id.progressBarDetail);
                    progressbar.setMax(1000);


                    WebView mWebView = (WebView) dialog.findViewById(R.id.webViewDetail);
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    //final Activity activity = dialog.;
                    mWebView.setWebChromeClient(new WebChromeClient() {
                        public void onProgressChanged(WebView view, int progress) {
                            // Activities and WebViews measure progress with different scales.
                            // The progress meter will automatically disappear when we reach 100%
                            /*activity.setProgress(progress * 1000);

                            activity.setTitle("Loading...");*/
                            progressbar.setProgress(progress*1000);
                            super.onProgressChanged(view, progress);

                            //activity.setProgress(progress * 1000);

                            if(progress == 1000){
                                //progressbar.setProgress(1000);
                              //  progressbar.setVisibility(View.GONE);
                                load.setVisibility(View.GONE);

                            }
                        }
                    });

                    mWebView.setNetworkAvailable(isOnline());


                    mWebView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            if (isOnline()) {
                                // return false to let the WebView handle the URL
                                return false;
                            } else {
                                // show the proper "not connected" message
                                view.loadData("<html><body>V&eacute;rifier que votre connexion internet est active..</body></html>", "text/html", "utf-8");
                                // return true if the host application wants to leave the current
                                // WebView and handle the url itself
                                return true;
                            }
                        }
                        @SuppressWarnings("deprecation")
                        @Override
                        public void onReceivedError(WebView view, int errorCode,
                                                    String description, String failingUrl) {
                            if (errorCode == ERROR_TIMEOUT) {
                                view.stopLoading();  // may not be needed
                                view.loadData("<html><body>Connexion internet lente..</body></html>", "text/html", "utf-8");
                            }
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            load.setVisibility(View.GONE);
                            //progressbar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            progressbar.setProgress(0);
                            super.onPageStarted(view, url, favicon);
                        }

                    });

                   if(isOnline()) {

                        mWebView.loadUrl(url);

                    }else{

                        mWebView.loadData("<html><body>V&eacute;rifier que votre connexion internet est active..</body></html>", "text/html", null);
                    }

                    dialog.show();

                    Window window = dialog.getWindow();
                    window.setLayout(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);


                }else{

                    Toast.makeText(getActivity(), getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet2), Toast.LENGTH_SHORT).show();

                }

            }
        });


        fichier_textview = (TextView) rootView.findViewById(R.id.fichier_textview);
        fichier_imageview = (ImageView) rootView.findViewById(R.id.fichier_image_view);


        supprimer_information_textview = (TextView) rootView.findViewById(net.freelancertech.journal.app.R.id.supprimer_information_textview);

        supprimer_information_textview.setOnTouchListener(new CustomTouchListener());
        supprimer_information_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline()) {
                    AlertDialog diaBox = AskOptionDeleteInformation();
                    diaBox.show();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet2), Toast.LENGTH_SHORT).show();
                }

            }
        });


        Utility.initialiseUrl(getActivity());

        View listviewcommentaire = inflater.inflate(R.layout.listview_commentaire, container, false);


        fabCommentaire = (FloatingActionButton)  listviewcommentaire.findViewById(R.id.fab_commentaire);
        fabCommentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOnline()) {

                         // custom dialog
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.dialog_commentaire);
                        dialog.setTitle(getResources().getString(R.string.commentaire));

                        final View rootViewDialogCommentaire = dialog.findViewById(R.id.dialog_commentaire);
                        final ImageView emojiButton = (ImageView) dialog.findViewById(R.id.emoji_btn);
                        ImageView submitButton = (ImageView) dialog.findViewById(R.id.submit_btn);
                        final EmojiconEditText emojiconEditText = (EmojiconEditText) dialog.findViewById(R.id.emojicon_edit_text);
                        final EmojIconActions emojIcon=new EmojIconActions(getActivity(),rootViewDialogCommentaire,emojiconEditText,emojiButton);
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



                        submitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String newText = emojiconEditText.getText().toString();
                                // textView.setText(newText);

                                if (!newText.matches("")) {

                                    String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(newText);

                                    commenterInformation(toServerUnicodeEncoded);

                                } else {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.vide), Toast.LENGTH_SHORT).show();
                                }

                                dialog.dismiss();

                            }
                        });

                        dialog.show();

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.system_message_internet2), Toast.LENGTH_SHORT).show();
                }


            }
        });

        mListView = (ListView) listviewcommentaire.findViewById(R.id.listview_commentaire);

        mListView.addHeaderView(rootView, null, false);

        mCommentaireAdapter = new CommentaireAdapter(getActivity());
        mCommentaireAdapter.setCustomButtonListner(DetailFragment.this);

        udapteListCommentaire(0);

        mListView.setAdapter(mCommentaireAdapter);


        //return mListView;
        return listviewcommentaire;
    }

    public void udapteListCommentaire(final int scroll) {


        if (isOnline()) {

            String apiUrl = Utility.urlApiInfosFlash;
            if ((apiUrl != null) && (informationId != -1)) {

                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(apiUrl).build();

                InstantInterface instantDriverService = restAdapter.create(InstantInterface.class);

                instantDriverService.getAllCommentaires(informationId, new Callback<List<CommentaireDTO>>() {
                    @Override
                    public void success(List<CommentaireDTO> commentaireDTOs, Response response) {

                        if (commentaireDTOs != null) {

                            mCommentaireAdapter.updateCommentaires(commentaireDTOs);

                            if (scroll==1) smoothScrollToPositionFromTop(mListView, mCommentaireAdapter.getCount()-1);

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.system_message_server), Toast.LENGTH_SHORT).show();
                    }
                });


            } else {
                Utility.initialiseUrl(getActivity());
            }


        } else {

            Toast.makeText(getActivity(), getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet3), Toast.LENGTH_SHORT).show();

        }
    }


    public boolean isOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public AlertDialog EditAskOption()
    {
        final LinedEditText edit = new LinedEditText(getActivity());
        String message="";
        Drawable icon = null;
        String title = "";
        String textButton = "";
        switch (action) {
            case 1:  {
                message = getResources().getString(net.freelancertech.journal.app.R.string.votre_commentaire);;
                icon = getResources().getDrawable(net.freelancertech.journal.app.R.drawable.ic_comment_black_24dp);
                title = getResources().getString(net.freelancertech.journal.app.R.string.commentaire);
                textButton = getResources().getString(net.freelancertech.journal.app.R.string.commenter);
                break;
            }
        }

        AlertDialog myEditDialogBox =new AlertDialog.Builder(getActivity())

                //set message, title, and icon
                .setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setView(edit)
                .setPositiveButton(textButton, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        final String valeur = edit.getText().toString();
                        if (!valeur.matches("")) {

                            switch (action) {
                                case 1:
                                    commenterInformation(valeur);
                                    break;
                            }


                        } else {
                            Toast.makeText(getActivity(), getResources().getString(net.freelancertech.journal.app.R.string.vide), Toast.LENGTH_SHORT).show();
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

    void commenterInformation(String commentaire) {

        if (isOnline()) {

            String apiUrl = Utility.urlApiInfosFlash;

            if (apiUrl != null) {

                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(apiUrl).build();

                InstantInterface instantDriverService = restAdapter.create(InstantInterface.class);

                CommentaireDTO commentaireDTO = new CommentaireDTO(0,commentaire," ",
                        Utility.getPreferredPrenom(getActivity()),Utility.getPreferredEmail(getActivity()));

                instantDriverService.commenterInformation(informationId, commentaireDTO, new Callback<CommentaireDTO>() {
                    @Override
                    public void success(CommentaireDTO commentaireDTO, Response response) {
                        Toast.makeText(getActivity(), getResources().getString(net.freelancertech.journal.app.R.string.commentaire_publie), Toast.LENGTH_SHORT).show();
                        udapteListCommentaire(1);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.system_message_server), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Utility.initialiseUrl(getActivity());
            }

        } else {
            Toast.makeText(getActivity(), getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet2), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(net.freelancertech.journal.app.R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(net.freelancertech.journal.app.R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mDoodle != null) {
            mShareActionProvider.setShareIntent(createShareDoodleIntent());
        }
    }

    private Intent createShareDoodleIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mDoodle + "#" + DOODLE_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    void onYearMonthChanged( String newYear, String newMonth ) {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {

            mUri = DoodleEntry.buildDoodleYearMonth(newYear,newMonth);

            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            // Read from cursor
            String mTitle = data.getString(COLUMN_MESSAGE);
            String mMessage = data.getString(COLUMN_OBJET);
            String mDate = data.getString(COLUMN_COLUMN_RUN_DATE);
            fichier = data.getString(COLUMN_FICHIER);
            url = data.getString(COLUMN_URL);

            informationId = data.getLong(COLUMN_INFORMATION_ID);

            String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(mTitle);
            mload_textview.setText(fromServerUnicodeDecoded);

            mmessage_textview.setText(mMessage);
            //mmessage_textview.setMovementMethod(LinkMovementMethod.getInstance());
            date_information_textview.setText(mDate);


            if (! "vide".equals(fichier)){

                fichier_textview.setText(getResources().getString(R.string.fichier));
                fichier_textview.setVisibility(View.VISIBLE);
                fichier_imageview.setVisibility(View.VISIBLE);

            }

            if (! "vide".equals(url)){

                SpannableString content = new SpannableString(url);
                content.setSpan(new UnderlineSpan(), 0, url.length(), 0);
                url_textview.setText(content);
                detail_textview.setVisibility(View.VISIBLE);
                url_textview.setVisibility(View.VISIBLE);

            }

            // URL image
            String urlFichier ="/facinfosflash/imagesprofils/"+fichier+".jpg";
            // Construct the URL photo
            // Log.e(LOG_TAG, "URLPHOTO = " + Utility.urlserveurimage+urlPhotoprofile);
            Uri builtUriFichier= new Uri.Builder()
                    .scheme("http")
                    .authority(Utility.urlserveurimage.replaceFirst("http://",""))
                    .path(urlFichier)
                    .build();

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    fichier_imageview.setImageBitmap(bitmap);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            //this will help us to avoid the Target being gc'd
            fichier_imageview.setTag(target);

            Picasso.with(getActivity()).load(builtUriFichier.toString()).into(target);


            fichier_imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    @SuppressLint("ResourceType") Animation animFadein = AnimationUtils.loadAnimation(getActivity(), R.animator.fade_in);
                    v.startAnimation(animFadein);

                    affichePhotoFichier(fichier,"Image");


                }
            });

            supprimer_information_textview.setVisibility(View.VISIBLE);

            mDoodle = " :: "+mMessage + " :: "+fromServerUnicodeDecoded+" :: ";
            // If onCreateOptionsMenu has already happened, we need to update the share intent now.
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareDoodleIntent());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    public static View getChildAtPosition(final AdapterView view, final int position) {
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }

    public static void smoothScrollToPositionFromTop(final AbsListView view, final int position) {
        View child = getChildAtPosition(view, position);
        // There's no need to scroll if child is already at top or view is already scrolled to its end
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
            return;
        }

        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.setOnScrollListener(null);

                    // Fix for scrolling bug
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(position);
                        }
                    });
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                                 final int totalItemCount) { }
        });

        // Perform scrolling to position
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.smoothScrollToPositionFromTop(position, 0);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onButtonClickListner(int position, CommentaireDTO value, int actionComponent) {

        final CommentaireDTO commentaire = value;
        commentaireSelect = commentaire;
        positionCommentaireSelect = position;
        mPosition = position+1;

        switch (actionComponent) {

            case 3:{

                if (commentaire != null) {

                    id = commentaire.getCommentaireId();
                    if (isOnline()) {
                        AlertDialog diaBox = AskOptionDeleteCommentaire(id);
                        diaBox.show();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet2), Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            }

        }


    }




    public void affichePhotoFichier(final  String fileName, String nom){


        //set up dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fichier_dialog);
        dialog.setTitle(nom);
        dialog.setCancelable(true);


        final ProportionalImageView photoFichier = (ProportionalImageView) dialog.findViewById(R.id.fichierprofilDialogImage);

        File file = new File(Environment.getExternalStorageDirectory().getPath() +"/journal/" + fichier.substring(3)+".jpg");
        if(!file.exists()) {

            // URL image
            String urlPhotoprofile ="/facinfosflash/imagesprofils/"+fileName+".jpg";
            // Construct the URL photo
            // Log.e(LOG_TAG, "URLPHOTO = " + Utility.urlserveurimage+urlPhotoprofile);
            final Uri builtUri = new Uri.Builder()
                    .scheme("http")
                    .authority(Utility.urlserveurimage.replaceFirst("http://",""))
                    .path(urlPhotoprofile)
                    .build();
            // Log.e(LOG_TAG, "URI = " + builtUri.toString());

            Picasso.with(getActivity()).load(builtUri.toString()).into(photoFichier);

        } else {

            Picasso.with(getActivity()).load(file).into(photoFichier);

            //Toast.makeText(getActivity(), getResources().getString(R.string.system_message_internet2), Toast.LENGTH_SHORT).show();
        }




        final Button telechargerButton = (Button) dialog.findViewById(R.id.telecharger_button);
        telechargerButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_file_download_black_24dp), null, null, null);
        telechargerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);



                // URL image
                String urlFichierb ="/facinfosflash/imagesprofils/"+fichier+".jpg";
                // Construct the URL photo
                // Log.e(LOG_TAG, "URLPHOTO = " + Utility.urlserveurimage+urlPhotoprofile);
                Uri builtUriFichier= new Uri.Builder()
                        .scheme("http")
                        .authority(Utility.urlserveurimage.replaceFirst("http://",""))
                        .path(urlFichierb)
                        .build();

                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        photoFichier.setImageBitmap(bitmap);

                        try {

                            File direct = new File(Environment.getExternalStorageDirectory()
                                    + "/journal");

                            if (!direct.exists()) {
                                direct.mkdirs();
                            }

                            File file = new File(Environment.getExternalStorageDirectory().getPath() +"/journal/" +fichier.substring(3)+".jpg");
                            if(!file.exists()){
                                file.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                                ostream.close();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };

                //this will help us to avoid the Target being gc'd
                photoFichier.setTag(target);


                Picasso.with(getActivity()).load(builtUriFichier.toString()).into(target);


                Toast.makeText(getActivity(), getResources().getString(R.string.telecharger_message), Toast.LENGTH_SHORT).show();

                dialog.dismiss();

            }
        });


        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);

    }

    public AlertDialog AskOptionDeleteInformation()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle(getResources().getString(net.freelancertech.journal.app.R.string.desabonner_activity_message3))
                .setMessage(net.freelancertech.journal.app.R.string.supprimer_information_confirmation)
                .setPositiveButton(getResources().getString(net.freelancertech.journal.app.R.string.supprimer), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        String apiUrl = Utility.urlApiInfosFlash;
                        if (apiUrl != null) {

                            RestAdapter restAdapter = new RestAdapter.Builder()
                                    .setEndpoint(apiUrl).build();

                            InstantInterface instantDriverService = restAdapter.create(InstantInterface.class);

                            instantDriverService.supprimerInformationAndCommentaire(informationId, new Callback<Reponse>() {
                                @Override
                                public void success(Reponse reponse, Response response) {

                                    try {
                                        //Utility.sendPushNotification("Delete offer",getActivity());
                                        DoodlesArchiveSyncAdapter.syncImmediately(getActivity());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);


                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.system_message_server), Toast.LENGTH_SHORT).show();
                                }
                            });


                        } else {
                            Utility.initialiseUrl(getActivity());
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

        return myQuittingDialogBox;

    }


    public AlertDialog AskOptionDeleteCommentaire(final long commentaireId)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle(getResources().getString(net.freelancertech.journal.app.R.string.desabonner_activity_message3))
                .setMessage(net.freelancertech.journal.app.R.string.supprimer_commentaire_confirmation)
                .setPositiveButton(getResources().getString(net.freelancertech.journal.app.R.string.supprimer), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        String apiUrl = Utility.urlApiInfosFlash;
                        if (apiUrl != null) {

                            RestAdapter restAdapter = new RestAdapter.Builder()
                                    .setEndpoint(apiUrl).build();

                            InstantInterface instantDriverService = restAdapter.create(InstantInterface.class);

                            instantDriverService.supprimerCommentaire(commentaireId, new Callback<Reponse>() {
                                @Override
                                public void success(Reponse reponse, Response response) {

                                    udapteListCommentaire(1);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.system_message_server), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Utility.initialiseUrl(getActivity());
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

        return myQuittingDialogBox;

    }




}