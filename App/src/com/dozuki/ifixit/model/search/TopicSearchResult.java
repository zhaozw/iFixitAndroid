package com.dozuki.ifixit.model.search;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dozuki.ifixit.MainApplication;
import com.dozuki.ifixit.R;
import com.dozuki.ifixit.model.Image;
import com.dozuki.ifixit.ui.guide.view.GuideViewActivity;
import com.dozuki.ifixit.ui.topic_view.TopicViewActivity;
import com.dozuki.ifixit.util.PicassoUtils;

import java.io.Serializable;

public class TopicSearchResult extends SearchResult implements Searchable, Serializable {
   private static final long serialVersionUID = -24643222443335L;

   public String mTitle;
   public String mDisplayTitle;
   public String mNamespace;
   public String mSummary;
   public String mUrl;
   public String mText;
   public Image mImage;


   @Override
   public View buildView(View v, LayoutInflater inflater, ViewGroup container) {
      if (v == null) {
         v = inflater.inflate(getLayout(), container, false);
      }

      ((TextView)v.findViewById(R.id.search_result_title)).setText(Html.fromHtml(mDisplayTitle));
      ImageView thumbnail = (ImageView)v.findViewById(R.id.search_result_thumbnail);

      v.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), TopicViewActivity.class);
            intent.putExtra(GuideViewActivity.TOPIC_NAME_KEY, mTitle);
            view.getContext().startActivity(intent);
         }
      });

      PicassoUtils.with(container.getContext())
       .load(mImage.getPath(MainApplication.get().getImageSizes().getThumb()))
       .into(thumbnail);

      return v;
   }

   @Override
   public int getLayout() {
      return R.layout.search_row;
   }
}
