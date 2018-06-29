package net.freelancertech.journal.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.BubbleImageView;
import com.squareup.picasso.Picasso;

import net.freelancertech.journal.app.R;
import net.freelancertech.journal.app.custom.CustomTouchListener;
import net.freelancertech.journal.app.model.CommentaireDTO;
import net.freelancertech.journal.app.utils.Utility;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Collections;
import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public class CommentaireAdapter extends BaseAdapter {

    private final String LOG_TAG = CommentaireAdapter.class.getSimpleName();

    CustomButtonListener customListner;
    public interface CustomButtonListener {
        public void onButtonClickListner(int position,CommentaireDTO value,  int actionComponent);
    }
    public void setCustomButtonListner(CustomButtonListener listener) {
        this.customListner = listener;
    }


    private  final Context context;
    private List<CommentaireDTO> mCommentaires = Collections.emptyList();


    private static class ViewHolder {
        public final EmojiconTextView mEtudiantCommentaireView;
        public final TextView mEtudiantDateCommentaireView;
        public final TextView mSupprimerView;

        public ViewHolder(EmojiconTextView etudiantCommentaireView,TextView etudiantDateCommentaireView, TextView supprimerView) {
            this.mEtudiantCommentaireView = etudiantCommentaireView;
            this.mEtudiantDateCommentaireView = etudiantDateCommentaireView;

            this.mSupprimerView = supprimerView;
        }
    }

    public CommentaireAdapter(Context context) {
        this.context = context;

    }

    public void updateCommentaires(List<CommentaireDTO> commentaires) {
        this.mCommentaires = commentaires;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mCommentaires.size();
    }

    @Override
    public CommentaireDTO getItem(int position) {
        return mCommentaires.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public View getView(final int position, View convertView, final ViewGroup parent) {

        EmojiconTextView etudiantCommentaireView ;
        TextView etudiantDateCommentaireView;

        TextView supprimerView;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.list_item_commentaire, parent, false);
            etudiantCommentaireView = (EmojiconTextView) convertView.findViewById(R.id.list_item_commentaire_etudiant_textview);
            etudiantDateCommentaireView = (TextView) convertView.findViewById(R.id.list_item_date_etudiant_commentaire_textview);
            supprimerView = (TextView) convertView.findViewById(R.id.list_item_supprimer_commenetaire_textview);

            convertView.setTag(new ViewHolder(etudiantCommentaireView,etudiantDateCommentaireView,supprimerView));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            etudiantCommentaireView = viewHolder.mEtudiantCommentaireView;
            etudiantDateCommentaireView = viewHolder.mEtudiantDateCommentaireView;

            supprimerView = viewHolder.mSupprimerView;
        }


        final CommentaireDTO commentaire = getItem(position);

        etudiantCommentaireView.setMovementMethod(LinkMovementMethod.getInstance());

        String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(commentaire.getCommentaire());
        etudiantCommentaireView.setText(fromServerUnicodeDecoded);

        etudiantDateCommentaireView.setText(commentaire.getDateEnregistrement());


        supprimerView.setVisibility(View.VISIBLE);


        supprimerView.setOnTouchListener(new CustomTouchListener());
        supprimerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position, commentaire, 3);
                }

            }
        });

        return convertView;
    }

}
