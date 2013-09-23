package com.dozuki.ifixit.ui.gallery;

import android.util.Log;
import com.dozuki.ifixit.R;
import com.dozuki.ifixit.model.Image;
import com.dozuki.ifixit.model.user.UserImage;
import com.dozuki.ifixit.util.APIEvent;
import com.dozuki.ifixit.util.APIService;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class PhotoMediaFragment extends MediaFragment {
   @Override
   protected void retrieveUserMedia() {
      mNextPageRequestInProgress = true;
      ((GalleryActivity)getActivity()).showLoading(R.id.gallery_loading_container);

      APIService.call(getActivity(),
       APIService.getUserImagesAPICall("?limit=" + IMAGE_PAGE_SIZE));
   }

   @Subscribe
   public void onUserImages(APIEvent.UserImages event) {
      ((GalleryActivity)getActivity()).hideLoading();
      setEmptyListView();

      if (!event.hasError()) {
         ArrayList<UserImage> imageList = new ArrayList<UserImage>(event.getResult());
         if (imageList.size() > 0) {

            mMediaList.setItems(imageList);
            if (mAlreadyAttachedImages != null) {
               mMediaList.removeImagesWithIds(mAlreadyAttachedImages);
            }
            mGalleryAdapter.notifyDataSetChanged();
            mGalleryAdapter.invalidatedView();
         }

         mNextPageRequestInProgress = false;
      } else {
         APIService.getErrorDialog(getActivity(), event).show();
      }
   }

   @Subscribe
   public void onUploadImage(APIEvent.UploadImage event) {
      if (!event.hasError()) {
         Image image = event.getResult();
         String key = event.getExtraInfo();

         mMediaList.findAndReplaceByKey(key, image);

         mGalleryAdapter.notifyDataSetChanged();
         mGalleryAdapter.invalidatedView();
      } else {
         APIService.getErrorDialog(getActivity(), event).show();
      }
   }

   @Subscribe
   public void onDeleteImage(APIEvent.DeleteImage event) {
      if (!event.hasError()) {
         mGalleryAdapter.notifyDataSetChanged();
      } else {
         APIService.getErrorDialog(getActivity(), event).show();
      }
   }
}
